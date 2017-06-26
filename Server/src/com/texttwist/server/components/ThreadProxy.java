package com.texttwist.server.components;

import com.texttwist.server.models.Match;
import com.texttwist.server.tasks.*;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

import static com.texttwist.server.components.GameServer.activeMatches;

/**
 * Created by loke on 18/06/2017.
 */
public class ThreadProxy implements Callable<Boolean> {
    protected ExecutorService threadPool = Executors.newCachedThreadPool();
    private Message request;
    private SocketChannel socketChannel;


    ThreadProxy(Message request, SocketChannel socketChannel){
        this.request = request;
        this.socketChannel = socketChannel;
    }


    private Boolean isValidToken(String token){
        return SessionsManager.getInstance().isValidToken(token);
    }

    @Override
    public Boolean call() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] byteMessage = null;
        System.out.println("Selecting right task for new thread");
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
                                    Match match = new Match(request.sender, request.data);
                                    activeMatches.addElement(match);

                                    DefaultListModel<String> matchName = new DefaultListModel<>();
                                    matchName.addElement(request.sender);
                                    Future<Boolean> joinMatch = threadPool.submit(new JoinMatch(request.sender, matchName, socketChannel));
                                    Boolean joinMatchRes = joinMatch.get();

                                    if(!joinMatchRes){
                                        //NON FARE NULLA, ASPETTA GLI ALTRI
                                        Message message = new Message("INVITES_ALL_SENDED", "", "", new DefaultListModel<String>());
                                        byteMessage = new String(message.toString()).getBytes();
                                        buffer = ByteBuffer.wrap(byteMessage);
                                        socketChannel.write(buffer);


                                        Future<Boolean> matchTimeout = threadPool.submit(new MatchTimeout(match));
                                        Boolean matchTimeoutRes = matchTimeout.get();
                                        if(matchTimeoutRes){

                                        } else {

                                            //TODO If game is started not send this message
                                            if(!match.isStarted()) {
                                                for (int i = 0; i < match.playersSocket.size(); i++) {
                                                    socketChannel = match.playersSocket.get(i).getValue();
                                                    if (socketChannel != null) {
                                                        buffer.clear();
                                                        message = new Message("TIMEOUT", "", "", new DefaultListModel<>());
                                                        byteMessage = new String(message.toString()).getBytes();
                                                        buffer = ByteBuffer.wrap(byteMessage);
                                                        socketChannel.write(buffer);

                                                        //clientSocket.close();
                                                    }

                                                }
                                            }
                                        }

                                        return matchTimeoutRes;
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
                            buffer.clear();
                            buffer = ByteBuffer.wrap(byteMessage);
                            this.socketChannel.write(buffer);
                            //socketChannel.close();
                            return false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                case "JOIN_GAME":
                    Future<Boolean> joinMatch = threadPool.submit(new JoinMatch(request.sender, request.data, socketChannel));
                    try {
                        Boolean joinMatchRes = joinMatch.get();
                        if(joinMatchRes){
                            System.out.print("START THE FUCKING GAME!!!!");

                            //Find the game, send broadcast communication
//                            buffer.flip();


                            Match match = Match.findMatch(request.data.get(0));

                            Future<DefaultListModel<String>> generateWords = threadPool.submit(new GenerateWords());
                            match.setLetters(generateWords.get());

                            for (int i =0; i< match.playersSocket.size(); i++) {
                                System.out.println("INVIO");
                                socketChannel = match.playersSocket.get(i).getValue();
                                if(socketChannel!=null) {
                                    buffer.clear();
                                    Message message = new Message("GAME_STARTED", "", "", match.letters);
                                    match.startGame();
                                    byteMessage  = new String(message.toString()).getBytes();

                                    buffer = ByteBuffer.wrap(byteMessage);
                                    try {
                                        socketChannel.write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //clientSocket.close();
                                }

                            }
                            //ULTIMO A JOINARE! INIZIA GIOCO
                        } else {
                            System.out.print("WAIT FRIENDS");
                            //NON FARE NULLA, ASPETA GLI ALTRI
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } {

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
