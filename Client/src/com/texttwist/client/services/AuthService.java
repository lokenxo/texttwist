package com.texttwist.client.services;

import com.texttwist.client.App;
import constants.Config;
import interfaces.IAuth;
import interfaces.INotificationClient;
import models.Response;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Author:      Lorenzo Iovino on 17/06/2017.
 * Description: Auth Service.
 *              Provide the interface for authentication
 */
public class AuthService {

    private String baseUrl = Config.getAuthServiceURI().concat("/auth");

    public Response login(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
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
