package org.math.plot.canvas;

import java.awt.event.*;

import org.math.plot.plotObjects.*;
import org.math.plot.plots.*;
import org.math.plot.render.*;

import static org.math.plot.utils.Array.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */
public class Plot3DCanvas extends PlotCanvas {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public final static int ROTATION = 2;

    public Plot3DCanvas() {
        super();
    }

    public Plot3DCanvas(Base b, BasePlot bp) {
        super(b, bp);
    }

    public Plot3DCanvas(double[] min, double[] max, int[] axesScales, String[] axesLabels) {
        super(min, max, axesScales, axesLabels);
    }

    public void initDrawer() {
        draw = new AWTDrawer3D(this);
    }

    public void initBasenGrid(double[] min, double[] max) {
        initBasenGrid(min, max, new int[] { Base.LINEAR, Base.LINEAR, Base.LINEAR }, new String[] { "X", "Y", "Z" });
    }

    public void initBasenGrid() {
        initBasenGrid(new double[] { 0, 0, 0 }, new double[] { 1, 1, 1 });
    }

    public void setAxesLabels(String Xlabel, String Ylabel, String Zlabel) {
        setAxesLabels(new String[] { Xlabel, Ylabel, Zlabel });
    }

    private static double[][] convertXY(double[][] XY) {
        if (XY.length == 3)
            return mergeColumns(XY[1], XY[2], XY[3]);
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

    public int addBoxPlot(String name, double[][] XY, double[][] dX) {
        return addPlot(new BoxPlot3D(XY, dX, COLORLIST[plots.size() % COLORLIST.length], name));
    }

    public int addBoxPlot(String name, double[][] XYdX) {
        return addPlot(new BoxPlot3D(getColumnsRangeCopy(XYdX, 0, 2), getColumnsRangeCopy(XYdX, 3, 5), COLORLIST[plots.size() % COLORLIST.length], name));
    }

    public int addHistogramPlot(String name, double[][] XY, double[][] dX) {
        return addPlot(new HistogramPlot3D(XY, dX, COLORLIST[plots.size() % COLORLIST.length], name));
    }

    public int addHistogramPlot(String name, double[][] XYdX) {
        return addPlot(new HistogramPlot3D(getColumnsRangeCopy(XYdX, 0, 2), getColumnsRangeCopy(XYdX, 3, 4), COLORLIST[plots.size() % COLORLIST.length], name));
    }

    public int addGridPlot(String name, double[] X, double[] Y, double[][] Z) {
        return addPlot(new GridPlot3D(X, Y, Z, COLORLIST[plots.size() % COLORLIST.length], name));
    }

    public int addGridPlot(String name, double[][] XYZMatrix) {
        double[] X = new double[XYZMatrix[0].length - 1];
        System.arraycopy(XYZMatrix[0], 1, X, 0, XYZMatrix[0].length - 1);
        double[] Y = new double[XYZMatrix.length - 1];
        for (int i = 0; i < Y.length; i++)
            Y[i] = XYZMatrix[i + 1][0];
        double[][] Z = getSubMatrixRangeCopy(XYZMatrix, 1, XYZMatrix.length - 1, 1, XYZMatrix[0].length - 1);

        return addGridPlot(name, X, Y, Z);
    }

    public void mouseDragged(MouseEvent e) {
        mouseCurent[0] = e.getX();
        mouseCurent[1] = e.getY();
        e.consume();
        int[] t;
        switch (ActionMode) {
        case TRANSLATION:
            t = new int[] { mouseCurent[0] - mouseClick[0], mouseCurent[1] - mouseClick[1] };
            draw.translate(t);
            mouseClick[0] = mouseCurent[0];
            mouseClick[1] = mouseCurent[1];
            break;
        case ROTATION:
            t = new int[] { mouseCurent[0] - mouseClick[0], mouseCurent[1] - mouseClick[1] };
            ((AWTDrawer3D) draw).rotate(t, panelSize);
            mouseClick[0] = mouseCurent[0];
            mouseClick[1] = mouseCurent[1];
            break;
        }
        repaint();
    }

}