package com.texttwist.client.services;
import com.texttwist.client.App;
import com.texttwist.client.models.Game;
import constants.Config;
import interfaces.IAuth;
import interfaces.INotificationClient;
import interfaces.INotificationServer;
import models.Response;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Authentication Service
 */
public class AuthService {

    private String baseUrl = Config.getAuthServerURI().concat("/auth");

    public Response login(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        try {

            /* si registra per la callback */
            System.out.println("Registering for callback");
            INotificationClient callbackObj = new NotificationClient();
            App.game.stub = (INotificationClient) UnicastRemoteObject.exportObject(callbackObj, 0);

            App.game.server.registerForCallback(App.game.stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        IAuth auth = (IAuth) Naming.lookup(baseUrl);
        return auth.login(userName, password);
    }

    public Response register(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        IAuth auth = (IAuth) Naming.lookup(baseUrl);
        return auth.register(userName, password);
    }

    public Response logout(String userName, INotificationClient stub) throws RemoteException, NotBoundException, MalformedURLException {
        IAuth auth = (IAuth) Naming.lookup(baseUrl);
        return auth.logout(userName, App.session.token, stub);
    }
}
