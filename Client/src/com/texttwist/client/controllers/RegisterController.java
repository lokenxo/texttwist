package com.texttwist.client.controllers;

import com.texttwist.client.App;
import models.Response;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Author:      Lorenzo Iovino on 17/06/2017.
 * Description: Controller of the Register Page
 */
public class RegisterController {

    public Response register(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        return App.authService.register(userName,password);
    }
}
