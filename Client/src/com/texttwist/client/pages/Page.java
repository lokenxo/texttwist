package com.texttwist.client.pages;

import constants.Palette;
import com.texttwist.client.ui.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 14/06/2017.
 * Description: Controller of the Home Page
 */
public class Page {

    public TTContainer root;
    public TTContainer footer;
    public static JFrame window;

    public Page(JFrame window){
        Page.window = window;

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
            new TTImage(
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
            new TTImageBtn(
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

    public void addNext(TTContainer parent, String caption, Callable<Object> clickHandler) {
        new TTLabelBtn(
            new Point(500, 0),
            new Dimension(150, 50),
            caption, new Font(Palette.textFont.getFontName(), Font.ITALIC, 34),
            null,
            clickHandler,
            parent);
    }

    public Timer addTimer(TTContainer parent, Font font, SwingWorker timerEndHandler, Integer value) {
        TTLabel lblTimer = new TTLabel(
            new Point(0, 0),
            new Dimension(150, 50),
            "00:00",
            font,
            null,
            parent
        );

        return new Timer(1000, new ActionListener() {
            private int count = value;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count <= 0) {
                    lblTimer.setText("00:00");
                    try {
                        timerEndHandler.execute();
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
    }
}
