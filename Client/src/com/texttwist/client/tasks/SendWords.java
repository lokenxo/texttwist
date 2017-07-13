package com.texttwist.client.tasks;

import com.texttwist.client.App;
import models.Message;
import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * Author:      Lorenzo Iovino on 29/06/2017.
 * Description: Task: SendWords.
 *              Send words to server and when done it wait for score.
 */
public class SendWords extends SwingWorker<Void,Void> {

    private SwingWorker callback;
    private DefaultListModel<String> words = new DefaultListModel<>();

    public SendWords(DefaultListModel<String> words, SwingWorker callback){
        this.callback = callback;
        this.words = words;
    }

    @Override
    public Void doInBackground() {
        try {
            App.openClientUDPSocket();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.clear();

            Message msg = new Message("WORDS", App.session.account.userName, App.session.token, words);
            String sentence = msg.toString();
            buffer.put(sentence.getBytes());
            buffer.flip();

            App.clientUDP.send(buffer, App.clientUDPSocketAddress);
            System.out.println("SENDED" + sentence);
        } catch (UnknownHostException e) {
            App.logger.write("SEND WORDS: Host address not correct");
        } catch (IOException e) {
            App.logger.write("SEND WORDS: Can't write on socket");
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
