package com.texttwist.client.pages;

import com.texttwist.client.constants.Palette;
import com.texttwist.client.pages.*;
import com.texttwist.client.ui.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by loke on 14/06/2017.
 */
public class Game extends Page {

    private TTContainer gamecontainer;
    private DefaultListModel<String> letters = new DefaultListModel<String>();
    private DefaultListModel<String> words = new DefaultListModel<String>();
    private DefaultListModel<Point> letterSpawningPoint = new DefaultListModel<Point>();

    public Game(JFrame window) throws IOException {
        super(window);
        createUIComponents();
        letters = fetchLetters();
        letterSpawningPoint = setLetterSpawningPoint();
        showLetters();
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

    private DefaultListModel<String> fetchLetters(){
        DefaultListModel l = new DefaultListModel<String>();
        l.addElement("C");
        l.addElement("A");
        l.addElement("E");
        l.addElement("P");
        l.addElement("C");
        l.addElement("I");
        l.addElement("L");
        l.addElement("S");

        return l;
    }

    private Callable<Object> getWords(){
        return null;
    }

    public boolean addWordToWordsList(String word) {
        words.addElement(word);
        return true;
    }

    private Point getRandomPosition(){
        if(letterSpawningPoint.size()>1) {
            int index = ThreadLocalRandom.current().nextInt(0, letterSpawningPoint.size() - 1);
            Point placeholder = letterSpawningPoint.get(index);
            letterSpawningPoint.remove(index);
            return placeholder;
        }
        return new Point(0,0);
    }

    public void showLetters(){
        for(int i = 0; i< letters.size(); i++){
            TTLetter letter = new TTLetter(
                    getRandomPosition(),
                    letters.get(i),
                    gamecontainer);
        }
        window.repaint();
        window.revalidate();
    }

    @Override
    public void createUIComponents() throws IOException {
        addLogo(root);
        gamecontainer = new TTContainer(
                null,
                new Dimension(1150,220),
                Palette.root_backgroundColor,
                -1,
                root);

        TTGameBox searchUserBar = new TTGameBox(
                new Point(150, 90),
                new Dimension(250, 40),
                "Word!",
                new DefaultListModel(),
                getWords(),
                gamecontainer);

        addFooter(root);
        addNext(footer,
                new Font(Palette.inputBox_font.getFontName(), Font.BOLD, 70),
                null,
                "Done!",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return new com.texttwist.client.pages.Menu(Page.window);
                    }
                });

        addTimer(footer,
                new Font(Palette.inputBox_font.getFontName(), Font.BOLD, 40),
                null,
                "00:00");
    }

}
