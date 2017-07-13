package com.texttwist.client.pages;

import com.texttwist.client.controllers.RegisterController;
import constants.Palette;
import com.texttwist.client.ui.*;
import models.Response;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 16/06/2017.
 * Description: Register Page
 */
public class RegisterPage extends Page {

    private RegisterController registerController;

    RegisterPage(JFrame window) {
        super(window);
        createUIComponents();
        registerController = new RegisterController();
        window.setVisible(true);
    }

    @Override
    public void createUIComponents() {
        addLogo(root);
        TTContainer registerDataContainer = new TTContainer(
            null,
            new Dimension(1150, 220),
            Palette.root_backgroundColor,
            -1,
            root
        );
        new TTLabel(
            new Point(70,35),
            new Dimension(400,40),
            "Insert your datas and press Register!",
            new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 12),
            null,
            registerDataContainer
        );

        TTInputField usernameField = new TTInputField(
            new Point(70,90),
            new Dimension(210,50),
            "Username",
            registerDataContainer
        );

        TTPasswordField passwordField = new TTPasswordField(
            new Point(290,90),
            new Dimension(210,50),
            registerDataContainer
        );

        new TTButton(
            new Point(70,150),
            new Dimension(430,50),
            "Register!",
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Response res = registerController.register(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                    if (res.code == 200){
                        return new TTDialog("success", res.message,
                        new Callable() {
                            @Override
                            public Object call() throws Exception {
                                return new HomePage(window);
                            }
                        },null);
                    } else {
                        return new TTDialog("alert", res.message,
                            new Callable() {
                            @Override
                            public Object call() throws Exception {
                                return null;
                            }
                        },null);
                    }
                }
            },
            registerDataContainer
        );

        addFooter(root);

        addBack(footer,
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return new HomePage(Page.window);
                }
            }
        );
    }
}
