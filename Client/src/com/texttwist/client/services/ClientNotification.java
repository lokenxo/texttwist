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
public class ClientNotification implements INotificationClient {


    public ClientNotification() throws RemoteException {
    }

    @Override
    public Response sendInvite(String userName, DefaultListModel<String> users) throws RemoteException {
        Logger.write("Invoked invitation with username=" + userName + "|" + users.toString() );

        if(users.contains(App.sessionService.account.userName)){
            Logger.write(userName+" ti ha sfidato!");
        }
        //Aggiungi alla lista di inviti
      /*  if ((userName != null && !userName.isEmpty()) && (password != null && !password.equals(""))) {
            if(AccountsManager.getInstance().register(userName, password)){
                Logger.write("Registration successfull");
                return new Response("Registration successfull", 200, null);
            } else {
                Logger.write("Registration unsuccessfull");
                return new Response("Registration unsuccessfull: Username exist!", 400, null);
            }
        }*/
        return new Response("Invitation received!", 200, null);
    }

}
