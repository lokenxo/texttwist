package com.texttwist.client.tasks;


import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
import com.texttwist.client.pages.Page;
import models.Message;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Author:      Lorenzo Iovino on 25/06/2017.
 * Description: Task: JoinMatch
 *
 */
public class JoinMatch extends SwingWorker<Void,Void> {

    private String matchName;

    public JoinMatch(String matchName) {
        this.matchName = matchName;
    }

    @Override
    public Void doInBackground() {
        //Clear pending invitation list and join selected match
        if(!App.gameService.gameIsStarted) {
            App.gameService.pendingList.clear();
            try {
                DefaultListModel<String> matchNames = new DefaultListModel<>();
                matchNames.addElement(matchName);
                Message message = new Message("JOIN_GAME", App.session.account.userName, App.session.token, matchNames);

                byte[] byteMessage = message.toString().getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(byteMessage);
                App.clientTCP.write(buffer);
                new GamePage(Page.window);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
