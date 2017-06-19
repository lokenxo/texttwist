package com.texttwist.client.ui;

import constants.Palette;

import javax.swing.*;
import java.awt.*;

/**
 * Created by loke on 14/06/2017.
 */
public class TTLetter extends TTLabel{
    public TTLetter(Point position, String caption, JPanel parent) {
        super(position,
                new Dimension(50,50),
                caption,
                new Font(Palette.inputBox_font.getFontName(), Font.ITALIC, 40),
                Palette.fontColor,
                parent);
        parent.add(this);

    }

}
