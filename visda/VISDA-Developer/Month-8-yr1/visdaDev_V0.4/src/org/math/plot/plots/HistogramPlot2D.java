package org.math.plot.plots;

import java.awt.*;

import org.math.plot.render.*;

public class HistogramPlot2D extends Plot {

    double[][] topLeft;

    double[][] topRight;

    double[][] bottomLeft;

    double[][] bottomRight;

    double[] widths;

    double offsetCenter_perWidth;

    double factorWidth;

    boolean autowidth;

    double[][] XY;

    public HistogramPlot2D(double[][] _XY, double[] w, Color c, String n) {
        this(_XY, w, 0, 1, c, n);
    }

    // TODO Histogram group plots

    public HistogramPlot2D(double[][] _XY, double[] w, double _offsetCenter_perWidth, double _factorWidth, Color c, String n) {
        super(n, c);
        XY = _XY;
        widths = w;

        autowidth = false;
        widths = w;
        offsetCenter_perWidth = _offsetCenter_perWidth;
        factorWidth = _factorWidth;

        // int minIndex = Array.minIndex(XY)[0];
        // double[] datasMin = Array.getRowCopy(XY, minIndex);
        // double widthsMin = widths[minIndex];
        // int maxIndex = Array.maxIndex(XY)[0];
        // double[] datasMax = Array.getRowCopy(XY, maxIndex);
        // double widthsMax = widths[maxIndex];
        // double[] min = { datasMin[0] - widthsMin / 2, 0 };
        // double[] max = { datasMax[0] + widthsMax / 2, 0 };
        // base.includeInBounds(min);
        // base.includeInBounds(max);

        topLeft = new double[XY.length][];
        topRight = new double[XY.length][];
        bottomLeft = new double[XY.length][];
        bottomRight = new double[XY.length][];
        for (int i = 0; i < XY.length; i++) {
            topLeft[i] = new double[] { XY[i][0] - factorWidth * widths[i] / 2 + (offsetCenter_perWidth - 0.5) * widths[i], XY[i][1] };
            topRight[i] = new double[] { XY[i][0] + factorWidth * widths[i] / 2 + (offsetCenter_perWidth - 0.5) * widths[i], XY[i][1] };
            bottomLeft[i] = new double[] { XY[i][0] - factorWidth * widths[i] / 2 + (offsetCenter_perWidth - 0.5) * widths[i], 0 };
            bottomRight[i] = new double[] { XY[i][0] + factorWidth * widths[i] / 2 + (offsetCenter_perWidth - 0.5) * widths[i], 0 };
        }
    }

    /*
     * public HistogramPlot2D(double[][] XY, Color c, String n, ProjectionBase
     * b) { super(XY, c, n, PlotPanel.HISTOGRAM, b);
     * 
     * autowidth = true;
     * 
     * topLeft = new double[datas.length][]; topRight = new
     * double[datas.length][]; bottomLeft = new double[datas.length][];
     * bottomRight = new double[datas.length][];
     * 
     * Sorting sort = new Sorting(DoubleArray.getColumnCopy(datas, 0), false);
     * datas = DoubleArray.getRowsCopy(XY, sort.getIndex());
     * 
     * topLeft[0] = new double[] { datas[0][0] + (datas[0][0] - datas[1][0]) /
     * 2, datas[0][1] }; topRight[0] = new double[] { (datas[0][0] +
     * datas[1][0]) / 2, datas[0][1] }; bottomLeft[0] = new double[] {
     * datas[0][0] + (datas[0][0] - datas[1][0]) / 2, 0 }; bottomRight[0] = new
     * double[] { (datas[0][0] + datas[1][0]) / 2, 0 }; for (int i = 1; i <
     * datas.length - 1; i++) { topLeft[i] = new double[] { (datas[i][0] +
     * datas[i - 1][0]) / 2, datas[i][1] }; topRight[i] = new double[] {
     * (datas[i][0] + datas[i + 1][0]) / 2, datas[i][1] }; bottomLeft[i] = new
     * double[] { (datas[i][0] + datas[i - 1][0]) / 2, 0 }; bottomRight[i] = new
     * double[] { (datas[i][0] + datas[i + 1][0]) / 2, 0 }; }
     * topLeft[datas.length - 1] = new double[] { (datas[datas.length - 1][0] +
     * datas[datas.length - 2][0]) / 2, datas[datas.length - 1][1] };
     * topRight[datas.length - 1] = new double[] { datas[datas.length - 1][0] +
     * (datas[datas.length - 1][0] - datas[datas.length - 2][0]) / 2,
     * datas[datas.length - 1][1] }; bottomLeft[datas.length - 1] = new double[] {
     * (datas[datas.length - 1][0] + datas[datas.length - 2][0]) / 2, 0 };
     * bottomRight[datas.length - 1] = new double[] { datas[datas.length - 1][0] +
     * (datas[datas.length - 1][0] - datas[datas.length - 2][0]) / 2, 0 }; }
     */

    public void plot(AbstractDrawer draw, Color c) {
        if (!visible) {
            return;
        }
        draw.setColor(c);
        for (int i = 0; i < XY.length; i++) {
            draw.drawLine(bottomLeft[i], topLeft[i]);
            draw.drawLine(topLeft[i], topRight[i]);
            draw.drawLine(topRight[i], bottomRight[i]);
            draw.drawLine(bottomRight[i], bottomLeft[i]);
        }
    }

    @Override
    public void setData(double[][] d) {
        XY = d;
    }

    @Override
    public double[][] getData() {
        return XY;
    }

    public void setDataWidth(double[] w) {
        widths = w;
    }

    public double[] getDataWidth() {
        return widths;
    }

    public void setData(double[][] d, double[] w) {
        XY = d;
        widths = w;
    }

    public double[] isSelected(int[] screenCoordTest, AbstractDrawer draw) {
        for (int i = 0; i < XY.length; i++) {
            int[] screenCoord = draw.project(XY[i]);

            if ((screenCoord[0] + note_precision > screenCoordTest[0]) && (screenCoord[0] - note_precision < screenCoordTest[0])
                    && (screenCoord[1] + note_precision > screenCoordTest[1]) && (screenCoord[1] - note_precision < screenCoordTest[1]))
                return XY[i];
        }
        return null;
    }

}
