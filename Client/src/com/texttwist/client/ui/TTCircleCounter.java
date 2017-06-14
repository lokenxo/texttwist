package com.texttwist.client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

/**
 * Created by loke on 14/06/2017.
 */
public class TTCircleCounter extends JComponent{

    private Point position;
    private Dimension dimension;

    public TTCircleCounter(Point position, Dimension dimension, Graphics g, TTContainer parent){
        this.position=position;
        this.dimension=dimension;

        Graphics2D g2d = (Graphics2D)g;
        Ellipse2D.Double circle = new Ellipse2D.Double(position.x, position.y, dimension.width, dimension.height);
        g2d.fill(circle);
        g2d.setColor(Color.RED);

        parent.add(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        Ellipse2D.Double circle = new Ellipse2D.Double(position.x, position.y, dimension.width, dimension.height);
        g2d.setColor(Color.RED);
        g2d.fill(circle);
    }
}
