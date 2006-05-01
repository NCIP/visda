package org.math.plot.plots;

import java.awt.*;

import org.math.plot.render.*;

public class LinePlot extends ScatterPlot {

    public LinePlot(String n, Color c, boolean[][] _pattern, double[][] _XY) {
        super(n, c, _pattern, _XY);
    }

    public LinePlot(String n, Color c, int _type, int _radius, double[][] _XY) {
        super(n, c, _type, _radius, _XY);
    }

    public LinePlot(String n, Color c, double[][] _XY) {
        super(n, c, _XY);
    }

    public void plot(AbstractDrawer draw, Color c) {
        if (!visible)
            return;

        super.plot(draw, c);

        draw.setColor(c);
        for (int i = 0; i < XY.length - 1; i++)
            draw.drawLine(XY[i], XY[i + 1]);
    }

}
