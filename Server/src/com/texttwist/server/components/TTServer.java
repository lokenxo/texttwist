package com.texttwist.server.components;

import com.texttwist.server.components.auth.TTAuth;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by loke on 15/06/2017.
 */
public class TTServer {

    private static final int PORT = 9999;

    public void start() {

        try {
            TTAuth auth = new TTAuth();
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.bind("auth", auth);
            System.out.println("Auth Service running at "+PORT+" port...");

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
