package com.texttwist.client.ui;
import constants.Palette;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Callable;


/**
 * Created by loke on 14/06/2017.
 */
public class TTGameBox extends TTInputField{

    private DefaultListModel words = new DefaultListModel();
    public TTGameBox(Point position,
                       Dimension dimension,
                       String placeholer,
                       DefaultListModel listModel,
                       TTContainer parent){

        super(position, dimension, placeholer, parent);
        setBackground(Palette.scrollPanel_backgroundColor);
        setFont(Palette.inputBox_font);
        setBounds(position.x, position.y, dimension.width, dimension.height);
        setPreferredSize(dimension);
        setForeground(Palette.fontColor);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == 10){
                    try {
                        System.out.println(getText());
                        setText("");
                        listModel.addElement(getText());
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
