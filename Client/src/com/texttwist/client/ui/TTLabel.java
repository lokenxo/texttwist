package com.texttwist.client.ui;

import constants.Palette;
import javax.swing.*;
import java.awt.*;

/**
 * Author:      Lorenzo Iovino on 13/06/2017.
 * Description: TTLabel component
 */
public class TTLabel extends JLabel{

    public TTLabel(Point position, Dimension dimension, String caption, Font font, Color fontColor, JPanel parent) {
        super();

        setBackground(Palette.inputBox_backgroundColor);

        if(font == null) {
            setFont(Palette.inputBox_font);
        } else {
            setFont(font);
        }

        setBounds(position.x,position.y,dimension.width, dimension.height);
        setPreferredSize(dimension);
        setText(caption);

        if(fontColor == null) {
            setForeground(Palette.fontColor);
        } else {
            setForeground(fontColor);
        }
        parent.add(this);
    }
}
