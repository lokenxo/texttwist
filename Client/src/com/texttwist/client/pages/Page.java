package com.texttwist.client.pages;

import constants.Palette;
import com.texttwist.client.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Created by loke on 13/06/2017.
 */
public class Page {

    public TTContainer root;
    public TTContainer footer;
    public static JFrame window;

    public Page(JFrame window){
        this.window = window;

        window.getContentPane().removeAll();
        window.repaint();
        window.revalidate();

        root = new TTContainer(
                new Point(40,20),
                new Dimension(0,0),
                Palette.root_backgroundColor,
                BoxLayout.Y_AXIS,
                null);
        window.add(root);
    }

    public void createUIComponents() throws Exception {}

    public void addLogo(TTContainer parent) {
        TTContainer container = new TTContainer(
                null,
                new Dimension(1150, 150),
                Palette.root_backgroundColor,
                -1,
                parent);

        try {
            TTImage logoImg = new TTImage(
                    new Point(0, 10),
                    new Dimension(1150, 150),
                    new ImageIcon(new File("./Client/resources/images/logo.png").getCanonicalPath()),
                    container);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBack(TTContainer parent, Callable<Object> clickHandler) {
        try {
            TTImageBtn back = new TTImageBtn(
                    new Point(0, 0),
                    new Dimension(50, 50),
                    new ImageIcon(new File("./Client/resources/images/back.png").getCanonicalPath()),
                    clickHandler,
                    parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFooter(TTContainer root) {
        footer = new TTContainer(
                null,
                new Dimension(1150, 60),
                Palette.root_backgroundColor,
                -1,
                root);
    }

    public void addNext(TTContainer parent, Font font, Color fontColor, String caption, Callable<Object> clickHandler) {
        TTLabelBtn next = new TTLabelBtn(
                new Point(500, 0),
                new Dimension(150, 50),
                caption,
                null,
                null,
                clickHandler,
                parent);
    }

    public Timer addTimer(TTContainer parent, Font font, Color fontColor, String caption, Callable<Object> timerEndHandler, Integer value) {
        TTLabel lblTimer = new TTLabel(
                new Point(0, 0),
                new Dimension(150, 50),
                caption,
                font,
                fontColor,
                parent);

        Timer timer = new Timer(1000, new ActionListener() {
            private int count = value;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count <= 0) {
                    lblTimer.setText("00:00");
                    try {
                        timerEndHandler.call();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    ((Timer)e.getSource()).stop();
               } else {
                    int minutes = count / 60;
                    int seconds = count % 60;
                    String str = String.format("%d:%02d", minutes, seconds);
                    lblTimer.setText(str);
                    count--;

                }
            }
        });
        return timer;
    }

}
