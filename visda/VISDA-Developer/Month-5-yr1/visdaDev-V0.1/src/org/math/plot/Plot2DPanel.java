package org.math.plot;

import org.math.plot.canvas.*;
import org.math.plot.plotObjects.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */

/** class for ascending compatibility */
public class Plot2DPanel extends PlotPanel {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public Plot2DPanel() {
        super(new Plot2DCanvas());
    }

    public Plot2DPanel(Base b, BasePlot bp) {
        super(new Plot2DCanvas(b, bp));
    }

    public Plot2DPanel(double[] min, double[] max, int[] axesScales, String[] axesLabels) {
        super(new Plot2DCanvas(min, max, axesScales, axesLabels));
    }

    public Plot2DPanel(PlotCanvas _canvas, String legendOrientation) {
        super(_canvas, legendOrientation);
    }

    public Plot2DPanel(PlotCanvas _canvas) {
        super(_canvas);
    }

    public int addScatterPlot(String name, double[]... XY) {
        return ((Plot2DCanvas) plotCanvas).addScatterPlot(name, XY);
    }

    public int addLinePlot(String name, double[]... XY) {
        return ((Plot2DCanvas) plotCanvas).addLinePlot(name, XY);
    }

    public int addBarPlot(String name, double[]... XY) {
        return ((Plot2DCanvas) plotCanvas).addBarPlot(name, XY);
    }

    public int addStaircasePlot(String name, double[]... XY) {
        return ((Plot2DCanvas) plotCanvas).addStaircasePlot(name, XY);
    }

    public int addBoxPlot(String name, double[][] XY, double[][] dX) {
        return ((Plot2DCanvas) plotCanvas).addBoxPlot(name, XY, dX);
    }

    public int addBoxPlot(String name, double[][] XYdX) {
        return ((Plot2DCanvas) plotCanvas).addBoxPlot(name, XYdX);
    }

    public int addHistogramPlot(String name, double[][] XY, double[] dX) {
        return ((Plot2DCanvas) plotCanvas).addHistogramPlot(name, XY, dX);
    }

    public int addHistogramPlot(String name, double[][] XYdX) {
        return ((Plot2DCanvas) plotCanvas).addHistogramPlot(name, XYdX);
    }

    @Override
    public int addPlot(String type, String name, double[][] XY) {
        if (type.equalsIgnoreCase(SCATTER)) {
            return addScatterPlot(name, XY);
        } else if (type.equalsIgnoreCase(LINE)) {
            return addLinePlot(name, XY);
        } else if (type.equalsIgnoreCase(BAR)) {
            return addBarPlot(name, XY);
        } else if (type.equalsIgnoreCase(STAIRCASE)) {
            return addStaircasePlot(name, XY);
        } else if (type.equalsIgnoreCase(HISTOGRAM)) {
            return addHistogramPlot(name, XY);
        } else if (type.equalsIgnoreCase(BOX)) {
            return addBoxPlot(name, XY);
        } else {
            throw new IllegalArgumentException("Plot type is unknown : " + type);
        }
    }
}
