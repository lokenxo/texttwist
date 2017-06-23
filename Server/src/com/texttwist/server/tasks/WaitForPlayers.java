package com.texttwist.server.tasks;

import com.texttwist.server.Server;
import com.texttwist.server.components.NotificationServer;
import com.texttwist.server.models.Match;
import constants.Config;
import interfaces.INotificationServer;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Callable;

/**
 * Created by loke on 19/06/2017.
 */
public class WaitForPlayers implements Callable<Boolean> {
    private Match match;
    private String sender;

    public WaitForPlayers(Match match) {
        this.match = match;
    }

    @Override
    public Boolean call() throws Exception {
        System.out.print("Wait for players!");

        return true;
    }
}
