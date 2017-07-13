package com.texttwist.server.services;

import com.texttwist.server.Server;
import constants.Config;
import interfaces.INotificationClient;
import interfaces.INotificationServer;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Author:      Lorenzo Iovino on 19/06/2017.
 * Description: Notification Service
 */
public class NotificationService implements INotificationServer {

    private List<INotificationClient> clients;
    public NotificationService() throws RemoteException {
        super();
        Server.logger.write("Notification Service running at "+ Config.NotificationServerPort+" port...");
        clients = new ArrayList<>();
    }

    public synchronized void registerForCallback(INotificationClient clientInterface) throws RemoteException {
        if(!clients.contains(clientInterface)){
            clients.add(clientInterface);
        }
    }

    public synchronized void unregisterForCallback(INotificationClient client) throws RemoteException {
        clients.remove(client);
    }

    public synchronized void sendInvitations(String username, DefaultListModel<String> users){
        Iterator i = clients.iterator();
        INotificationClient client = null;
        while (i.hasNext()) {
            client = (INotificationClient) i.next();
            try {
                client.sendInvite(username, users);
            } catch (RemoteException e) {
                try {
                    unregisterForCallback(client);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
