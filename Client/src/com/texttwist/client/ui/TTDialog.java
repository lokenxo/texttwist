package com.texttwist.client.ui;

import com.texttwist.client.App;
import constants.Palette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 17/06/2017.
 */
public class TTDialog extends JFrame {

    TTContainer root;
    public TTDialog(String type, String message, Callable okHandler, Callable cancelHandler) {
        setPreferredSize( new Dimension( 450, 200 ));
        setSize(new Dimension(450,200));
        setLocation(App.getWindowsPosition().x+100,App.getWindowsPosition().y+150);
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setContentPane(new ShadowPane());

        root = new TTContainer(
                new Point(40,20),
                new Dimension(0,0),
                Palette.root_backgroundColor,
                -1,
                null);
        switch (type){
            case "alert":{
                root.setBorder(BorderFactory.createLineBorder(Palette.dialog_alert));

            }
            case "success": {
                root.setBorder(BorderFactory.createLineBorder(Palette.dialog_success));
            }
        }
        add(root);

        TTLabel msg = new TTLabel(
                new Point(60,20),
                new Dimension(350,50),
                message,
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 38),
                null,
                root);

        if(okHandler != null && cancelHandler != null){
            TTButton okBtn = new TTButton(
                    new Point(60,100),
                    new Dimension(150,50),
                    "Ok",
                    new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            okHandler.call();
                            dispose();
                            return null;
                        }
                    },
                    root);
            TTButton cancelBtn = new TTButton(
                    new Point(250,100),
                    new Dimension(150,50),
                    "Cancel",
                    new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            cancelHandler.call();
                            dispose();
                            return null;
                        }
                    },
                    root);
        } else {
            if(cancelHandler != null) {
                TTButton cancelBtn = new TTButton(
                        new Point(150, 100),
                        new Dimension(150, 50),
                        "Cancel",
                        new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                cancelHandler.call();
                                dispose();
                                return null;
                            }
                        },
                        root);
            }
            if(okHandler != null) {
                TTButton okBtn = new TTButton(
                        new Point(150,100),
                        new Dimension(150,50),
                        "Ok",
                        new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                okHandler.call();
                                dispose();
                                return null;
                            }
                        },
                        root);
            }
        }

        setVisible(true);
    }

    public class ShadowPane extends JPanel {

        public ShadowPane() {
            setLayout(new BorderLayout());
            setOpaque(false);
            setBackground(Color.BLACK);
            setBorder(new EmptyBorder(0, 0, 5, 5));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            g2d.fillRect(5, 5, getWidth(), getHeight());
            g2d.dispose();
        }
    }

}
