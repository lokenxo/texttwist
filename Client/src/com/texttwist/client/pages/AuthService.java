package com.texttwist.client.pages;

import com.texttwist.client.constants.Config;
import interfaces.IAuth;
import models.Response;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by loke on 17/06/2017.
 */
public class AuthService {

    protected String baseUrl = Config.getRMIServerAddress().concat("/auth");

    public AuthService(){
    }

    public Response login(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        IAuth auth = (IAuth) Naming.lookup(baseUrl);
        return auth.login(userName, password);
    }

    public Response register(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        IAuth auth = (IAuth) Naming.lookup(baseUrl);
        return auth.register(userName, password);
    }

    public Response logout(String userName, String token) throws RemoteException, NotBoundException, MalformedURLException {
        IAuth auth = (IAuth) Naming.lookup(baseUrl);
        return auth.logout(userName, token);
    }
}
