package com.texttwist.client.models;

import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
import com.texttwist.client.pages.MenuPage;
import com.texttwist.client.pages.Page;
import com.texttwist.client.services.NotificationClient;
import com.texttwist.client.tasks.InvitePlayers;
import com.texttwist.client.ui.TTDialog;
import constants.Config;
import interfaces.INotificationClient;
import interfaces.INotificationServer;
import javafx.util.Pair;
import models.Message;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;

/**
 * Created by loke on 18/06/2017.
 */
public class Game {

    public Integer multicastId = 0 ;
    public DefaultListModel<String> pendingList = new DefaultListModel<String>();
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    public DefaultListModel<String> words = new DefaultListModel<String>();
    public DefaultListModel<String> letters = new DefaultListModel<String>();
    public DefaultListModel<Pair<String,Integer>> ranks = new DefaultListModel<>();
    public INotificationClient stub;
    public DefaultListModel<Pair<String,Integer>> globalRanks = new DefaultListModel<>();
    public MulticastSocket multicastSocket;
    public INotificationServer server;

    public SocketChannel clientSocket = null;

    public Game(){

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(Config.NotificationServerStubPort);
            server = (INotificationServer) registry.lookup(Config.NotificationServerName);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        InetSocketAddress socketAddress = new InetSocketAddress(Config.GameServerURI, Config.GameServerPort);
        try {
            clientSocket = SocketChannel.open(socketAddress);
            clientSocket.configureBlocking(false);
            System.out.println("Join multicast group");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void newMatch(String userName) {
        //Aggiungi alla lista di inviti
        try {
            this.addToPendingList(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Visualizza popup
        new TTDialog("success", "New invitation from: " + userName + "!",
            new Callable() {
                @Override
                public Object call() throws Exception {
                    App.game.joinMatch(userName);
                    return null;
                }
            },
            new Callable() {
                @Override
                public Object call() throws Exception {
                    return new MenuPage(Page.window);
                }
            });
    }


    public void setWords(DefaultListModel<String> words){
        this.words = words;
    }



    public void setLetters(DefaultListModel<String> letters){
        this.letters = letters;
    }

    public void joinMatch(String matchName) {
        //Svuota la lista dei game pendenti e joina il game selezionato
        this.pendingList.clear();
        try {
            //Invia tcp req a server per dirgli che sto joinando
            DefaultListModel<String> matchNames = new DefaultListModel<String>();
            matchNames.addElement(matchName);
            Message message = new Message("JOIN_GAME", App.session.account.userName, App.session.token, matchNames);

            byte[] byteMessage = new String(message.toString()).getBytes();
            buffer = ByteBuffer.wrap(byteMessage);
            clientSocket.write(buffer);

            new GamePage(Page.window);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Object play(DefaultListModel<String> userNames) throws IOException {
        SwingWorker worker = new InvitePlayers(userNames,clientSocket);
        worker.execute();
        return null;
    }

    public void setMulticastId(Integer multicastId){
        this.multicastId = multicastId;
    }

    public void addToPendingList(String username) throws IOException {
        pendingList.addElement(username);
    }
}
