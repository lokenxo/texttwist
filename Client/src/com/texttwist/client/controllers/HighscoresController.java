package com.texttwist.client.controllers;
import com.texttwist.client.services.HighscoresService;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 28/06/2017.
 */
public class HighscoresController {

    HighscoresService highscoresService = new HighscoresService();

    public HighscoresController(){

    }


    public void fetchHighscores(JFrame window){
        highscoresService.fetchHighscores(new Callable<String>() {
            @Override
            public String call() throws Exception {
                window.revalidate();
                window.repaint();
                System.out.println("ASDDD");
                return "";
            }
        });
    }

}
