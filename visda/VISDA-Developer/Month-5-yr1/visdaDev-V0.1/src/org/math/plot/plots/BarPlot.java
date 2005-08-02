package org.math.plot.plots;

import java.awt.*;

import org.math.plot.render.*;
import org.math.plot.utils.*;

public class BarPlot extends ScatterPlot {

    public BarPlot(String n, Color c, boolean[][] _pattern, double[][] _XY) {
        super(n, c, _pattern, _XY);
    }

    public BarPlot(String n, Color c, int _type, int _radius, double[][] _XY) {
        super(n, c, _type, _radius, _XY);
    }

    public BarPlot(String n, Color c, double[][] _XY) {
        super(n, c, _XY);
    }

    public void plot(AbstractDrawer draw, Color c) {
        if (!visible)
            return;

        super.plot(draw, c);

        draw.setColor(c);
        for (int i = 0; i < XY.length; i++) {
            double[] axeprojection = Array.copy(XY[i]);
            axeprojection[axeprojection.length - 1] = draw.canvas.base.baseCoords[0][axeprojection.length - 1];
            draw.drawLine(XY[i], axeprojection);
        }
    }
}