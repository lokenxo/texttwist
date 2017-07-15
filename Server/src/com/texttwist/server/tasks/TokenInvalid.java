package com.texttwist.server.tasks;

import com.texttwist.server.Server;
import models.Message;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 11/07/2017.
 * Description: Task: Token Invalid
 */
public class TokenInvalid implements Callable<Void> {
    private String sender;
    private ByteBuffer buffer;
    private SocketChannel channel;

    public TokenInvalid (String sender, SocketChannel channel, ByteBuffer buffer){
        this.sender=sender;
        this.buffer=buffer;
        this.channel=channel;
    }

    @Override
    public Void call()throws Exception {
        Server.logger.write("TOKEN INVALID: TOKEN USED BY "+ sender+ " IS NOT VALID");
        Message msg = new Message("TOKEN_NOT_VALID", "", null, new DefaultListModel<>());
        buffer.clear();
        byte[] byteMessage = msg.toString().getBytes();
        buffer = ByteBuffer.wrap(byteMessage);
        channel.write(buffer);
        return null;
    }

}