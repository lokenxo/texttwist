package com.texttwist.client.tasks;

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

        //Mostra pannello di conferma che le lettere sono tutte arrivate
        new TTDialog("success", "GamePage is ready. Press OK to start!",
            new Callable() {
                @Override
                public Object call() throws Exception {
                    gamePage.showLetters();
                    System.out.println(letters);
                    gamePage.timer.start();
                    return null;
                }
            }, null);
        return null;
    }

    @Override
    public void done(){
        System.out.println("Done start gamePage");
    }
}
