package com.texttwist.client;

import com.texttwist.client.pages.AuthService;
import com.texttwist.client.pages.Home;
import com.texttwist.client.pages.MatchService;
import com.texttwist.client.pages.SessionService;
import com.texttwist.client.services.NotificationClient;
import constants.Config;
import interfaces.INotificationClient;
import interfaces.INotificationServer;
import utilities.Logger;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by loke on 13/06/2017.
 */
public class App extends JFrame {

    public static AuthService authService;
    public static SessionService sessionService;
    public static MatchService matchService;
    public static JFrame app;

    public App() throws IOException {
        setPreferredSize( new Dimension( 640, 480 ));
        setSize(new Dimension(640,480));
        setLocation(100,100);
        setResizable( false );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Definitions of registry for auth
        long id = Thread.currentThread().getId();
        Logger logger = new Logger(new File("./client_"+id+".log"), "Client"+id);

        Logger.write("Client starting ...");
        try {
            Registry registry = LocateRegistry.getRegistry(Config.NotificationServerStubPort);
            INotificationServer server = (INotificationServer) registry.lookup(Config.NotificationServerName);

            /* si registra per la callback */
            System.out.println("Registering for callback");
            INotificationClient callbackObj = new NotificationClient();
            INotificationClient stub = (INotificationClient) UnicastRemoteObject.exportObject(callbackObj, 0);

            server.registerForCallback(stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        //Init services
        authService = new AuthService();
        sessionService = new SessionService();
        matchService = new MatchService();
        app = this;
        Home home = new Home(this);
    }

    public static Point getWindowsPosition(){
        return new Point(app.getX(), app.getY());
    }

}
