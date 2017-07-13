package com.texttwist.client.controllers;

import com.texttwist.client.App;
import models.Response;
import models.Session;
import models.User;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import static com.texttwist.client.App.authService;

/**
 * Author:      Lorenzo Iovino on 20/06/2017.
 * Description: Controller of the Home Page
 */
public class HomeController {

    public Response login(String userName, String password) throws RemoteException, NotBoundException, MalformedURLException {
        Response res = authService.login(userName,password);
        if (res.code == 200){
            try {
                App.registerForNotifications();
            } catch (RemoteException e) {
                App.logger.write("AUTH SERVICE: Can't register for notification");
            }
            App.session = (new Session(new User(userName,password,0), res.data.get("token").toString()));
        }
        return res;
    }
}
