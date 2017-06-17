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
public class MenuController {

    public MenuController(){
    }

    public TTResponse logout(String userName, String token) throws RemoteException, NotBoundException, MalformedURLException {
        TTResponse res =  App.authService.logout(userName,token);
        if (res.code == 200){
            App.sessionService.remove();
        }
        return res;
    }
}
