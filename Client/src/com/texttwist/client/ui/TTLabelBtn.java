package com.texttwist.client.ui;

import constants.Palette;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;

/**
 * Author:      Lorenzo Iovino on 14/06/2017.
 * Description: TTLabelBtn component
 */
public class TTLabelBtn extends TTLabel{

    public TTLabelBtn(Point position, Dimension dimension, String caption, Font font, Color fontColor, Callable<Object> clickHandler, JPanel parent) {
        super(position, dimension, caption, font, fontColor, parent);

        setForeground(Palette.registerLblBtn_color);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                setForeground(Palette.registerLblBtn_onmouseclick_color);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseClicked(e);
                setForeground(Palette.registerLblBtn_color);
                try {
                    clickHandler.call();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseClicked(e);
                setForeground(Palette.registerLblBtn_onmouseover_color);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseClicked(e);
                setForeground(Palette.registerLblBtn_color);
            }

        });
    }
}
