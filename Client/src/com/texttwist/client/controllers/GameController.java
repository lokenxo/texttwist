package com.texttwist.client.controllers;

import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
import com.texttwist.client.tasks.SendWords;
import com.texttwist.client.tasks.WaitForPlayers;
import com.texttwist.client.tasks.WaitForScore;

import javax.swing.*;
import java.util.concurrent.Callable;

import static com.texttwist.client.App.gameService;

/**
 * Author:      Lorenzo Iovino on 27/06/2017.
 * Description: Controller of the Game Page
 */
public class GameController {

    private GamePage gamePage;

    private SwingWorker waitForScore(SwingWorker callback){
        return new WaitForScore(callback);
    }

    private SwingWorker sendWords(SwingWorker callback){
        return new SendWords(gameService.words, waitForScore(callback));
    }

    public GameController(GamePage gamePage){
        this.gamePage = gamePage;
    }

    public SwingWorker waitForPlayers(Callable<Void> callback) {
        return new WaitForPlayers(callback);
    }

    public Callable<Void> startGame = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            App.gameService.start();
            gamePage.showGameIsReadyAlert();
            return null;
        }
    };

    public DefaultListModel<String> getLetters() {
        return gameService.getLetters();
    }

    public SwingWorker timeIsOver(SwingWorker callback) {
        return sendWords(callback);
    }

    public DefaultListModel<String> getWords(){
        return gameService.getWords();
    }
}
