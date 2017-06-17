package com.texttwist.client.pages;

import com.texttwist.client.constants.Config;
import interfaces.ITTAuth;
import models.TTResponse;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by loke on 17/06/2017.
 */
public class RegisterController {

    public RegisterController(){
    }

    public TTResponse register(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        return App.authService.register(userName,password);
    }
}
