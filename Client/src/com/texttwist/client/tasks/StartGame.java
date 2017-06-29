package com.texttwist.client.tasks;

import com.texttwist.client.App;
import com.texttwist.client.pages.Game;
import com.texttwist.client.ui.TTDialog;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 25/06/2017.
 */
public class StartGame extends SwingWorker<Void,Void> {


    /*Words inserted from user*/
    private DefaultListModel<String> letters = new DefaultListModel<>();
    private Game game;

    public StartGame(DefaultListModel<String> letters, Game game){
        this.letters = letters;
        this.game = game;
    }

    @Override
    public Void doInBackground(){

        //Mostra pannello di conferma che le lettere sono tutte arrivate
        new TTDialog("success", "Game is ready. Press OK to start!",
            new Callable() {
                @Override
                public Object call() throws Exception {
                    game.showLetters();
                    System.out.println(letters);
                    game.timer.start();
                    return null;
                }
            }, null);
        return null;
    }

    @Override
    public void done(){
        System.out.println("Done start game");
    }
}
