package com.texttwist.client.tasks;

import com.texttwist.client.App;
import models.Message;
import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 20/06/2017.
 * Description: Task: FetchHighscore.
 *              Ask for highscores to server. When received it shows in highscores list
 */
public class FetchHighscore extends SwingWorker<Void,Void> {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private Callable<Void> callback;

    public FetchHighscore(Callable<Void> callback){
        this.callback = callback;
    }

    @Override
    public Void doInBackground() {
        Message message = new Message("FETCH_HIGHSCORES", App.session.account.userName, App.session.token, new DefaultListModel<>());
        buffer = ByteBuffer.allocate(1024);
        byte[] byteMessage = message.toString().getBytes();
        buffer = ByteBuffer.wrap(byteMessage);

        try {
            App.clientTCP.write(buffer);
        } catch (IOException e) {
            App.logger.write("FETCH HIGHSCORES: Can't write on socket");
        }

        try {
            buffer = ByteBuffer.allocate(1024);
            while (App.clientTCP.read(buffer) != -1) {
                String line = new String(buffer.array(), buffer.position(), buffer.remaining());

                if (line.startsWith("MESSAGE")) {
                    Message msg = Message.toMessage(line);
                    if (msg.message.equals("HIGHSCORES") && msg.data != null) {
                        App.gameService.globalRanks = utilities.Parse.score(msg.data);
                        break;
                    }
                }
                buffer.clear();
            }
        } catch (IOException e) {
            App.logger.write("FETCH HIGHSCORES: Can't read from socket");
        }
        return null;
    }

    public void done(){
        try {
            callback.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
