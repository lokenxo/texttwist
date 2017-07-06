package com.texttwist.client.tasks;

import com.texttwist.client.App;
import constants.Config;
import javafx.util.Pair;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

/**
 * Created by loke on 29/06/2017.
 */
public class SendWords extends SwingWorker<Void,Void> {

    DefaultListModel<Pair<String,Integer>> ranks = new DefaultListModel<Pair<String,Integer>>();

    SwingWorker callback;
    DefaultListModel<String> words = new DefaultListModel<>();

    //TODO PASSARE LA CALLBACK ALLO SWING WORKER ED ESEGUIRLA AL DONE
    public SendWords(DefaultListModel<String> words, SwingWorker callback){
        this.callback = callback;
        this.words = words;
    }

    @Override
    public Void doInBackground() {
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();

            InetAddress IPAddress = InetAddress.getByName(Config.WordsReceiverServerURI);
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            Message msg = new Message("WORDS", App.session.account.userName, "", words);
            String sentence = msg.toString();
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Config.WordsReceiverServerPort);
            clientSocket.send(sendPacket);
            //clientSocket.close();

            return null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void done(){
        System.out.println("Done send SCOREEEEEE");
        try {
            this.callback.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
