package com.texttwist.server.tasks;

import com.texttwist.server.Server;
import com.texttwist.server.models.Match;
import constants.Config;
import models.Message;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by loke on 27/06/2017.
 */
public class ReceiveWords implements Callable<Boolean>{

    protected ExecutorService threadPool = Executors.newCachedThreadPool();

    public Boolean receiveWords = true;
    public Match match;
    public ReceiveWords(Match match) {
        this.match = match;
    }

    @Override
    public Boolean call() throws Exception {
        System.out.print("READY TO Receive words !!!!");

        DatagramSocket serverSocket = new DatagramSocket(Config.WordsReceiverServerPort);
        byte[] receiveData = new byte[1024];

        Future<Boolean> matchTimeout = threadPool.submit(new MatchTimeout(receiveWords));

        while(receiveWords)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String rcv = new String( receivePacket.getData());
            System.out.println("RECEIVED: " + rcv);
            Message msg = Message.toMessage(rcv);
            Future<Integer> computeScore = threadPool.submit(new ComputeScore(msg.sender, match, msg.data));

            //Se tutti hanno inviato le parole, blocca il timer e restituisci true
            computeScore.get();
            System.out.println(match.allPlayersSendedHisScore());
            if(match.allPlayersSendedHisScore()){
                //matchTimeout.cancel(true);
                return true;
            }

        }
        return false;
    }

}
