package com.texttwist.client.services;
import com.texttwist.client.App;
import constants.Config;
import interfaces.IAuth;
import models.Response;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Authentication Service
 */
public class AuthService {

    private String baseUrl = Config.getAuthServerURI().concat("/auth");

    public Response login(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        IAuth auth = (IAuth) Naming.lookup(baseUrl);
        return auth.login(userName, password);
    }

    public Response register(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        IAuth auth = (IAuth) Naming.lookup(baseUrl);
        return auth.register(userName, password);
    }

    public Response logout(String userName) throws RemoteException, NotBoundException, MalformedURLException {
        IAuth auth = (IAuth) Naming.lookup(baseUrl);
        return auth.logout(userName, App.sessionService.account.token);
    }
}
