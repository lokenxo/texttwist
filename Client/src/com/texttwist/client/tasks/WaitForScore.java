package com.texttwist.client.tasks;

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

            Message msg;
            while(true) {
                byte[] buf = new byte[1024];
                System.out.println("SONO QUIIIII");
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                System.out.println("ANCORA QUII");
                App.game.multicastSocket.receive(recv);

                String s = new String(recv.getData());
                System.out.println("HSHSHSHS");
                System.out.println(s);
                msg = Message.toMessage(s);
                if(msg.message.equals("ASD")){
                    break;
                }
            }

            if(msg.data != null) {
                for (int i = 0; i < msg.data.size() - 1; i++) {
                    String[] splitted = msg.data.get(i).split(":");
                    System.out.println(splitted.toString());
                    ranks.addElement(new Pair<String, Integer>(splitted[0], new Integer(splitted[1])));
                }
            }
            App.game.ranks = ranks;
            App.game.multicastSocket.leaveGroup(InetAddress.getByName(Config.ScoreMulticastServerURI));

            System.out.println(App.game.ranks);
            System.out.println("ENDDDDd");

            App.game.multicastSocket.disconnect();
            App.game.multicastSocket.close();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void done(){
        System.out.println("Done ranks");
        App.game.ranks = ranks;
           // App.game.multicastSocket.close();
            //App.game.clientSocket.close();

        try {
            this.callback.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
