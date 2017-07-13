package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.MenuPage;
import com.texttwist.client.pages.Page;
import com.texttwist.client.ui.TTDialog;
import models.Message;
import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 25/06/2017.
 * Description: Task: WaitForPlayers.
 *              Wait for players joins, when all joined then sends a message of game started to all
 *
 */
public class WaitForPlayers extends SwingWorker<Void,Void> {

    private boolean joinTimeout = false;
    private Callable<Void> callback;

    public WaitForPlayers(Callable<Void> callback) {
        this.callback = callback;
    }

    @Override
    public Void doInBackground() {
        try {
            App.gameService.isWaiting = true;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            TTDialog loading = new TTDialog("alert", "Waiting for users joins",null,null);
            buffer.flip();

            while (App.clientTCP.read(buffer) != -1) {
                String line = new String(buffer.array(), buffer.position(), buffer.remaining());
                buffer.clear();

                if (line.startsWith("MESSAGE")) {
                    buffer.clear();

                    Message msg = Message.toMessage(line);
                    if (msg.message.equals("JOIN_TIMEOUT")) {
                        loading.dispose();
                        App.gameService.isWaiting = false;
                        joinTimeout = true;

                        new TTDialog("alert", "TIMEOUT!",
                            new Callable() {
                                @Override
                                public Void call() throws Exception {
                                    new MenuPage(Page.window);
                                    return null;
                                }
                            }, null);
                        return null;
                    }

                    if (msg.message.equals("MATCH_NOT_AVAILABLE")) {
                        loading.dispose();
                        joinTimeout = true;
                        App.gameService.isWaiting = false;

                        new TTDialog("alert", "THE GAME IS NOT MORE AVAILABLE!",
                            new Callable() {
                                @Override
                                public Void call() throws Exception {
                                    new MenuPage(Page.window);
                                    return null;
                                }
                            }, null);
                        return null;
                    }

                    if (msg.message.equals("GAME_STARTED")) {
                        loading.dispose();
                        App.gameService.isWaiting = false;

                        if(msg.data !=null ) {
                            DefaultListModel<String> data = msg.data;
                            App.openClientMulticastSocket(Integer.valueOf(data.remove(data.size()-2)));
                            App.gameService.setLetters(msg.data);
                            break;
                        }
                    }
                    buffer = ByteBuffer.allocate(1024);
                }
            }
        } catch (IOException e) {
            App.logger.write("WAIT FOR SCORE: Can't receive from socket");
        }
        return null;
    }

    @Override
    public void done(){
        if(!joinTimeout) {
            try {
                this.callback.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
