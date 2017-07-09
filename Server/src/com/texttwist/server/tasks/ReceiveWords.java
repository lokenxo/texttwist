package com.texttwist.server.tasks;

import com.texttwist.server.Server;
import com.texttwist.server.components.GameServer;
import com.texttwist.server.models.Match;
import constants.Config;
import models.Message;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.texttwist.server.components.GameServer.activeMatches;

/**
 * Created by loke on 27/06/2017.
 */
public class ReceiveWords implements Callable<Boolean>{

    protected ExecutorService threadPool = Executors.newCachedThreadPool();

    public DatagramChannel channel;
    ByteBuffer buffer;


    public ReceiveWords(DatagramChannel channel, ByteBuffer buffer) {
        this.buffer = buffer;
        this.channel = channel;
    }

    @Override
    public Boolean call() throws Exception {


        Message msg;
        DatagramSocket s = new DatagramSocket(Config.WordsReceiverServerPort);


        while(true) {
            byte[] buf = new byte[1024];
            System.out.println("RECEIVIN WORDS");

            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            s.receive(packet);

            System.out.println("WORDS RECEIVED");
            String rcv = new String(packet.getData());
            System.out.println(rcv);
            if (rcv.startsWith("MESSAGE")) {
                msg = Message.toMessage(rcv);
                System.out.println(msg.sender);
                Match match = Match.findMatchByPlayer(msg.sender);
                threadPool.submit(new ComputeScore(msg.sender, msg.data, match));
            }

        }


    }

}
