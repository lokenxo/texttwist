package com.texttwist.client.pages;

import com.texttwist.client.App;
import models.Response;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by loke on 18/06/2017.
 */
public class MatchSetupController {

    public MatchSetupController(){}
    public Response play(DefaultListModel<String> userNames) throws RemoteException, NotBoundException, MalformedURLException {

        System.out.print(userNames.toString());
      /*  Response res = App.authService.login(userName,password);
        if (res.code == 200){
            App.sessionService.create(userName, res.data.get("token").toString());
        }*/
        return null;
    }

}
