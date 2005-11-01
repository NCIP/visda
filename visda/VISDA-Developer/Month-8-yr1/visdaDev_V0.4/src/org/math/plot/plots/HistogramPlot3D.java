package org.math.plot.plots;

import java.awt.*;

import org.math.plot.render.*;

public class HistogramPlot3D extends Plot {

    double[][] topNW;

    double[][] topNE;

    double[][] topSW;

    double[][] topSE;

    double[][] bottomNW;

    double[][] bottomNE;

    double[][] bottomSW;

    double[][] bottomSE;

    double[][] widths;

    double[][] XY;

    public HistogramPlot3D(double[][] _XY, double[][] w, Color c, String n) {
        super(n, c);
        XY = _XY;
        widths = w;

        // int minIndex = Array.minIndex(XY)[0];
        // double[] datasMin = Array.getRowCopy(XY, minIndex);
        // double[] widthsMin = widths[minIndex];
        // int maxIndex = Array.maxIndex(XY)[0];
        // double[] datasMax = Array.getRowCopy(XY, maxIndex);
        // double[] widthsMax = widths[maxIndex];
        // double[] min = { datasMin[0] - widthsMin[0] / 2, datasMin[1] -
        // widthsMin[1] / 2, 0 };
        // double[] max = { datasMax[0] + widthsMax[0] / 2, datasMax[1] +
        // widthsMax[1] / 2, 0 };
        // base.includeInBounds(min);
        // base.includeInBounds(max);

        topNW = new double[XY.length][];
        topNE = new double[XY.length][];
        topSW = new double[XY.length][];
        topSE = new double[XY.length][];
        bottomNW = new double[XY.length][];
        bottomNE = new double[XY.length][];
        bottomSW = new double[XY.length][];
        bottomSE = new double[XY.length][];
        for (int i = 0; i < XY.length; i++) {
            topNW[i] = new double[] { XY[i][0] - widths[i][0] / 2, XY[i][1] + widths[i][1] / 2, XY[i][2] };
            topNE[i] = new double[] { XY[i][0] + widths[i][0] / 2, XY[i][1] + widths[i][1] / 2, XY[i][2] };
            topSW[i] = new double[] { XY[i][0] - widths[i][0] / 2, XY[i][1] - widths[i][1] / 2, XY[i][2] };
            topSE[i] = new double[] { XY[i][0] + widths[i][0] / 2, XY[i][1] - widths[i][1] / 2, XY[i][2] };
            bottomNW[i] = new double[] { XY[i][0] - widths[i][0] / 2, XY[i][1] + widths[i][1] / 2, 0 };
            bottomNE[i] = new double[] { XY[i][0] + widths[i][0] / 2, XY[i][1] + widths[i][1] / 2, 0 };
            bottomSW[i] = new double[] { XY[i][0] - widths[i][0] / 2, XY[i][1] - widths[i][1] / 2, 0 };
            bottomSE[i] = new double[] { XY[i][0] + widths[i][0] / 2, XY[i][1] - widths[i][1] / 2, 0 };
        }
    }

    public void plot(AbstractDrawer draw, Color c) {
        if (!visible) {
            return;
        }
        draw.setColor(c);
        for (int i = 0; i < XY.length; i++) {
            draw.drawLine(topNW[i], topNE[i]);
            draw.drawLine(topNE[i], topSE[i]);
            draw.drawLine(topSE[i], topSW[i]);
            draw.drawLine(topSW[i], topNW[i]);

            draw.drawLine(bottomNW[i], bottomNE[i]);
            draw.drawLine(bottomNE[i], bottomSE[i]);
            draw.drawLine(bottomSE[i], bottomSW[i]);
            draw.drawLine(bottomSW[i], bottomNW[i]);

            draw.drawLine(bottomNW[i], topNW[i]);
            draw.drawLine(bottomNE[i], topNE[i]);
            draw.drawLine(bottomSE[i], topSE[i]);
            draw.drawLine(bottomSW[i], topSW[i]);
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

    public void setDataWidth(double[][] w) {
        widths = w;
    }

    public double[][] getDataWidth() {
        return widths;
    }

    public void setData(double[][] d, double[][] w) {
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
