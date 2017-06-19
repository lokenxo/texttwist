package com.texttwist.client.pages;

import com.texttwist.client.App;
import models.Response;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by loke on 18/06/2017.
 */
public class MatchSetupController {

    public MatchSetupController(){}
    public Response play(DefaultListModel<String> userNames) throws RemoteException, NotBoundException, MalformedURLException {
        try {
            return App.matchService.play(userNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
