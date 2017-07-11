package com.texttwist.server.tasks;

import com.texttwist.server.Server;
import models.Message;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by loke on 11/07/2017.
 */
public class TokenInvalid implements Callable<Boolean> {
    private String sender;
    private ByteBuffer buffer;
    private SocketChannel channel;

    public TokenInvalid (String sender, SocketChannel channel, ByteBuffer buffer){
        this.sender=sender;
        this.buffer=buffer;
        this.channel=channel;
    }

    @Override
    public Boolean call()throws Exception {
        System.out.print("TOKEN NON VALIDO");
        buffer = ByteBuffer.allocate(1024);
        Message msg = new Message("MATCH_NOT_AVAILABLE", "", null, new DefaultListModel<>());
        buffer.clear();
        byte[] byteMessage = msg.toString().getBytes();
        buffer = ByteBuffer.wrap(byteMessage);
        channel.write(buffer);
        return false;
    }

}