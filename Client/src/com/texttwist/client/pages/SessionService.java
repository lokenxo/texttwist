package com.texttwist.client.pages;

import interfaces.ITTAuth;
import models.TTAccount;
import models.TTResponse;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by loke on 17/06/2017.
 */
public class SessionService {

    protected TTAccount account;

    public SessionService(){}

    public void create(String userName, String token) {
        account = new TTAccount(userName, token);
    }

    public void remove(){
        account = null;
    }
}
