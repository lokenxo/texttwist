package com.texttwist.client.pages;

import com.texttwist.client.constants.Palette;
import com.texttwist.client.ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 13/06/2017.
 */
public class Register extends Page {

    private TTContainer registerDataContainer;
    public Register(JFrame window) {
        super(window);
        createUIComponents();
        window.setVisible(true);
    }

    @Override
    public void createUIComponents() {
        addLogo(root);
        registerDataContainer = new TTContainer(
                null,
                new Dimension(1150,220),
                Palette.root_backgroundColor,
                -1,
                root);
        TTLabel registerText = new TTLabel(
                new Point(70,35),
                new Dimension(400,40),
                "<html><h2>Insert your datas and press Register!</h2></html>",
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 12),
                null,
                registerDataContainer);
        TTInputField usernameField = new TTInputField(
                new Point(70,90),
                new Dimension(210,50),
                "Username",
                registerDataContainer);
        TTPasswordField passwordField = new TTPasswordField(
                new Point(290,90),
                new Dimension(210,50),
                registerDataContainer);
        TTButton register = new TTButton(
                new Point(70,150),
                new Dimension(430,50),
                "Register!",
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {

                        //TODO CHIAMA API PER REGISTRAZIONE E SE TUTTO OKEY MANDA A PAGINA LOGIN
                        return new Home(window);
                    }
                },
                registerDataContainer);
        addBack(root,
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return new Home(Page.window);
                }
        });

    }
}
