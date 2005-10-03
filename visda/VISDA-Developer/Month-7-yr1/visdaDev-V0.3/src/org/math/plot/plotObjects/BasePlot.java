package org.math.plot.plotObjects;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */

import java.awt.*;

import org.math.plot.render.*;

public class BasePlot implements /*Plotable,*/BaseDependant {

    public static Color DEFAULT_COLOR = Color.DARK_GRAY;

    protected Base base;

    protected Axe[] axes;

    protected boolean visible = true;

    protected Color color;

    public BasePlot(Base b, String[] as) {
        this(b, as, DEFAULT_COLOR);
    }

    public BasePlot(Base b, Axe[] a, Color c) {
        base = b;
        axes = a;
        color = c;
    }

    public BasePlot(Base b, String[] as, Color c) {
        base = b;
        if (as.length != base.dimension) {
            throw new IllegalArgumentException("String array of axes names must have " + base.dimension + " elements.");
        }
        color = c;
        axes = new Axe[base.dimension];
        for (int i = 0; i < base.dimension; i++) {
            axes[i] = new Axe(base, as[i], color, i);
        }
        // resetBase();
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public void setVisible(int i, boolean v) {
        axes[i].setVisible(v);
    }

    public void setGridVisible(int i, boolean v) {
        axes[i].setGridVisible(v);
    }

    public boolean getVisible() {
        return visible;
    }

    public void setColor(Color c) {
        color = c;
        for (int i = 0; i < axes.length; i++) {
            axes[i].setColor(c);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setLegend(String[] as) {
        if (as.length != base.dimension) {
            throw new IllegalArgumentException("String array of axes names must have " + base.dimension + " elements.");
        }
        for (int i = 0; i < axes.length; i++) {
            axes[i].setLegend(as[i]);
        }
        // resetBase();
    }

    public void setLegend(int i, String as) {
        axes[i].setLegend(as);
        // resetBase();
    }

    public String[] getLegend() {
        String[] array = new String[axes.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = axes[i].getLegend();
        }
        return array;
    }

    public String getLegend(int i) {
        return axes[i].getLegend();
    }

    public void setBase(Base b) {
        base = b;
        for (int i = 0; i < axes.length; i++) {
            axes[i].base = base;
        }
        resetBase();
    }

    public void plot(AbstractDrawer draw) {
        if (!visible)
            return;

        for (int i = 0; i < axes.length; i++)
            axes[i].plot(draw);
    }

    public Axe getAxe(int i) {
        return axes[i];
    }

    public Axe[] getAxes() {
        return axes;
    }

    public void resetBase() {
        // System.out.println("BasePlot.resetBase");
        for (int i = 0; i < axes.length; i++) {
            axes[i].resetBase();
        }
    }

}