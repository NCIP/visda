package org.math.plot.canvas;

import org.math.plot.plotObjects.*;
import org.math.plot.plots.*;
import org.math.plot.render.*;

import static org.math.plot.utils.Array.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */
public class Plot2DCanvas extends PlotCanvas {

    // public final static String PARALLELHISTOGRAM = "PARALLELHISTOGRAM";

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public Plot2DCanvas() {
        super();
    }

    public Plot2DCanvas(Base b, BasePlot bp) {
        super(b, bp);
    }

    public Plot2DCanvas(double[] min, double[] max, int[] axesScales, String[] axesLabels) {
        super(min, max, axesScales, axesLabels);
    }

    public void initDrawer() {
        draw = new AWTDrawer2D(this);
    }

    public void initBasenGrid(double[] min, double[] max) {
        initBasenGrid(min, max, new int[] { Base.LINEAR, Base.LINEAR }, new String[] { "X", "Y" });
    }

    public void initBasenGrid() {
        initBasenGrid(new double[] { 0, 0 }, new double[] { 1, 1 });
    }

    public void setAxesLabels(String Xlabel, String Ylabel) {
        setAxesLabels(new String[] { Xlabel, Ylabel });
    }

    private static double[][] convertXY(double[][] XY) {
        if (XY.length == 2)
            return mergeColumns(XY[1], XY[2]);
        else
            return XY;
    }

    public int addScatterPlot(String name, double[]... XY) {
        convertXY(XY);
        return addPlot(new ScatterPlot(name, COLORLIST[plots.size() % COLORLIST.length], XY));
    }

    public int addLinePlot(String name, double[]... XY) {
        convertXY(XY);
        return addPlot(new LinePlot(name, COLORLIST[plots.size() % COLORLIST.length], XY));
    }

    public int addBarPlot(String name, double[]... XY) {
        convertXY(XY);
        return addPlot(new BarPlot(name, COLORLIST[plots.size() % COLORLIST.length], XY));
    }

    public int addStaircasePlot(String name, double[]... XY) {
        convertXY(XY);
        return addPlot(new StaircasePlot(name, COLORLIST[plots.size() % COLORLIST.length], XY));
    }

    public int addBoxPlot(String name, double[][] XY, double[][] dX) {
        return addPlot(new BoxPlot2D(XY, dX, COLORLIST[plots.size() % COLORLIST.length], name));
    }

    public int addBoxPlot(String name, double[][] XYdX) {
        return addPlot(new BoxPlot2D(getColumnsRangeCopy(XYdX, 0, 1), getColumnsRangeCopy(XYdX, 2, 3), COLORLIST[plots.size() % COLORLIST.length], name));
    }

    public int addHistogramPlot(String name, double[][] XY, double[] dX) {
        return addPlot(new HistogramPlot2D(XY, dX, COLORLIST[plots.size() % COLORLIST.length], name));
    }

    public int addHistogramPlot(String name, double[][] XYdX) {
        return addPlot(new HistogramPlot2D(getColumnsRangeCopy(XYdX, 0, 1), getColumnCopy(XYdX, 2), COLORLIST[plots.size() % COLORLIST.length], name));
    }

    public static void main(String[] args) {
        /*
         * Plot2DPanel p2d = new Plot2DPanel(DoubleArray.random(10, 2), "plot
         * 1", PlotPanel.SCATTER); new FrameView(p2d);
         * p2d.addPlot(DoubleArray.random(10, 2), "plot 2", PlotPanel.SCATTER);
         * p2d.grid.getAxe(0).darkLabel.setCorner(0.5, -10);
         * p2d.grid.getAxe(1).darkLabel.setCorner(0, -0.5);
         */
    }
}