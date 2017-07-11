package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.MenuPage;
import com.texttwist.client.pages.Page;
import com.texttwist.client.ui.TTDialog;
import constants.Config;
import models.Message;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by loke on 25/06/2017.
 */
public class WaitForPlayers extends SwingWorker<DefaultListModel<String>,DefaultListModel<String>> {

    public SocketChannel socketChannel;
    public DefaultListModel<String> words;
    public DefaultListModel<String> letters;
    boolean joinTimeout = false;
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    SwingWorker callback;

    public WaitForPlayers(SwingWorker callback) {
        this.callback = callback;
        this.words = words;
        this.socketChannel = App.game.clientSocket;
    }

    @Override
    public DefaultListModel<String> doInBackground() {
        try {
            buffer = ByteBuffer.allocate(1024);
            String line1 = new String(buffer.array(), buffer.position(), buffer.remaining());
            TTDialog loading = new TTDialog("alert", "Waiting for users joins",null,null);
            buffer.flip();

            while (this.socketChannel.read(buffer) != -1) {

                String line = new String(buffer.array(), buffer.position(), buffer.remaining());
               // String line = new String()
                buffer.clear();

                if (line.startsWith("MESSAGE")) {
                    buffer.clear();
                    System.out.println("Mi arriva questo dal notificationServer " +  line);

                    Message msg = Message.toMessage(line);
                    System.out.println(msg.message);
                    if (msg.message.equals("JOIN_TIMEOUT")) {
                        loading.dispose();
                        joinTimeout = true;
                        System.out.println("JOIN TIMEOUT ENTERED");

                        new TTDialog("alert", "TIMEOUT!",
                            new Callable() {
                                @Override
                                public Object call() throws Exception {
                                    //socketChannel.close();
                                    return new MenuPage(Page.window);

                                }
                            }, null);
                        return new DefaultListModel<String>();
                    }

                    if (msg.message.equals("MATCH_NOT_AVAILABLE")) {
                        loading.dispose();
                        System.out.println("MATCH NOT AVAILABLE ENTERED");

                        joinTimeout = true;
                        new TTDialog("alert", "THE GAME IS NOT MORE AVAILABLE!",
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        //socketChannel.close();
                                        return new MenuPage(Page.window);

                                    }
                                }, null);
                        return new DefaultListModel<String>();
                    }

                    if (msg.message.equals("GAME_STARTED")) {
                        loading.dispose();
                        System.out.println("GAME STARTED ENTERED");

                        DefaultListModel<String> data;
                        if(msg.data !=null ) {
                            data= msg.data;

                            Integer multicastId = Integer.valueOf(data.remove(data.size()-2));
                            App.game.setMulticastId(multicastId);

                            App.game.multicastSocket = new MulticastSocket(App.game.multicastId);
                            InetAddress ia = InetAddress.getByName(Config.ScoreMulticastServerURI);
                            App.game.multicastSocket.joinGroup(ia);
                            letters = msg.data;


                            //socketChannel.close();
                            return words;
                        } else {
                            System.out.println("USCITO CON");
                            System.out.println(line);
                            return new DefaultListModel<>();
                        }

                    }
                    buffer = ByteBuffer.allocate(1024);

                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return new DefaultListModel<String>();
    }

    @Override
    public void done(){
        if(!joinTimeout) {
            try {
                App.game.setLetters(letters);
                this.callback.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        }
    }
}
