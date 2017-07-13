package com.texttwist.server.tasks;

import com.texttwist.server.Server;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 19/06/2017.
 */
public class SendInvitations implements Callable<Boolean> {
    private DefaultListModel<String> users;
    private String sender;

    public SendInvitations(String sender, DefaultListModel<String> users) {
        this.users = users;
        this.sender = sender;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            System.out.println("INVIA INVITO A" + users);

            Server.notificationServer.sendInvitations(sender, users);

        } catch (Exception e) {
            System.out.println("Eccezione" + e);
            e.printStackTrace();
        }

        return true;
    }
}
