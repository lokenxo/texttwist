package com.texttwist.client.ui;

import constants.Palette;
import javax.swing.*;
import java.awt.*;


/**
 * Author:      Lorenzo Iovino on 14/06/2017.
 * Description: TTLetter component
 */
public class TTLetter extends TTLabel{
    public TTLetter(Point position, String caption, JPanel parent) {
        super(position,
            new Dimension(50,50),
            caption,
            new Font(Palette.textFont.getFontName(), Font.ITALIC, 30),
            Palette.fontColor,
            parent);
        parent.add(this);
    }
}
