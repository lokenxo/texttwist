package com.texttwist.client.tasks;

import com.texttwist.client.App;
import constants.Config;
import javafx.util.Pair;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Created by loke on 29/06/2017.
 */
public class SendWords extends SwingWorker<Void,Void> {

    DefaultListModel<Pair<String,Integer>> ranks = new DefaultListModel<Pair<String,Integer>>();

    SwingWorker callback;
    DefaultListModel<String> words = new DefaultListModel<>();

    //TODO PASSARE LA CALLBACK ALLO SWING WORKER ED ESEGUIRLA AL DONE
    public SendWords(DefaultListModel<String> words, SwingWorker callback){
        this.callback = callback;
        this.words = words;
    }

    @Override
    public Void doInBackground() {
        try {
            InetSocketAddress myAddress = new InetSocketAddress(Config.WordsReceiverServerURI, Config.WordsReceiverServerPort);
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.bind(null);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.clear();
            System.out.println("SENDER=" + App.session.account.userName);
            Message msg = new Message("WORDS", App.session.account.userName, "", words);
            String sentence = msg.toString();
            buffer.put(sentence.getBytes());
            buffer.flip();
            datagramChannel.send(buffer, myAddress);
            System.out.println("WORDS INVIATE");
            System.out.println(sentence);

            return null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void done(){
        try {
            this.callback.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
