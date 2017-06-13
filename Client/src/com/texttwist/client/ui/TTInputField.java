package com.texttwist.client.ui;

import com.texttwist.client.constants.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by loke on 13/06/2017.
 */
public class TTInputField extends TTInputBox{

    public TTInputField(Point position, Dimension dimension, String placeholder, JPanel parent) {
        super();

        setBackground(Palette.inputBox_backgroundColor);
        setFont(Palette.inputBox_font);
        setBounds(position.x, position.y, dimension.width, dimension.height);
        setPreferredSize(dimension);
        setForeground(Palette.fontColor);
        setPlaceholder(placeholder);

        parent.add(this);

    }


}
