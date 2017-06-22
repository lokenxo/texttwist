package com.texttwist.server.components;

import com.texttwist.server.tasks.CheckOnlineUsers;
import com.texttwist.server.tasks.SendInvitations;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

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

                                    Message message = new Message("INVITES_ALL_SENDED", "", "", new DefaultListModel<String>());
                                    byte[] byteMessage = new String(message.toString()).getBytes();
                                    buffer = ByteBuffer.wrap(byteMessage);
                                    socketChannel.write(buffer);
                                    socketChannel.close();
                                    return true;
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
                            byte[] byteMessage = new String(message.toString()).getBytes();
                            buffer = ByteBuffer.wrap(byteMessage);
                            socketChannel.write(buffer);
                            socketChannel.close();
                            return false;
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
