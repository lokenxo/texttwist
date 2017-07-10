package com.texttwist.client.controllers;
import com.texttwist.client.App;
import com.texttwist.client.pages.GamePage;
import com.texttwist.client.tasks.SendWords;
import com.texttwist.client.tasks.StartGame;
import com.texttwist.client.tasks.WaitForPlayers;
import com.texttwist.client.tasks.WaitForScore;
import javax.swing.*;
import java.nio.ByteBuffer;

/**
 * GamePage Controller
 */
public class GameController {

    private GamePage game;

    public GameController(GamePage game){
        this.game = game;
    }

    public DefaultListModel<String> getLetters(){
        return App.game.letters;
    }

    public DefaultListModel<String> getWords() {
        return App.game.words;
    }

    public SwingWorker waitForPlayers(SwingWorker callback) {
        return new WaitForPlayers(callback);
    }

    private SwingWorker waitForScore(SwingWorker callback){
        return new WaitForScore(callback);
    }

    public SwingWorker sendWords(SwingWorker callback){
        return new SendWords(App.game.words, waitForScore(callback));
    }

    public SwingWorker startGame() {
        return new StartGame(App.game.letters, game);
    }
}
