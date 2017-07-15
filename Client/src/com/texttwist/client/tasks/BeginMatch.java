package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.MenuPage;
import com.texttwist.client.pages.Page;
import com.texttwist.client.ui.TTDialog;
import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 15/07/2017.
 * Description: Task: BeginMatch
 *
 */
public class BeginMatch extends SwingWorker<Void,Void> {

    private String userName;

    public BeginMatch(String userName) {
        this.userName = userName;
    }

    @Override
    public Void doInBackground() {
        //Add to pending invites list
        try {
            App.gameService.addToPendingList(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!App.gameService.gameIsStarted && !App.gameService.isWaiting) {
            //Show invite popup
            new TTDialog("success", "New invite from: " + userName + "!",
                    new Callable() {
                        @Override
                        public Object call() throws Exception {
                            new JoinMatch(userName).execute();
                            return null;
                        }
                    },
                    new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return new MenuPage(Page.window);
                        }
                    });
        }
        return null;
    }
}