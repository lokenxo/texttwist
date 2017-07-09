package com.texttwist.client;

import com.texttwist.client.services.AuthService;
import com.texttwist.client.pages.HomePage;
import com.texttwist.client.models.Game;
import com.texttwist.client.services.NotificationClient;
import constants.Config;
import interfaces.INotificationClient;
import interfaces.INotificationServer;
import models.Session;
import utilities.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
    public static Session session;
    public static Game game;
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

        //Init models
        game = new Game();

        //Init services
        authService = new AuthService();

        app = this;

        HomePage home = new HomePage(this);

       /* app.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                try {
                    authService.logout(App.session.account.userName);
                    System.out.println("LOGOUT BECAUSE WINDOW CLOSED");
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
            }
        });*/
    }

    public static Point getWindowsPosition(){
        return new Point(app.getX(), app.getY());
    }

}
