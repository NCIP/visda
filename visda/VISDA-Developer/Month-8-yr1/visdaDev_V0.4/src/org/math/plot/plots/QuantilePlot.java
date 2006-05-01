package org.math.plot.plots;

import java.awt.*;

import org.math.plot.render.*;
import org.math.plot.utils.*;

/**
 * <p>
 * Titre :
 * </p>
 * <p>
 * Description :
 * </p>
 * <p>
 * Copyright : BSD License
 * </p>
 * <p>
 * Soci�t� :
 * </p>
 * 
 * @author Yann RICHET
 * @version 1.0
 */

public class QuantilePlot extends Plot {

    public static int RADIUS = 5;

    // public int[][] shape = { { 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 1 }, { 1, 0, 0,
    // 0, 1 }, { 1, 0, 0, 0, 1 }, { 1, 1, 1, 1, 1 } };

    Plot plot;

    int axe;

    double quantileRate;

    double main_data_constant = 0;

    public boolean symetric = true;

    double[] Q;

    public QuantilePlot(Plot p, int a, double[] q) {
        this(p, a, q, 0.5);
    }

    public QuantilePlot(Plot p, int a, double q, double r) {
        this(p, a, null, r);
        main_data_constant = q;
    }

    public QuantilePlot(Plot p, int a, double q) {
        this(p, a, q, 0.5);
    }

    public QuantilePlot(Plot p, int a, double[] q, double r) {
        super(r + " quantile of " + p.name, p.color);
        if (q != null)
            Array.checkLength(q, p.getData().length);
        Q = q;
        plot = p;
        axe = a;
        quantileRate = r;
    }

    public double getQuantilesValue(int numCoord) {
        return Q[numCoord];
    }

    public int getAxe() {
        return axe;
    }

    public double getQuantileRate() {
        return quantileRate;
    }

    public void plot(AbstractDrawer draw, Color c) {
        if (!plot.visible)
            return;

        draw.setColor(c);

        if (main_data_constant == 0)
            for (int i = 0; i < plot.getData().length; i++) {
                double[] d = Array.getRowCopy(plot.getData(), i);
                d[axe] += Q[i];
                draw.drawLine(plot.getData()[i], d);
                // draw.drawDot(d, RADIUS/*(int)(RADIUS*quantileRate)*/);

                if (symetric) {
                    d[axe] -= 2 * Q[i];
                    draw.drawLine(plot.getData()[i], d);
                    // draw.drawDot(d, RADIUS/*(int)(RADIUS*quantileRate)*/);
                }
            }
        else
            for (int i = 0; i < plot.getData().length; i++) {
                double[] d = Array.getRowCopy(plot.getData(), i);
                d[axe] += main_data_constant;
                draw.drawLine(plot.getData()[i], d);
                // draw.drawDot(d, shape/*RADIUS/*(int)(RADIUS*quantileRate)*/);

                if (symetric) {
                    d[axe] -= 2 * main_data_constant;
                    draw.drawLine(plot.getData()[i], d);
                    // draw.drawDot(d, RADIUS/*(int)(RADIUS*quantileRate)*/);
                }
            }

    }

    @Override
    public void setData(double[][] d) {
        Q = d[0];
    }

    @Override
    public double[][] getData() {
        return new double[][] { Q };
    }

    public double[] isSelected(int[] screenCoordTest, AbstractDrawer draw) {
        /*
         * for (int i = 0; i < plot.getData().length; i++) { double[] pC =
         * plot.getData()[i]; pC[axe] = pC[axe] + Q[i]; int[] screenCoord =
         * draw.project(pC);
         * 
         * if ((screenCoord[0] + note_precision > screenCoordTest[0]) &&
         * (screenCoord[0] - note_precision < screenCoordTest[0]) &&
         * (screenCoord[1] + note_precision > screenCoordTest[1]) &&
         * (screenCoord[1] - note_precision < screenCoordTest[1])) return pC; }
         */
        return null;
    }
}
