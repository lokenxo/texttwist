package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.Game;
import com.texttwist.client.pages.Menu;
import com.texttwist.client.pages.Page;
import com.texttwist.client.ui.TTDialog;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by loke on 25/06/2017.
 */
public class InvitePlayers extends SwingWorker<Boolean,Void> {

    ByteBuffer buffer = ByteBuffer.allocate(1024);

    public DefaultListModel<String> userNames;
    public SocketChannel socketChannel;

    public InvitePlayers(DefaultListModel<String> userNames, SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.userNames = userNames;
    }
    @Override
    public Boolean doInBackground() {
        Message message = new Message("START_GAME", App.sessionService.account.userName, App.sessionService.account.token, userNames);

        byte[] byteMessage = new String(message.toString()).getBytes();
        buffer = ByteBuffer.wrap(byteMessage);
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (socketChannel.read(buffer) != -1) {
                buffer.clear();

                String line = new String(buffer.array(), buffer.position(), buffer.remaining());

                if (line.startsWith("MESSAGE")) {
                    Message msg = Message.toMessage(line);
                    if (msg.message.equals("USER_NOT_ONLINE")) {
                        new TTDialog("alert", "Users not online!",
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        return null;
                                    }
                                }, null);
                        return null;

                    }

                    if (msg.message.equals("INVITES_ALL_SENDED")) {
                        // clientSocket.close();
                        new TTDialog("success", "Invite all sended!",
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        //In attesa dei giocatori
                                        new Game(Page.window);
                                        return null;
                                    }
                                }, null);
                        return null;

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void done() {
        System.out.println("DDDDDDDD22");

    }
}
