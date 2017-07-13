package com.texttwist.server.proxies;

import com.texttwist.server.Server;
import com.texttwist.server.services.SessionsService;
import com.texttwist.server.models.Match;
import com.texttwist.server.tasks.*;
import models.Message;
import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;


/**
 * Author:      Lorenzo Iovino on 18/06/2017.
 * Description: Message Dispatcher
 * */
public class MessageDispatcher implements Callable<Boolean> {
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final Message request;
    private final SocketChannel socketChannel;
    private ByteBuffer bufferMessage;
    private boolean matchNotAvailable = false;

    public MessageDispatcher(Message request, SocketChannel socketChannel, ByteBuffer bufferMessage) {
        this.request = request;
        this.socketChannel = socketChannel;
        this.bufferMessage = bufferMessage;
    }

    @Override
    public Boolean call() {
        bufferMessage = ByteBuffer.allocate(1024);
        byte[] byteMessage = null;
        if(SessionsService.getInstance().isValidToken(request.token)){
            switch(request.message){

                case "START_GAME":
                    Future<Boolean> onlineUsers = threadPool.submit(new CheckOnlineUsers(request.data));
                    try {
                        //Check if invited users are online
                        Boolean usersOnline = onlineUsers.get();
                        if(usersOnline){
                            Future<Boolean> sendInvitations = threadPool.submit(new SendInvitations(request.sender, request.data));
                            Boolean invitationSended = sendInvitations.get();
                            if (invitationSended) {

                                //Server create new match
                                request.data.addElement(request.sender);
                                final Match match = new Match(request.sender, request.data);
                                Match.activeMatches.add(match);

                                DefaultListModel<String> matchName = new DefaultListModel<>();
                                matchName.addElement(request.sender);

                                //Match creator join match
                                Future<Boolean> joinMatch = threadPool.submit(new JoinMatch(request.sender, matchName, socketChannel));
                                Boolean joinMatchRes = joinMatch.get();

                                //Notify to the client that invites was sents correctly
                                if(!joinMatchRes){
                                    bufferMessage = ByteBuffer.allocate(1024);
                                    Message message = new Message("INVITES_ALL_SENDED", "", "", new DefaultListModel<>());
                                    byteMessage = message.toString().getBytes();
                                    bufferMessage = ByteBuffer.wrap(byteMessage);
                                    socketChannel.write(bufferMessage);
                                }

                                //Starts to wait until all player joins
                                Future<Boolean> joinTimeout = threadPool.submit(new JoinTimeout(match));
                                Boolean joinTimeoutRes = joinTimeout.get();
                                //If joinTimeoutRes==true timeout happen, need to notify to all waiting clients
                                if(joinTimeoutRes){
                                    Future<Boolean> sendMessageJoinTimeout = threadPool.submit(
                                            new SendMessageToAllPlayers(match,
                                                    new Message("JOIN_TIMEOUT", "", "", new DefaultListModel<>()), socketChannel));
                                    Boolean sendMessageJoinTimeoutRes = sendMessageJoinTimeout.get();
                                    if(!sendMessageJoinTimeoutRes){
                                        Match.activeMatches.remove(Match.findMatchIndex(Match.activeMatches, match.matchCreator));
                                        return sendMessageJoinTimeoutRes;
                                    }
                                } else {
                                    //All done, all player joined
                                    return true;
                                }
                            }
                        } else {
                            //Some user in the list is not online
                            Message message = new Message("USER_NOT_ONLINE", "", "", new DefaultListModel<>());
                            byteMessage = message.toString().getBytes();
                            bufferMessage.clear();
                            bufferMessage = ByteBuffer.wrap(byteMessage);
                            this.socketChannel.write(bufferMessage);
                            return false;
                        }
                    } catch (InterruptedException e) {
                        Server.logger.write("MESSAGE DISPATCHER - START GAME: InterruptedException");
                    } catch (ExecutionException e) {
                        Server.logger.write("MESSAGE DISPATCHER - START GAME: ExecutionException");
                    } catch (IOException e) {
                        Server.logger.write("MESSAGE DISPATCHER - START GAME: IOException");
                    }

                case "FETCH_HIGHSCORES":
                    //Fetch hisghscore and send back to client
                    Future<DefaultListModel<String>> computeHighscores = threadPool.submit(new ComputeHighscores());
                    try {
                        DefaultListModel<String> computeHighscoresRes = computeHighscores.get();
                            bufferMessage.clear();
                            bufferMessage = ByteBuffer.allocate(1024);

                            Message message = new Message("HIGHSCORES", "", "", computeHighscoresRes);
                            byteMessage = message.toString().getBytes();

                            bufferMessage = ByteBuffer.wrap(byteMessage);
                            try {
                                socketChannel.write(bufferMessage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        return false;
                    } catch (InterruptedException e) {
                        Server.logger.write("MESSAGE DISPATCHER - FETCH HIGHSCORES: InterruptedException");
                    } catch (ExecutionException e) {
                        Server.logger.write("MESSAGE DISPATCHER - FETCH HIGHSCORES: ExecutionException");
                    }

                case "JOIN_GAME":
                    //An user joined the game
                    Future<Boolean> joinMatch = threadPool.submit(new JoinMatch(request.sender, request.data, socketChannel));
                    try {
                        Match match = Match.findMatch(Match.activeMatches, request.data.get(0));;
                        Boolean joinMatchRes = joinMatch.get();

                        //If joinMatchRes=true start the game! Because all player joined
                        if(joinMatchRes){

                            //If match not fired join timeout, notify all player that game is started
                            if(!match.joinTimeout) {

                                //Generate letters to send to clients
                                Future<DefaultListModel<String>> generateLetters = threadPool.submit(new GenerateLetters());
                                match.setLetters(generateLetters.get());
                                match.letters.addElement(String.valueOf(match.multicastId));

                                for (int i = 0; i < match.playersSocket.size(); i++) {
                                    SocketChannel socketClient = match.playersSocket.get(i).getValue();
                                    if (socketClient != null) {
                                        bufferMessage.clear();
                                        bufferMessage = ByteBuffer.allocate(1024);

                                        Message message = new Message("GAME_STARTED", "", "", match.letters);
                                        match.startGame();
                                        byteMessage = message.toString().getBytes();
                                        bufferMessage = ByteBuffer.wrap(byteMessage);
                                        try {
                                            String s = new String(bufferMessage.array(), bufferMessage.position(), bufferMessage.remaining());
                                            socketClient.write(bufferMessage);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                if (matchNotAvailable) {
                                    matchNotAvailable = false;
                                    return false;
                                }
                            }
                        } else {
                            //Match doesn't exist more because a timeout happen
                            if(match == null){
                                bufferMessage = ByteBuffer.allocate(1024);

                                if (socketChannel != null) {
                                    bufferMessage = ByteBuffer.allocate(1024);
                                    Message msg = new Message("MATCH_NOT_AVAILABLE", "", null, new DefaultListModel<>());
                                    bufferMessage.clear();
                                    byteMessage = msg.toString().getBytes();
                                    bufferMessage = ByteBuffer.wrap(byteMessage);
                                    socketChannel.write(bufferMessage);
                                    matchNotAvailable = true;
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        Server.logger.write("MESSAGE DISPATCHER - JOIN GAME: InterruptedException");
                    } catch (ExecutionException e) {
                        Server.logger.write("MESSAGE DISPATCHER - JOIN GAME: ExecutionException");
                    } catch (IOException e) {
                        Server.logger.write("MESSAGE DISPATCHER - JOIN GAME: IOException");
                    }
                default:
                    break;
            }
        } else {
            //If token is invalid, return error message to client
            threadPool.submit(new TokenInvalid(request.sender, socketChannel, bufferMessage));
            return false;
        }
        return false;
    }
}
