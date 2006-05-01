package org.math.plot.plotObjects;

import java.awt.*;

import org.math.plot.render.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */
public class Line implements Plotable {

    protected double[][] extrem = new double[2][];

    protected Color color;

    boolean visible = true;

    public Line(double[] c1, double[] c2, Color col) {
        extrem[0] = c1;
        extrem[1] = c2;
        color = col;
    }

    public void setColor(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public boolean getVisible() {
        return visible;
    }

    public void plot(AbstractDrawer draw) {
        if (!visible)
            return;

        draw.setColor(color);
        draw.drawLine(extrem[0], extrem[1]);
    }
}
