package com.texttwist.client.ui;

import constants.Palette;
import javax.swing.*;
import java.awt.*;


/**
 * Author:      Lorenzo Iovino on 13/06/2017.
 * Description: TTPasswordField component
 */
public class TTPasswordField extends JPasswordField{

    private String placeholder = "";

    public TTPasswordField(Point position, Dimension dimension, JPanel parent) {
        super();

        setBackground(Palette.inputBox_backgroundColor);
        setFont(Palette.inputBox_font);
        setBounds(position.x, position.y, dimension.width, dimension.height);
        setPreferredSize(dimension);
        setForeground(Palette.fontColor);
        setText(placeholder);
        parent.add(this);
    }
}
