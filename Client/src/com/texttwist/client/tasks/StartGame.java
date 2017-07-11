package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
import com.texttwist.client.ui.TTDialog;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 25/06/2017.
 */
public class StartGame extends SwingWorker<Void,Void> {


    /*Words inserted from user*/
    private DefaultListModel<String> letters = new DefaultListModel<>();
    private GamePage gamePage;

    public StartGame(DefaultListModel<String> letters, GamePage game){
        this.letters = letters;
        this.gamePage = game;

    }

    @Override
    public Void doInBackground(){
        App.game.gameIsStarted =true;
        //Mostra pannello di conferma che le lettere sono tutte arrivate
        new TTDialog("success", "Game is ready. Press OK to start!",
            new Callable() {
                @Override
                public Object call() throws Exception {
                    gamePage.showLetters();
                    gamePage.timer.start();
                    return null;
                }
            }, null);
        return null;
    }

    @Override
    public void done(){
    }
}
