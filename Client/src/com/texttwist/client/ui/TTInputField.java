package com.texttwist.client.ui;

import constants.Palette;
import javax.swing.*;
import java.awt.*;

/**
 * Author:      Lorenzo Iovino on 13/06/2017.
 * Description: TTInputField component
 */
public class TTInputField extends TTInputBox{

    public TTInputField(Point position, Dimension dimension, String placeholder, JPanel parent) {
        super();

        setBackground(Palette.inputBox_backgroundColor);
        setFont(Palette.textFont);
        setBounds(position.x, position.y, dimension.width, dimension.height);
        setPreferredSize(dimension);
        setForeground(Palette.fontColor);
        setPlaceholder(placeholder);
        parent.add(this);
    }
}
