package com.texttwist.server;

import com.texttwist.server.components.Auth;
import com.texttwist.server.components.GameServer;
import utilities.Logger;

import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by loke on 15/06/2017.
 */
public class Server {

    public Server() throws IOException {
        //Start services
        Logger logger = new Logger(new File("./server.log"), "Server");

        Logger.write("Server starting ...");
        try {
            //Definitions of registry for auth
            Auth auth = new Auth(9999);
            Registry registry = LocateRegistry.createRegistry(auth.serverPort);
            registry.bind("auth", auth);

            GameServer server = new GameServer(10000);
            new Thread(server).start();

            Logger.write("Server started");

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

}
