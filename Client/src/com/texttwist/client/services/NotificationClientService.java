package com.texttwist.client.services;

import com.texttwist.client.App;
import com.texttwist.client.tasks.BeginMatch;
import interfaces.INotificationClient;
import javax.swing.*;
import java.rmi.RemoteException;

/**
 * Author:      Lorenzo Iovino on 15/06/2017.
 * Description: Provide the interface for the notifications.
 */
public class NotificationClientService implements INotificationClient {

    @Override
    public void sendInvite(String userName, DefaultListModel<String> users) throws RemoteException {
        App.logger.write("Invoked invitation with username =" + userName + "|" + users.toString() );

        if(App.session != null) {
            if (users.contains(App.session.account.userName)) {
                App.logger.write(userName + " send a invitation!");
                new BeginMatch(userName).execute();
            } else {
                App.logger.write("User " + userName + " is slogged");
            }
        }
    }
}
