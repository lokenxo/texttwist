package com.texttwist.server;

import com.texttwist.server.components.Auth;
import com.texttwist.server.components.GameServer;
import com.texttwist.server.components.NotificationServer;
import constants.Config;
import interfaces.INotificationServer;
import utilities.Logger;

import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by loke on 15/06/2017.
 */
public class Server {
    public static NotificationServer notificationServer;
    public Server() throws IOException {
        //Start services
        Logger logger = new Logger(new File("./server.log"), "Server");

        Logger.write("Server starting ...");
        try {
            //Definitions of registry for auth
            Auth auth = new Auth(Config.AuthServerPort);
            Registry authRegistry = LocateRegistry.createRegistry(auth.serverPort);
            authRegistry.bind("auth", auth);

            GameServer gameServer = new GameServer(Config.GameServerPort);
            new Thread(gameServer).start();

            try {
                /*registrazione presso il registry */
                notificationServer = new NotificationServer();
                INotificationServer stub = (INotificationServer) UnicastRemoteObject.exportObject(notificationServer, Config.NotificationServerPort);
                LocateRegistry.createRegistry(Config.NotificationServerStubPort);
                Registry notificationRegistry = LocateRegistry.getRegistry(Config.NotificationServerStubPort);
                notificationRegistry.bind(Config.NotificationServerName, stub);


            } catch (Exception e) {
                System.out.println("Eccezione" + e);
            }
            Logger.write("Server started");
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }


}
