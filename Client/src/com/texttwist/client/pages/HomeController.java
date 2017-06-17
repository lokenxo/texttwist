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
 * Created by loke on 15/06/2017.
 */
public class HomeController {

    public HomeController(){
    }

    public TTResponse login(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        TTResponse res = App.authService.login(userName,password);
        if (res.code == 200){
            App.sessionService.create(userName, password);
        }
        return res;
    }
}
