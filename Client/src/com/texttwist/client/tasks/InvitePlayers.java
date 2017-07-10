package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
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
        buffer = ByteBuffer.allocate(1024);
        Message message = new Message("START_GAME", App.session.account.userName, App.session.token, userNames);

        byte[] byteMessage = new String(message.toString()).getBytes();
        buffer = ByteBuffer.wrap(byteMessage);
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (socketChannel.read(buffer) != -1) {

                String line = new String(buffer.array(), buffer.position(), buffer.remaining());

                if (line.startsWith("MESSAGE")) {
                    Message msg = Message.toMessage(line);
                    if (msg.message.equals("USER_NOT_ONLINE")) {
                        new TTDialog("alert", "Users not online!",
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        buffer.clear();
                                        return null;
                                    }
                                }, null);
                        buffer.clear();
                        return null;

                    }

                    if (msg.message.equals("INVITES_ALL_SENDED")) {
                        // clientSocket.close();
                        new TTDialog("success", "Invite all sended!",
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        //In attesa dei giocatori
                                        new GamePage(Page.window);
                                        buffer.clear();
                                        return null;
                                    }
                                }, null);
                        buffer.clear();
                        return null;

                    }
                }
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void done() {

    }
}
