package com.texttwist.client.pages;

import com.texttwist.client.tasks.StartGame;
import constants.Palette;
import com.texttwist.client.ui.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created by loke on 14/06/2017.
 */
public class Game extends Page {

    private TTContainer gameContainer;
    public GameController gameController;
    public TTGameBox gameBox;
    public DefaultListModel<String> words = new DefaultListModel<String>();
    public DefaultListModel<Point> letterSpawningPoint = new DefaultListModel<Point>();

    public Timer timer = null;

    public Point getRandomPosition(){
        if(letterSpawningPoint.size()>1) {
            int index = ThreadLocalRandom.current().nextInt(0, letterSpawningPoint.size() - 1);
            Point placeholder = letterSpawningPoint.get(index);
            letterSpawningPoint.remove(index);
            return placeholder;
        }
        return new Point(0,0);
    }


    public void showLetters(){
        for(int i = 0; i< this.gameController.letters.size(); i++){
            TTLetter letter = new TTLetter(
                    getRandomPosition(),
                    this.gameController.letters.get(i),
                    gameContainer);
        }

        window.repaint();
        window.revalidate();
    }

    public Game(JFrame window) throws IOException {
        super(window);
        createUIComponents();
        gameController = new GameController();
        letterSpawningPoint = setLetterSpawningPoint();
        this.gameController.waitForPlayers();
        this.gameController.startGame(this);
        window.setVisible(true);
    }

    private DefaultListModel<Point> setLetterSpawningPoint(){
        DefaultListModel l = new DefaultListModel<Point>();

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

    @Override
    public void createUIComponents() throws IOException {
        addLogo(root);
        gameContainer = new TTContainer(
                null,
                new Dimension(1150,220),
                Palette.root_backgroundColor,
                -1,
                root);

        gameBox = new TTGameBox(
                new Point(150, 90),
                new Dimension(250, 40),
                "Insert word and Press ENTER!",
                words,
                gameContainer);

        addFooter(root);
        addNext(footer,
                new Font(Palette.inputBox_font.getFontName(), Font.BOLD, 70),
                null,
                "Done!",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return null;
                    }
                });

        timer = addTimer(footer,
                new Font(Palette.inputBox_font.getFontName(), Font.BOLD, 40),
                null,
                "00:00",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        gameController.sendWords(words);
                        return null;
                    }},
                15);
    }

}
