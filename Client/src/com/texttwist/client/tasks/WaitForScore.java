package com.texttwist.client.tasks;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.texttwist.client.App;
import constants.Config;
import javafx.util.Pair;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/**
 * Created by loke on 27/06/2017.
 */
public class WaitForScore extends SwingWorker<Void,Void> {

    DefaultListModel<Pair<String,Integer>> ranks = new DefaultListModel<Pair<String,Integer>>();

    SwingWorker callback;

    //TODO PASSARE LA CALLBACK ALLO SWING WORKER ED ESEGUIRLA AL DONE
    public WaitForScore(SwingWorker callback){
        this.callback = callback;
    }

    @Override
    public Void doInBackground() {
        InetAddress group = null;
        try {

            MulticastSocket ms = new MulticastSocket(App.match.multicastId);
            InetAddress ia = InetAddress.getByName(Config.ScoreMulticastServerURI);
            ms.joinGroup(ia);
            System.out.println("Join multicast group");

            byte[] buf = new byte[1024];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            ms.receive(recv);
            String s = new String(recv.getData());
            System.out.println("HSHSHSHS");
            System.out.println(s);
            Message msg = Message.toMessage(s);
            System.out.println(msg.data);

            for(int i = 0; i< msg.data.size()-1; i++){
                String[] splitted = msg.data.get(i).split(":");
                System.out.println(splitted.toString());
                ranks.addElement(new Pair<String, Integer>(splitted[0],new Integer(splitted[1])));
            }

            App.match.ranks = ranks;
            System.out.println(App.match.ranks);
            System.out.println("ENDDDDd");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void done(){
        System.out.println("Done");
        App.match.ranks = ranks;
        try {
            this.callback.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
