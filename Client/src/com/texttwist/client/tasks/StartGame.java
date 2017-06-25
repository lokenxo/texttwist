package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.Game;
import com.texttwist.client.pages.GameController;
import oracle.jrockit.jfr.JFR;

import javax.swing.*;

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

        while(!(this.game.gameController.letters.size() > 0)) {
            System.out.println(this.game.gameController.letters.size());
            this.game.gameController.letters = App.matchService.words;
        }
        System.out.println(this.game.gameController.letters);
        game.showLetters();
        if(this.game.gameController.letters.size()>0){
            this.game.timer.start();
        }

    }
}
