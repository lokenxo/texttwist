package com.texttwist.client.ui;

import constants.Palette;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * Author:      Lorenzo Iovino on 14/06/2017.
 * Description: TTGameBox component
 */
public class TTGameBox extends TTInputField{

    public TTGameBox(Point position,
                       Dimension dimension,
                       String placeholder,
                       DefaultListModel<String> list,
                       TTContainer parent){

        super(position, dimension, placeholder, parent);
        setBackground(Palette.scrollPanel_backgroundColor);
        setFont(Palette.inputBox_font);
        setBounds(position.x, position.y, dimension.width, dimension.height);
        setPreferredSize(dimension);
        setForeground(Palette.fontColor);
        list.clear();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if(e.getKeyCode() == 10){
                try {
                    list.addElement(getText());
                    setText("");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            //Every time i press a key, execute a search of users
            }
        });

        parent.add(this);
    }
}
