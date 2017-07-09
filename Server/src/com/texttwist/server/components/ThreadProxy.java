package com.texttwist.server.components;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.texttwist.server.models.Match;
import com.texttwist.server.tasks.*;
import constants.Config;
import javafx.util.Pair;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

import static com.texttwist.server.components.GameServer.activeMatches;

/**
 * Created by loke on 18/06/2017.
 */
public class ThreadProxy implements Callable<Boolean> {
    protected final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final Message request;
    private final SocketChannel socketChannel;
    private ByteBuffer bufferMessage;
    boolean matchNotAvailable =false;


    ThreadProxy(Message request, SocketChannel socketChannel, ByteBuffer bufferMessage) {
        this.request = request;
        this.socketChannel = socketChannel;
        this.bufferMessage = bufferMessage;

    }


    private Boolean isValidToken(String token){
        return SessionsManager.getInstance().isValidToken(token);
    }

    @Override
    public Boolean call() {
        bufferMessage = ByteBuffer.allocate(1024);
        byte[] byteMessage = null;
        if(isValidToken(request.token)){
            switch(request.message){
                case "START_GAME":
                    Future<Boolean> onlineUsers = threadPool.submit(new CheckOnlineUsers(request.data));
                    try {
                        Boolean usersOnline = onlineUsers.get();
                        if(usersOnline){
                            Future<Boolean> sendInvitations = threadPool.submit(new SendInvitations(request.sender, request.data));
                            try {
                                Boolean invitationSended = sendInvitations.get();
                                if (invitationSended) {

                                    //Crea nuova partita e attendi i giocatori
                                    request.data.addElement(request.sender);
                                    final Match match = new Match(request.sender, request.data);
                                    match.printAll();

                                    activeMatches.add(match);

                                    DefaultListModel<String> matchName = new DefaultListModel<>();
                                    matchName.addElement(request.sender);

                                    Future<Boolean> joinMatch = threadPool.submit(new JoinMatch(request.sender, matchName, socketChannel));
                                    Boolean joinMatchRes = joinMatch.get();

                                    if(!joinMatchRes){
                                        //NON FARE NULLA, ASPETTA GLI ALTRI
                                        Message message = new Message("INVITES_ALL_SENDED", "", "", new DefaultListModel<String>());
                                        byteMessage = message.toString().getBytes();
                                        bufferMessage = ByteBuffer.wrap(byteMessage);
                                        socketChannel.write(bufferMessage);


                                        Future<Boolean> joinTimeout = threadPool.submit(new JoinTimeout(match));
                                        match.timeout = joinTimeout;
                                        joinTimeout.get();
                                        if(match.joinTimeout){
                                            Future<Boolean> sendMessageJoinTimeout = threadPool.submit(
                                                    new SendMessageToAllPlayers(match, new Message("JOIN_TIMEOUT", "", "", new DefaultListModel<>()), socketChannel));
                                            Boolean sendMessageJoinTimeoutRes = sendMessageJoinTimeout.get();
                                            if(!sendMessageJoinTimeoutRes){
                                                activeMatches.remove(Match.findMatchIndex(activeMatches,match.matchCreator));
                                                return sendMessageJoinTimeoutRes;
                                            }
                                        } else {
                                            System.out.println("TIMEOUT FINITO SENZA EFFETTI");
                                        }
                                    }

                                } else {
                                    return false;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {

                            Message message = new Message("USER_NOT_ONLINE", "", "", new DefaultListModel<String>());
                            byteMessage = new String(message.toString()).getBytes();
                            bufferMessage.clear();
                            bufferMessage = ByteBuffer.wrap(byteMessage);
                            this.socketChannel.write(bufferMessage);
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                case "FETCH_HIGHSCORES":
                    Future<DefaultListModel<String>> computeHighscores = threadPool.submit(new ComputeHighscores());
                    try {
                        DefaultListModel<String> computeHighscoresRes = computeHighscores.get();
                        Message message = new Message("HIGHSCORES", "", "", computeHighscoresRes);
                        byteMessage  = message.toString().getBytes();

                        bufferMessage = ByteBuffer.wrap(byteMessage);
                        try {
                            socketChannel.write(bufferMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                case "JOIN_GAME":
                    Future<Boolean> joinMatch = threadPool.submit(new JoinMatch(request.sender, request.data, socketChannel));
                    try {
                        Match match = Match.findMatch(activeMatches, request.data.get(0));;
                        Boolean joinMatchRes = joinMatch.get();
                        if(joinMatchRes){

                            if(!match.joinTimeout) {
                                Future<DefaultListModel<String>> generateLetters = threadPool.submit(new GenerateLetters());
                                match.setLetters(generateLetters.get());
                                match.letters.addElement(String.valueOf(match.multicastId));

                                for (int i = 0; i < match.playersSocket.size(); i++) {
                                    SocketChannel socketClient = match.playersSocket.get(i).getValue();
                                    if (socketClient != null) {
                                        bufferMessage.clear();
                                        Message message = new Message("GAME_STARTED", "", "", match.letters);
                                        match.startGame();

                                        match.timeout.cancel(true);
                                        System.out.println("TIMEOUT CANCELLEd");
                                        byteMessage = message.toString().getBytes();

                                        bufferMessage = ByteBuffer.wrap(byteMessage);
                                        try {
                                            socketClient.write(bufferMessage);
                                        } catch (IOException e) {

                                        }
                                        //clientSocket.close();
                                    }
                                }
                                if (matchNotAvailable) {
                                    return false;
                                }
                            }
                            //RISPONDI CON LA CLASSIFICA
                           // break;
                            //ULTIMO A JOINARE! INIZIA GIOCO
                        } else {
                            if(match == null){
                                bufferMessage = ByteBuffer.allocate(1024);
                                if (socketChannel != null) {
                                    Message msg = new Message("MATCH_NOT_AVAILABLE", "", null, new DefaultListModel<>());
                                    bufferMessage.clear();
                                    byteMessage = msg.toString().getBytes();
                                    bufferMessage = ByteBuffer.wrap(byteMessage);
                                    socketChannel.write(bufferMessage);
                                    matchNotAvailable = true;
                                }
                                //Match non disponibile

                            }
                            //NON FARE NULLA, ASPETA GLI ALTRI
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                default:

                    break;
            }

        } else {
            System.out.print("TOKEN NON VALIDO");
            return false;
        }

        return false;
    }
}
