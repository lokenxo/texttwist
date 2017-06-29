package com.texttwist.client.controllers;
import com.texttwist.client.App;
import models.Response;
import models.Session;
import models.User;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by loke on 15/06/2017.
 */
public class HomeController {

    public Response login(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        Response res = App.authService.login(userName,password);
        System.out.println(res.data);
        if (res.code == 200){
            App.session = (new Session(new User(userName,password,0), res.data.get("token").toString()));
        }
        return res;
    }
}
