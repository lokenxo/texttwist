package com.texttwist.client.pages;

import com.texttwist.client.controllers.GameController;
import constants.Config;
import constants.Palette;
import com.texttwist.client.ui.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Author:      Lorenzo Iovino on 27/06/2017.
 * Description: Game Page
 */
public class GamePage extends Page {

    private TTContainer gameContainer;
    private GameController gameController;
    public Timer timer;

    //All the spawning points
    private final DefaultListModel<Point> letterSpawningPoints = setLetterSpawningPoint();
    //Available spawning points
    private DefaultListModel<Point> availableLetterSpawningPoint = new DefaultListModel<>();

    public GamePage(JFrame window) throws IOException {
        super(window);
        gameController = new GameController(this);

        //Reset and set spawning point for letters
        availableLetterSpawningPoint.clear();
        availableLetterSpawningPoint = letterSpawningPoints;

        //Wait for players, then when all players joined start the game
        gameController.waitForPlayers(gameController.startGame).execute();

        createUIComponents();
        window.setVisible(true);
    }

    /*Occupy a random position of the available spawning points, if positions ends (can't happen) use position (0,0)*/
    private Point occupyRandomPosition(){
        if(availableLetterSpawningPoint.size() > 1) {
            int index = ThreadLocalRandom.current().nextInt(0, letterSpawningPoints.size() - 1);
            Point placeholder = letterSpawningPoints.get(index);
            letterSpawningPoints.remove(index);
            return placeholder;
        }

        return new Point(0,0);
    }

    private DefaultListModel<Point> setLetterSpawningPoint(){

        DefaultListModel<Point> l = new DefaultListModel<>();

        //FirstRow
        l.addElement(new Point(100,30));
        l.addElement(new Point(200,15));
        l.addElement(new Point(300,30));
        l.addElement(new Point(400,15));
        l.addElement(new Point(500,25));

        //SecondRow
        l.addElement(new Point(15,80));
        l.addElement(new Point(65,95));
        l.addElement(new Point(440,80));
        l.addElement(new Point(500,90));

        //ThirdRow
        l.addElement(new Point(50,140));
        l.addElement(new Point(150,130));
        l.addElement(new Point(250,125));
        l.addElement(new Point(350,145));
        l.addElement(new Point(450,140));
        l.addElement(new Point(550,130));

      return l;
    }

    public void showLetters(){

        /* Place letters in an available random spawning point */
        DefaultListModel<String> letters = gameController.getLetters();
        for(int i = 0; i < letters.size()-1; i++){
            new TTLetter(
                occupyRandomPosition(),
                letters.get(i),
                gameContainer
            );
        }

        window.repaint();
        window.revalidate();
    }

    public void showGameIsReadyAlert(){
        //Mostra pannello di conferma che le lettere sono tutte arrivate
        new TTDialog("success", "GameService is ready. Press OK to start!",
            new Callable() {
                @Override
                public Object call() throws Exception {
                    showLetters();
                    timer.start();
                    return null;
                }
            }, null);
    }

    @Override
    public void createUIComponents() throws IOException {

        addLogo(root);

        gameContainer = new TTContainer(
            null,
            new Dimension(1150,220),
            Palette.root_backgroundColor,
            -1,
            root
        );

        new TTGameBox(
            new Point(150, 90),
            new Dimension(250, 40),
            "Insert word and Press ENTER!",
            gameController.getWords(),
            gameContainer
        );

        addFooter(root);

        timer = addTimer(
            footer,
            new Font(Palette.inputBox_font.getFontName(), Font.BOLD, 40),
            gameController.timeIsOver(new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    new HighscoresPage(window,true);
                    return null;
                }
            }),
            Config.timeoutGame
        );
    }
}
