package com.texttwist.server;

import com.texttwist.server.services.AuthService;
import com.texttwist.server.services.MessageService;
import com.texttwist.server.services.NotificationService;
import com.texttwist.server.services.ReceiveWordsService;
import constants.Config;
import interfaces.INotificationServer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import utilities.Logger;
import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


/**
 * Author:      Lorenzo Iovino on 15/06/2017.
 * Description: Server. Initialize all  services.
 */
public class Server {
    public static NotificationService notificationServer;
    public static JedisPool jedisPool;
    public static Logger logger;
    public static AuthService auth;

    public Server() throws IOException {
        logger = new Logger(new File("./notificationServer.log"), "Server", true);
        Server.logger.write("Services starting ...");
        startAuthService();
        startJedisService();
        startMessageService();
        startWordsReceiverService();
        startNotificationService();
        Server.logger.write("Services started correctly ...");
    }

    private void startAuthService(){
        //Starting Auth service based on RMI
        try {
            auth = new AuthService(Config.AuthServerPort);
            Registry authRegistry = LocateRegistry.createRegistry(auth.serverPort);
            authRegistry.bind("auth", auth);
        } catch (RemoteException e) {
            Server.logger.write("SERVER: RMI authentication service error (Remote exception)");
        } catch (AlreadyBoundException e) {
            Server.logger.write("SERVER: RMI authentication service can't use this port because is busy ");
        }
    }

    private void startJedisService(){
        //Starting Jedis pool for Redis connection
        jedisPool = new JedisPool(new JedisPoolConfig(), Config.RedisServerURI);
    }

    private void startMessageService(){
        //Starting the Message service based on TCP
        new Thread(new MessageService(Config.GameServerPort)).start();
    }

    private void startWordsReceiverService(){
        //Starting the Receive Words service based on UDP
        new Thread(new ReceiveWordsService()).start();
    }

    private void startNotificationService(){
        //Starting Notification service based on RMI
        try {
            notificationServer = new NotificationService();
            INotificationServer stub = (INotificationServer) UnicastRemoteObject.exportObject(notificationServer, Config.NotificationServerPort);
            LocateRegistry.createRegistry(Config.NotificationServerStubPort);

            Registry notificationRegistry = LocateRegistry.getRegistry(Config.NotificationServerStubPort);
            notificationRegistry.bind(Config.NotificationServerName, stub);
        } catch (RemoteException e) {
            Server.logger.write("SERVER: RMI notification service error (Remote exception)");
        } catch (AlreadyBoundException e) {
            Server.logger.write("SERVER: RMI notification service can't use this port because is busy ");
        }
    }
}
