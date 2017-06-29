package com.texttwist.client.controllers;
import com.texttwist.client.App;
import models.Response;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by loke on 15/06/2017.
 */
public class HomeController {

    public HomeController(){
    }

    public Response login(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        Response res = App.authService.login(userName,password);
        if (res.code == 200){
            App.sessionService.create(userName, res.data.get("token").toString());
        }
        return res;
    }
}
