package com.texttwist.client.controllers;

import com.texttwist.client.App;
import models.Response;
import models.Session;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import static com.texttwist.client.App.authService;

/**
 * Author:      Lorenzo Iovino on 17/06/2017.
 * Description: Controller of the Menu Page
 */
public class MenuController {

    public void logout() throws RemoteException, NotBoundException, MalformedURLException {
        Response res = authService.logout(App.session.account.userName, App.notificationStub);
        if (res.code == 200){
            App.session = null;
        }
    }

    public Session getSession(){
        return App.session;
    }
}
