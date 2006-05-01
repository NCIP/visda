package org.math.plot.plotObjects;

import java.awt.*;

import org.math.plot.*;
import org.math.plot.canvas.*;
import org.math.plot.render.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */

public class BaseLabel extends Label /* implements BaseDependant */{

    public BaseLabel(String l, double[] rC, Color c) {
        super(l, rC, c);
    }

    /*
     * public void resetBase() { System.out.println("BaseLabel.resetBase"); }
     */

    public static void main(String[] args) {
        Plot3DCanvas p3d = new Plot3DCanvas(new double[] { 0, 0, 0 }, new double[] { 10, 10, 10 }, new int[3], new String[] { "x", "y", "z" });
        new FrameView(p3d);
        // p3d.addPlot(DoubleArray.random(10, 3), "plot", "SCATTER");
        p3d.addPlotable(new BaseLabel("label", new double[] { -0.1, 0.5, 0.5 }, Color.RED));
    }

    public void plot(AbstractDrawer draw) {
        draw.setColor(color);
        draw.setFont(font);
        draw.drawStringRatio(label, coord, angle, cornerE, cornerN);
    }

}
