package com.texttwist.client.pages;

import com.texttwist.client.constants.Palette;
import com.texttwist.client.ui.TTContainer;
import com.texttwist.client.ui.TTImage;
import com.texttwist.client.ui.TTImageBtn;
import com.texttwist.client.ui.TTLabel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by loke on 13/06/2017.
 */
public class Page {

    public TTContainer root;
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

    public void createUIComponents() throws IOException {}

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
                    new Point(0, 800),
                    new Dimension(0, 0),
                    new ImageIcon(new File("./Client/resources/images/back.png").getCanonicalPath()),
                    clickHandler,
                    parent);
            TTLabel registerText = new TTLabel(
                    new Point(55,775),
                    new Dimension(350,0),
                    "Back",
                    new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 24),
                    null,
                    parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
