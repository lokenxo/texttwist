package com.texttwist.client.controllers;

import models.Response;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import static com.texttwist.client.App.authService;

/**
 * Author:      Lorenzo Iovino on 17/06/2017.
 * Description: Controller of the Register Page
 */
public class RegisterController {

    public Response register(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        return authService.register(userName,password);
    }
}
