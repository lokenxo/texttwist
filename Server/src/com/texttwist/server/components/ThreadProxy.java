package com.texttwist.server.components;

import com.texttwist.client.App;
import com.texttwist.server.models.Match;
import com.texttwist.server.tasks.*;
import constants.Config;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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
    private DatagramSocket datagramSocket;


    ThreadProxy(Message request, SocketChannel socketChannel, DatagramSocket datagramSocket){
        this.request = request;
        this.socketChannel = socketChannel;
        this.datagramSocket = datagramSocket;
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
                                    activeMatches.add(match);

                                    DefaultListModel<String> matchName = new DefaultListModel<>();
                                    matchName.addElement(request.sender);
                                    Future<Boolean> joinMatch = threadPool.submit(new JoinMatch(request.sender, matchName, socketChannel));
                                    Boolean joinMatchRes = joinMatch.get();

                                    if(!joinMatchRes){
                                        //NON FARE NULLA, ASPETTA GLI ALTRI
                                        Message message = new Message("INVITES_ALL_SENDED", "", "", new DefaultListModel<String>());
                                        byteMessage = message.toString().getBytes();
                                        buffer = ByteBuffer.wrap(byteMessage);
                                        socketChannel.write(buffer);


                                        Future<Boolean> joinTimeout = threadPool.submit(new JoinTimeout(match));
                                        Boolean joinTimeoutRes = joinTimeout.get();
                                        if(!joinTimeoutRes){
                                            Future<Boolean> sendMessageJoinTimeout = threadPool.submit(
                                                    new SendMessageToAllPlayers(match, new Message("JOIN_TIMEOUT", "", "", new DefaultListModel<>()), socketChannel));
                                            Boolean sendMessageJoinTimeoutRes = sendMessageJoinTimeout.get();
                                            if(!sendMessageJoinTimeoutRes){
                                                activeMatches.remove(Match.findMatchIndex(activeMatches,match.matchCreator));
                                                return sendMessageJoinTimeoutRes;
                                            }
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
                            buffer.clear();
                            buffer = ByteBuffer.wrap(byteMessage);
                            this.socketChannel.write(buffer);
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

                        buffer = ByteBuffer.wrap(byteMessage);
                        try {
                            socketChannel.write(buffer);
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
                        Boolean joinMatchRes = joinMatch.get();
                        if(joinMatchRes){
                            System.out.print("START THE GAME!!!!");

                            Match match = Match.findMatch(activeMatches, request.data.get(0));

                            Future<DefaultListModel<String>> generateLetters = threadPool.submit(new GenerateLetters());
                            match.setLetters(generateLetters.get());
                            match.letters.addElement(String.valueOf(match.multicastId));

                            for (int i =0; i< match.playersSocket.size(); i++) {
                                System.out.println("INVIO");
                                socketChannel = match.playersSocket.get(i).getValue();
                                if(socketChannel != null) {
                                    buffer.clear();
                                    Message message = new Message("GAME_STARTED", "", "", match.letters);
                                    match.startGame();
                                    byteMessage  = message.toString().getBytes();

                                    buffer = ByteBuffer.wrap(byteMessage);
                                    try {
                                        socketChannel.write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //clientSocket.close();
                                }

                            }

                            //Start receive words: tempo masimo 5 minuti per completare l'invio delle lettere.
                            Future<Boolean> receiveWords = threadPool.submit(new ReceiveWords(match, datagramSocket));
                            Boolean receiveWordsRes = receiveWords.get();
                            if(!receiveWordsRes){
                                match.setUndefinedScorePlayersToZero();
                                System.out.println("ZERO PUNTI a chi non ha ancora inviato le lettere, TIMER SCADUTO");
                            } else {
                                System.out.println("TUTTI I GIOCATORI HANNO CONSEGNATO IN TEMPO");
                            }

                            Message msg = new Message("FINALSCORE","SERVER","", match.getMatchPlayersScoreAsStringList());

                            MulticastSocket multicastSocket = new MulticastSocket(match.multicastId);
                            InetAddress ia = InetAddress.getByName(Config.ScoreMulticastServerURI);
                            DatagramPacket hi = new DatagramPacket(msg.toString().getBytes(), msg.toString().length(), ia, match.multicastId);
                            multicastSocket.send(hi);
                            activeMatches.remove(Match.findMatchIndex(activeMatches,match.matchCreator));


                            //RISPONDI CON LA CLASSIFICA
                            break;
                            //ULTIMO A JOINARE! INIZIA GIOCO
                        } else {
                            System.out.print("WAIT FRIENDS");
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
