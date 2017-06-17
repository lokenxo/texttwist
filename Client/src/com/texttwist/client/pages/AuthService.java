package com.texttwist.client.pages;

import com.texttwist.client.constants.Config;
import interfaces.ITTAuth;
import models.TTResponse;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by loke on 17/06/2017.
 */
public class AuthService {

    protected String baseUrl = Config.getRMIServerAddress().concat("/auth");

    public AuthService(){
    }

    public TTResponse login(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        ITTAuth auth = (ITTAuth) Naming.lookup(baseUrl);
        return auth.login(userName, password);
    }

    public TTResponse register(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        ITTAuth auth = (ITTAuth) Naming.lookup(baseUrl);
        return auth.register(userName, password);
    }

    public TTResponse logout(String userName, String token) throws RemoteException, NotBoundException, MalformedURLException {
        ITTAuth auth = (ITTAuth) Naming.lookup(baseUrl);
        return auth.logout(userName, token);
    }
}
