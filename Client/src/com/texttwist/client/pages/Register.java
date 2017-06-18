package com.texttwist.client.pages;

import com.texttwist.client.constants.Palette;
import com.texttwist.client.ui.*;
import models.Response;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Created by loke on 13/06/2017.
 */
public class Register extends Page {

    private TTContainer registerDataContainer;
    private RegisterController registerController;
    public Register(JFrame window) {
        super(window);
        createUIComponents();
        registerController = new RegisterController();
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
                        //TODO CHIAMA API PER LOGIN E SE TUTTO OKEY MANDA A PAGINA DEL MENU
                        Response res = registerController.register(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                        if (res.code == 200){
                            return new TTDialog("success", res.message,
                            new Callable() {
                                @Override
                                public Object call() throws Exception {
                                    return new Home(window);
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
                registerDataContainer);
        addFooter(root);
        addBack(footer,
            new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return new Home(Page.window);
                }
        });

    }
}
