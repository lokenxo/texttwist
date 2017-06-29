package com.texttwist.client.tasks;

import com.texttwist.client.App;
import constants.Config;
import javafx.util.Pair;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by loke on 28/06/2017.
 */
public class FetchHighscore extends SwingWorker<Void,Void> {

    DefaultListModel<Pair<String,Integer>> globalRanks = new DefaultListModel<Pair<String,Integer>>();
    Callable<String> callback;
    SocketChannel socketChannel;
    ByteBuffer buffer = ByteBuffer.allocate(1024);

    //TODO PASSARE LA CALLBACK ALLO SWING WORKER ED ESEGUIRLA AL DONE
    public FetchHighscore(Callable<String> callback, SocketChannel socketChannel){
        this.socketChannel = socketChannel;
        this.callback = callback;
    }

    @Override
    public Void doInBackground() {
        Message message = new Message("FETCH_HIGHSCORES", App.sessionService.account.userName, App.sessionService.account.token, new DefaultListModel<>());
        buffer.flip();
        buffer.clear();
        byte[] byteMessage = new String(message.toString()).getBytes();
        buffer = ByteBuffer.wrap(byteMessage);
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            buffer = ByteBuffer.allocate(1024);

            while (socketChannel.read(buffer) != -1) {

                buffer.clear();

                String line = new String(buffer.array(), buffer.position(), buffer.remaining());

                if (line.startsWith("MESSAGE")) {
                    Message msg = Message.toMessage(line);
                    System.out.println(line);
                    if (msg.message.equals("HIGHSCORES")) {

                        for(int i = 0; i< msg.data.size()-1; i++){
                            String[] splitted = msg.data.get(i).split(":");
                            System.out.println(splitted.toString());
                            globalRanks.addElement(new Pair<String, Integer>(splitted[0],new Integer(splitted[1])));
                        }

                        App.match.globalRanks = globalRanks;

                        System.out.println(globalRanks);
                        /*new TTDialog("alert", "Users not online!",
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        return null;
                                    }
                                }, null);*/
                        return null;

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void done(){
        System.out.println("Done");
        App.match.globalRanks = globalRanks;
        try {
            this.callback.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

