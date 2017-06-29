package com.texttwist.client.controllers;
import com.texttwist.client.App;
import com.texttwist.client.pages.Game;
import com.texttwist.client.tasks.SendWords;
import com.texttwist.client.tasks.StartGame;
import com.texttwist.client.tasks.WaitForPlayers;
import com.texttwist.client.tasks.WaitForScore;
import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Game Controller
 */
public class GameController {

    private Game game;
    private Timer timer;

    public GameController(Game game){
        this.game = game;
        this.timer = game.timer;

    }

    public DefaultListModel<String> getLetters(){
        return App.match.letters;
    }

    public DefaultListModel<String> getWords() {
        return App.match.words;
    }

    public SwingWorker waitForPlayers(SwingWorker callback) {
        return new WaitForPlayers(callback);
    }

    private SwingWorker waitForScore(SwingWorker callback){
        return new WaitForScore(callback);
    }

    public SwingWorker sendWords(SwingWorker callback){
        return new SendWords(App.match.words, waitForScore(callback));
    }

    public SwingWorker startGame() {
        return new StartGame(App.match.letters, game);
    }
}
