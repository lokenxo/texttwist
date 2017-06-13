package com.texttwist.client.pages;

import javax.swing.*;

/**
 * Created by loke on 13/06/2017.
 */
public class Register extends Page {

    public Register(JFrame window) {
        super(window);
        createUIComponents();
        window.setVisible(true);
    }

    @Override
    public void createUIComponents() {
        addLogo(root);

    }
}
