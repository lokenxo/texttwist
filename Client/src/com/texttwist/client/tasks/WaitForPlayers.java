package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.Menu;
import com.texttwist.client.pages.Page;
import com.texttwist.client.ui.TTDialog;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;

/**
 * Created by loke on 25/06/2017.
 */
public class WaitForPlayers extends SwingWorker<DefaultListModel<String>,DefaultListModel<String>> {

    public SocketChannel socketChannel;
    public DefaultListModel<String> words;
    public DefaultListModel<String> letters;

    ByteBuffer buffer = ByteBuffer.allocate(1024);
    SwingWorker callback;

    public WaitForPlayers(SwingWorker callback) {
        this.callback = callback;
        this.words = words;
        this.socketChannel = App.match.clientSocket;
    }

    @Override
    public DefaultListModel<String> doInBackground() {
        try {
            TTDialog loading = new TTDialog("alert", "Waiting for users joins",null,null);
            buffer.flip();
            while (this.socketChannel.read(this.buffer) != -1) {

                String line = new String(this.buffer.array(), this.buffer.position(), this.buffer.remaining());
                buffer.clear();

                if (line.startsWith("MESSAGE")) {
                    buffer.clear();

                    Message msg = Message.toMessage(line);
                    if (msg.message.equals("JOIN_TIMEOUT")) {
                        socketChannel.close();
                        loading.dispose();
                        new TTDialog("alert", "TIMEOUT!",
                            new Callable() {
                                @Override
                                public Object call() throws Exception {
                                    return new Menu(Page.window);

                                }
                            }, null);
                        return new DefaultListModel<String>();
                    }

                    if (msg.message.equals("GAME_STARTED")) {
                        loading.dispose();
                        DefaultListModel<String> data = msg.data;

                        System.out.println("HERE");
                        System.out.println(msg.data);
                        Integer multicastId = Integer.valueOf(data.remove(data.size()-2));
                        System.out.println(multicastId);
                        App.match.setMulticastId(multicastId);
                        letters = msg.data;

                        //socketChannel.close();
                        return words;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DefaultListModel<String>();
    }

    @Override
    public void done(){
        System.out.println("Done wait for players");
        try {
            System.out.println(letters);
            App.match.setLetters(letters);
            System.out.println("PAROLE IN INVIO");
            this.callback.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
