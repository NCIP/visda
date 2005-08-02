package org.math.plot.plots;

import java.awt.*;

import org.math.plot.*;
import org.math.plot.render.*;

public class ScatterPlot extends Plot {

    private int type;

    private int radius;

    private boolean[][] pattern;

    private boolean use_pattern;

    double[][] XY;

    public ScatterPlot(String n, Color c, boolean[][] _pattern, double[][] _XY) {
        super(n, c);
        XY = _XY;
        use_pattern = true;
        pattern = _pattern;
    }

    public ScatterPlot(String n, Color c, int _type, int _radius, double[][] _XY) {
        super(n, c);
        XY = _XY;
        use_pattern = false;
        type = _type;
        radius = _radius;
    }

    public ScatterPlot(String n, Color c, double[][] _XY) {
        this(n, c, AbstractDrawer.ROUND, AbstractDrawer.DEFAULT_DOT_RADIUS, _XY);
    }

    public void plot(AbstractDrawer draw, Color c) {
        if (!visible)
            return;

        draw.setColor(c);
        if (use_pattern) {
            draw.setDotType(AbstractDrawer.PATTERN);
            draw.setDotPattern(pattern);
        } else {
            draw.setDotRadius(radius);
            if (type == AbstractDrawer.CROSS)
                draw.setDotType(AbstractDrawer.CROSS);
            else
                draw.setDotType(AbstractDrawer.ROUND);
        }

        for (int i = 0; i < XY.length; i++)
            draw.drawDot(XY[i]);
    }

    public void setDotPattern(int t) {
        type = t;
        use_pattern = false;
    }

    public void setDotPattern(boolean[][] t) {
        use_pattern = true;
        pattern = t;
    }

    @Override
    public void setData(double[][] d) {
        XY = d;
    }

    @Override
    public double[][] getData() {
        return XY;
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

    /**
     * @param args
     */
    public static void main(String[] args) {
        // double[][] XYZ = new double[][]
        // {{.1,.2,.3},{.4,.5,.6},{.7,.8,.9},{1.0,1.1,1.2},{1.3,1.4,1.5},{1.6,1.7,1.8}};

        Plot3DPanel p = new Plot3DPanel();
        for (int i = 0; i < 10; i++) {
            double[][] XYZ = new double[100][3];
            for (int j = 0; j < XYZ.length; j++) {
                XYZ[j][0] = 1 + Math.random();
                XYZ[j][1] = 100 * Math.random();
                XYZ[j][2] = 0.0001 * Math.random();
            }
            p.addScatterPlot("toto" + i, XYZ);
        }
        p.addQuantiletoPlot(0, 0, 0.5);

        p.setLegendOrientation(PlotPanel.SOUTH);
        new FrameView(p);
    }

}