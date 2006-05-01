package org.math.plot.plots;

import java.awt.*;
import java.util.*;

import org.math.plot.canvas.*;
import org.math.plot.plotObjects.*;
import org.math.plot.render.*;
import org.math.plot.utils.*;

public abstract class Plot implements Plotable, Noteable, Editable {

    // protected String type;

    protected String name;

    protected Color color;

    public boolean visible = true;

    public Vector<QuantilePlot> quantiles;

    public boolean noted = false;

    public boolean forcenoted = false;

    public int note_precision = 5;

    public Plot(String n, Color c) {
        name = n;
        color = c;
        quantiles = new Vector<QuantilePlot>(0);
    }

    public void addQuantile(QuantilePlot q) {
        quantiles.add(q);
    }

    public void addQuantile(int a, double[] q) {
        quantiles.add(new QuantilePlot(this, a, q));
    }

    public void addQuantile(int a, double q) {
        quantiles.add(new QuantilePlot(this, a, q));
    }

    public void addQuantiles(double[][][] q) {
        for (int i = 0; i < q[0].length; i++) {
            addQuantile(i, Array.getColumnCopy(q, i, 0));
            addQuantile(i, Array.getColumnCopy(q, i, 1));
        }
    }

    public void addQuantiles(double[][] q) {
        for (int i = 0; i < q[0].length; i++) {
            addQuantile(i, Array.getColumnCopy(q, i));
        }
    }

    public abstract void setData(double[][] d);

    public abstract double[][] getData();

    public void setVisible(boolean v) {
        visible = v;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    /*
     * public String getType() { return type; }
     */

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        color = c;
    }

    public abstract double[] isSelected(int[] screenCoordTest, AbstractDrawer draw);

    public void note(AbstractDrawer draw) {
        plot(draw, PlotCanvas.NOTE_COLOR);
        plotQuantiles(draw, PlotCanvas.NOTE_COLOR);
    }

    protected abstract void plot(AbstractDrawer draw, Color c);

    public void plot(AbstractDrawer draw) {
        if (noted)
            note(draw);
        else {
            plot(draw, color);
            plotQuantiles(draw, color);
        }
    }

    protected void plotQuantiles(AbstractDrawer draw, Color c) {
        for (int i = 0; i < quantiles.size(); i++) {
            quantiles.get(i).plot(draw, c);
        }
    }

    public void edit(Object src) {
        ((PlotCanvas) src).displayDatasFrame(((PlotCanvas) src).getPlotIndex(this));
    }

    public void editnote(AbstractDrawer draw) {
        plot(draw, PlotCanvas.EDIT_COLOR);
        plotQuantiles(draw, PlotCanvas.EDIT_COLOR);
    }
}