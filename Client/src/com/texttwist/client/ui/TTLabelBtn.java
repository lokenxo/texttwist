package com.texttwist.client.ui;

import com.texttwist.client.constants.Palette;
import com.texttwist.client.pages.Entrypoint;
import com.texttwist.client.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;

/**
 * Created by loke on 13/06/2017.
 */
public class TTLabelBtn extends TTLabel{

    public TTLabelBtn(Point position, Dimension dimension, String caption, Font font, Color fontColor, Callable<Page> clickHandler, JPanel parent) {
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
