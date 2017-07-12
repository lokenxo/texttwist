package com.texttwist.client.controllers;

import com.texttwist.client.App;
import interfaces.INotificationClient;
import models.Response;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Author:      Lorenzo Iovino on 17/06/2017.
 * Description: Controller of the Menu Page
 */
public class MenuController {

    public void logout(String userName, INotificationClient stub) throws RemoteException, NotBoundException, MalformedURLException {
        Response res = App.authService.logout(userName, stub);
        if (res.code == 200){
            App.session = null;
        }
    }
}
