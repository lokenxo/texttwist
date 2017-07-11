package com.texttwist.client.services;
import com.texttwist.client.App;
import interfaces.INotificationClient;
import models.Response;
import utilities.Logger;

import javax.swing.*;
import java.rmi.RemoteException;

/**
 * Created by loke on 15/06/2017.
 */
public class NotificationClient implements INotificationClient {


    public NotificationClient() throws RemoteException {
    }

    @Override
    public Response sendInvite(String userName, DefaultListModel<String> users) throws RemoteException {
        Logger.write("Invoked invitation with username =" + userName + "|" + users.toString() );

        if(App.session != null) {
            if (users.contains(App.session.account.userName)) {
                Logger.write(userName + " send a invitation!");
                App.game.newMatch(userName);
            } else {
                Logger.write("User " + userName + " is slogged");
            }
        }
        return null;
    }
}
