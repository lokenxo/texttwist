package com.texttwist.client;

import com.texttwist.client.services.GameService;
import com.texttwist.client.services.AuthService;
import com.texttwist.client.pages.HomePage;
import com.texttwist.client.services.NotificationClientService;
import constants.Config;
import interfaces.INotificationClient;
import interfaces.INotificationServer;
import models.Session;
import utilities.Logger;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Author:      Lorenzo Iovino on 13/06/2017.
 * Description: The App entrypoint.
 *              Here is possible to declare services globally accessible.
 */
public class App extends JFrame {

    private static InetSocketAddress clientTCPSocketAddress = new InetSocketAddress(Config.MessageServiceURI, Config.MessageServicePort);
    public static InetSocketAddress clientUDPSocketAddress = new InetSocketAddress(Config.WordsReceiverServiceURI, Config.WordsReceiverServicePort);
    private static InetAddress clientMulticastSocketAddress;

    public static AuthService authService;
    public static GameService gameService;
    public static Logger logger;
    public static Session session;
    public static JFrame app;

    public static INotificationClient notificationStub;
    public static MulticastSocket clientMulticast;
    public static SocketChannel clientTCP;
    public static DatagramChannel clientUDP;

    public App() throws IOException {
        setPreferredSize( new Dimension( 640, 480 ));
        setSize(new Dimension(640,480));
        setLocation(100,100);
        setResizable( false );
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*Setup logger*/
        String id = ManagementFactory.getRuntimeMXBean().getName();
        logger = new Logger(new File("./client_"+id+".log"), "Client"+id, true);
        logger.write("Client starting ...");

        /*Load fonts*/
        try {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
                    new File("./Client/resources/fonts/DK Trained Monkey.otf").getCanonicalPath())));
        } catch (IOException|FontFormatException e) {
            logger.write("APP: Font not found!");
        }

        /*services*/
        gameService = new GameService();
        authService = new AuthService();

        app = this;

        new HomePage(this);
    }

    public static void registerForNotifications() throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(Config.NotificationServiceStubPort);
        INotificationClient callbackObj = new NotificationClientService();
        notificationStub = (INotificationClient) UnicastRemoteObject.exportObject(callbackObj, 0);
        INotificationServer notificationServer = (INotificationServer) registry.lookup(Config.NotificationServiceName);
        notificationServer.registerForCallback(notificationStub);
    }

    public static void openClientTCPSocket(){
        try {
            clientTCP = SocketChannel.open(clientTCPSocketAddress);
            clientTCP.configureBlocking(false);
        } catch (IOException e) {
            logger.write("APP: Can't open client TCP socket!");
        }
    }

    public static void openClientMulticastSocket(Integer multicastId){
        try {
            App.gameService.setMulticastId(multicastId);
            clientMulticastSocketAddress = InetAddress.getByName(Config.ScoreMulticastServiceURI);
            clientMulticast = new MulticastSocket(gameService.multicastId);
            clientMulticast.joinGroup(clientMulticastSocketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeClientMulticastSocket(){
        //Leave group and close multicast socket
        try {
            App.clientMulticast.leaveGroup(InetAddress.getByName(Config.ScoreMulticastServiceURI));
            App.clientMulticast.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openClientUDPSocket(){
        try {
            clientUDP = DatagramChannel.open();
            clientUDP.bind(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Point getWindowsPosition(){
        return new Point(app.getX(), app.getY());
    }
}
