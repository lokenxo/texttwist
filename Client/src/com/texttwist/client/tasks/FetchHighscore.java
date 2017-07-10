package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.HighscoresPage;
import javafx.util.Pair;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Job: FetchHighscore
 */
public class FetchHighscore extends SwingWorker<Void,Void> {

    private DefaultListModel<Pair<String,Integer>> globalRanks = new DefaultListModel<>();
    private SocketChannel socketChannel;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    HighscoresPage highscoresPage;

    public FetchHighscore(HighscoresPage highscoresPage){
        this.socketChannel = App.game.clientSocket;
        this.highscoresPage = highscoresPage;
    }

    @Override
    public Void doInBackground() {
        Message message = new Message("FETCH_HIGHSCORES", App.session.account.userName, App.session.token, new DefaultListModel<>());
        buffer = ByteBuffer.allocate(1024);
        byte[] byteMessage = message.toString().getBytes();
        buffer = ByteBuffer.wrap(byteMessage);
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            buffer = ByteBuffer.allocate(1024);

            while (socketChannel.read(buffer) != -1) {

                String line = new String(buffer.array(), buffer.position(), buffer.remaining());

                if (line.startsWith("MESSAGE")) {
                    Message msg = Message.toMessage(line);
                    //MODIFICARE QUI. IL BUG SI VERIFICA ANCHE CON 2 CLIENT, INVIANDO IL GIOCO A UN CLIENT CHE STA SULLA PAGNA DI HIGHSCORES
                    if (msg.message.equals("HIGHSCORES") && msg.data != null) {

                        for(int i = 0; i< msg.data.size()-1; i++){
                            String[] splitted = msg.data.get(i).split(":");
                            globalRanks.addElement(new Pair<>(splitted[0],new Integer(splitted[1])));
                        }
                        buffer.clear();

                        return null;

                    }
                }
                buffer.clear();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void done(){
        App.game.globalRanks = globalRanks;
        this.highscoresPage.showHighscoreList();
    }
}

