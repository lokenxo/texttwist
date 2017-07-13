package com.texttwist.server.tasks;

import com.texttwist.server.Server;
import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 19/06/2017.
 * Description: Task: Send Invitations
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
            Server.notificationServer.sendInvitations(sender, users);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
