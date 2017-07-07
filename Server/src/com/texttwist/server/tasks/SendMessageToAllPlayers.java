package com.texttwist.server.tasks;

import com.texttwist.server.models.Match;
import models.Message;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by loke on 27/06/2017.
 */
public class SendMessageToAllPlayers  implements Callable<Boolean> {


    public final Match match;
    public final Message message;
    public SocketChannel socketChannel;
    public SendMessageToAllPlayers(Match match, Message message, SocketChannel socketChannel){
        this.match = match;
        this.message = message;
        this.socketChannel = socketChannel;
    }

    @Override
    public Boolean call() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        if(!match.isStarted()) {
            for (int i = 0; i < match.playersSocket.size(); i++) {
                socketChannel = match.playersSocket.get(i).getValue();
                if (socketChannel != null) {
                    buffer.clear();
                    byte[] byteMessage = message.toString().getBytes();
                    buffer = ByteBuffer.wrap(byteMessage);
                    socketChannel.write(buffer);
                }

            }
            return false;

        } else {
            return true;
        }
    }
}
