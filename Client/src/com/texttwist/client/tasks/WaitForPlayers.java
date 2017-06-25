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

/**
 * Created by loke on 25/06/2017.
 */
public class WaitForPlayers extends SwingWorker<DefaultListModel<String>,DefaultListModel<String>> {

    public SocketChannel socketChannel;
    public DefaultListModel<String> words;
    ByteBuffer buffer = ByteBuffer.allocate(1024);

    public WaitForPlayers(SocketChannel socketChannel, DefaultListModel<String> words) {
        this.socketChannel = socketChannel;
        this.words = words;
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
                    System.out.println("HEY");
                    System.out.println(msg);
                    if (msg.message.equals("TIMEOUT")) {
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
                        words = msg.data;
                        //socketChannel.close();
                        App.matchService.setWords(words);
                        return words;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DefaultListModel<String>();
    }

    public void done(){
        System.out.println("DONEEE");
    }
}
