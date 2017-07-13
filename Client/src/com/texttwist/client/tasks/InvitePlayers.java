package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
import com.texttwist.client.pages.Page;
import com.texttwist.client.ui.TTDialog;
import models.Message;
import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 25/06/2017.
 * Description: Task: InvitePlayers
 *              Sends invite to all players of the match
 */
public class InvitePlayers extends SwingWorker<Void,Void> {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private DefaultListModel<String> userNames;

    public InvitePlayers(DefaultListModel<String> userNames) {
        this.userNames = userNames;
    }

    @Override
    public Void doInBackground() {
        buffer = ByteBuffer.allocate(1024);
        Message message = new Message("START_GAME", App.session.account.userName, App.session.token, userNames);

        byte[] byteMessage = message.toString().getBytes();
        buffer = ByteBuffer.wrap(byteMessage);
        try {
            App.clientTCP.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (App.clientTCP.read(buffer) != -1) {

                String line = new String(buffer.array(), buffer.position(), buffer.remaining());
                if (line.startsWith("MESSAGE")) {
                    Message msg = Message.toMessage(line);

                    if (msg.message.equals("USER_NOT_ONLINE")) {
                        new TTDialog("alert", "Users not online!",
                            new Callable() {
                                @Override
                                public Void call() throws Exception {
                                    buffer.clear();
                                    return null;
                                }
                            }, null);
                        buffer.clear();
                        return null;
                    }

                    if (msg.message.equals("INVITES_ALL_SENDED")) {
                        new TTDialog("success", "Invite all sended!",
                            new Callable() {
                                @Override
                                public Void call() throws Exception {
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
            App.logger.write("INVITE PLAYERS: Can't read from socket");
        }
        return null;
    }
}
