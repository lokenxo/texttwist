package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.Game;
import com.texttwist.client.pages.GameController;
import com.texttwist.client.pages.Page;
import com.texttwist.client.ui.TTDialog;
import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 25/06/2017.
 */
public class StartGame implements Runnable {

    public Game game;
    public StartGame(Game game){
        this.game = game;

    }

    @Override
    public void run(){

        //Letters are ready? Polling
        while(!(this.game.gameController.letters.size() > 0)) {
            this.game.gameController.letters = App.matchService.words;
        }

        //Mostra pannello di conferma che le lettere sono tutte arrivate
        new TTDialog("success", "Game is ready. Press OK to start!",
            new Callable() {
                @Override
                public Object call() throws Exception {
                    game.showLetters();
                    game.timer.start();
                    return null;
                }
            }, null);
    }
}
