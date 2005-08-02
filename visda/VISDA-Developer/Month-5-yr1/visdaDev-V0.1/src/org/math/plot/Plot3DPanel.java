package org.math.plot;

import org.math.plot.canvas.*;
import org.math.plot.plotObjects.*;
import org.math.plot.utils.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */

/** class for ascending compatibility */
public class Plot3DPanel extends PlotPanel {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public Plot3DPanel() {
        super(new Plot3DCanvas());
    }

    public Plot3DPanel(Base b, BasePlot bp) {
        super(new Plot3DCanvas(b, bp));
    }

    public Plot3DPanel(double[] min, double[] max, int[] axesScales, String[] axesLabels) {
        super(new Plot3DCanvas(min, max, axesScales, axesLabels));
    }

    public Plot3DPanel(PlotCanvas _canvas, String legendOrientation) {
        super(_canvas, legendOrientation);
    }

    public Plot3DPanel(PlotCanvas _canvas) {
        super(_canvas);
    }

    public int addScatterPlot(String name, double[]... XY) {
        return ((Plot3DCanvas) plotCanvas).addScatterPlot(name, XY);
    }

    public int addLinePlot(String name, double[]... XY) {
        return ((Plot3DCanvas) plotCanvas).addLinePlot(name, XY);
    }

    public int addBarPlot(String name, double[]... XY) {
        return ((Plot3DCanvas) plotCanvas).addBarPlot(name, XY);
    }

    public int addBoxPlot(String name, double[][] XY, double[][] dX) {
        return ((Plot3DCanvas) plotCanvas).addBoxPlot(name, XY, dX);
    }

    public int addBoxPlot(String name, double[][] XYdX) {
        return ((Plot3DCanvas) plotCanvas).addBoxPlot(name, Array.getColumnsRangeCopy(XYdX, 0, 2), Array.getColumnsRangeCopy(XYdX, 3, 5));
    }

    public int addHistogramPlot(String name, double[][] XY, double[][] dX) {
        return ((Plot3DCanvas) plotCanvas).addHistogramPlot(name, XY, dX);
    }

    public int addHistogramPlot(String name, double[][] XYdX) {
        return ((Plot3DCanvas) plotCanvas).addHistogramPlot(name, Array.getColumnsRangeCopy(XYdX, 0, 2), Array.getColumnsRangeCopy(XYdX, 3, 4));
    }

    public int addGridPlot(String name, double[] X, double[] Y, double[][] Z) {
        return ((Plot3DCanvas) plotCanvas).addGridPlot(name, X, Y, Z);
    }

    public int addGridPlot(String name, double[][] XYZMatrix) {
        return ((Plot3DCanvas) plotCanvas).addGridPlot(name, XYZMatrix);
    }

    @Override
    public int addPlot(String type, String name, double[][] XY) {
        if (type.equalsIgnoreCase(SCATTER)) {
            return addScatterPlot(name, XY);
        } else if (type.equalsIgnoreCase(LINE)) {
            return addLinePlot(name, XY);
        } else if (type.equalsIgnoreCase(BAR)) {
            return addBarPlot(name, XY);
        } else if (type.equalsIgnoreCase(HISTOGRAM)) {
            return addHistogramPlot(name, XY);
        } else if (type.equalsIgnoreCase(BOX)) {
            return addBoxPlot(name, XY);
        } else if (type.equalsIgnoreCase(GRID)) {
            return addGridPlot(name, XY);
        } else {
            throw new IllegalArgumentException("Plot type is unknown : " + type);
        }
    }

}
