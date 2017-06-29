package com.texttwist.client.services;
import com.texttwist.client.App;
import com.texttwist.client.pages.Game;
import com.texttwist.client.pages.Home;
import com.texttwist.client.pages.Menu;
import com.texttwist.client.pages.Page;
import com.texttwist.client.ui.TTDialog;
import interfaces.INotificationClient;
import models.Response;
import utilities.Logger;

import javax.swing.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

/**
 * Created by loke on 15/06/2017.
 */
public class NotificationClient implements INotificationClient {


    public NotificationClient() throws RemoteException {
    }

    @Override
    public Response sendInvite(String userName, DefaultListModel<String> users) throws RemoteException {
        Logger.write("Invoked invitation with username=" + userName + "|" + users.toString() );

        if(users.contains(App.sessionService.account.userName)){
            Logger.write(userName+" ti ha sfidato!");
            App.match.newMatch(userName);
        }
        return null;
    }
}
