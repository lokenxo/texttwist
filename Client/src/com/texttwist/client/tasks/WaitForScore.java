package com.texttwist.client.tasks;

import com.texttwist.client.App;
import constants.Config;

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


    @Override
    public Void doInBackground() {
        InetAddress group = null;
        try {

            MulticastSocket ms = new MulticastSocket(App.matchService.multicastId);
            InetAddress ia = InetAddress.getByName(Config.ScoreMulticastServerURI);
            ms.joinGroup(ia);
            System.out.println("Join multicast group");

            byte[] buf = new byte[1024];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            ms.receive(recv);
            String s = new String(recv.getData());
            System.out.println(s);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void done(){
        System.out.println("Done");
    }

}
