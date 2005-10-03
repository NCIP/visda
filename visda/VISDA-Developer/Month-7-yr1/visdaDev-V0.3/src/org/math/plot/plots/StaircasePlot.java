package org.math.plot.plots;

import java.awt.*;

import org.math.plot.render.*;

public class StaircasePlot extends ScatterPlot {

    public boolean link = true;

    public StaircasePlot(String n, Color c, boolean[][] _pattern, double[][] _XY) {
        super(n, c, _pattern, _XY);
    }

    public StaircasePlot(String n, Color c, int _type, int _radius, double[][] _XY) {
        super(n, c, _type, _radius, _XY);
    }

    public StaircasePlot(String n, Color c, double[][] _XY) {
        super(n, c, _XY);
    }

    public void plot(AbstractDrawer draw, Color c) {
        if (!visible)
            return;

        draw.setColor(c);
        for (int i = 0; i < XY.length - 1; i++) {
            double[] begin = XY[i];
            double[] end = XY[i + 1];
            end[end.length - 1] = XY[i][end.length - 1];
            draw.drawLine(begin, end);
        }

        if (link) {
            for (int i = 1; i < XY.length - 1; i++) {
                double[] begin = XY[i];
                double[] end = XY[i + 1];
                begin[begin.length - 1] = XY[i - 1][begin.length - 1];
                draw.drawLine(begin, end);
            }
        }
    }
}