/*
 * Created on 3 juin 2005 by richet
 */
package org.math.plot.plots;

import java.awt.*;

import org.math.plot.*;
import org.math.plot.render.*;

public class GridPlot3D extends Plot {

    double[] X;

    double[] Y;

    double[][] Z;

    private double[][] XYZ_list;

    public GridPlot3D(double[] _X, double[] _Y, double[][] _Z, Color c, String n) {
        super(n, c);
        X = _X;
        Y = _Y;
        Z = _Z;
        buildXYZ_list();
    }

    public void plot(AbstractDrawer draw, Color c) {
        if (!visible)
            return;

        draw.setColor(c);
        for (int i = 0; i < X.length; i++)
            for (int j = 0; j < Y.length - 1; j++)
                draw.drawLine(new double[] { X[i], Y[j], Z[j][i] }, new double[] { X[i], Y[j + 1], Z[j + 1][i] });

        for (int j = 0; j < Y.length; j++)
            for (int i = 0; i < X.length - 1; i++)
                draw.drawLine(new double[] { X[i], Y[j], Z[j][i] }, new double[] { X[i + 1], Y[j], Z[j][i + 1] });

    }

    private void buildXYZ_list() {
        XYZ_list = new double[X.length * Y.length][3];
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < Y.length; j++) {
                XYZ_list[i + (j) * X.length][0] = X[i];
                XYZ_list[i + (j) * X.length][1] = Y[j];
                XYZ_list[i + (j) * X.length][2] = Z[j][i];
            }
        }
    }

    @Override
    public void setData(double[][] _Z) {
        Z = _Z;
        buildXYZ_list();
    }

    @Override
    public double[][] getData() {
        return XYZ_list;
    }

    public void setDataZ(double[][] _Z) {
        Z = _Z;
        buildXYZ_list();
    }

    public double[][] getDataZ() {
        return Z;
    }

    public void setDataX(double[] _X) {
        X = _X;
        buildXYZ_list();
    }

    public double[] getDataX() {
        return X;
    }

    public void setDataY(double[] _Y) {
        Y = _Y;
        buildXYZ_list();
    }

    public double[] getDataY() {
        return Y;
    }

    public void setDataXYZ(double[] _X, double[] _Y, double[][] _Z) {
        X = _X;
        Y = _Y;
        Z = _Z;
        buildXYZ_list();
    }

    public double[] isSelected(int[] screenCoordTest, AbstractDrawer draw) {
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < Y.length; j++) {
                double[] XY = { X[i], Y[j], Z[j][i] };
                int[] screenCoord = draw.project(XY);

                if ((screenCoord[0] + note_precision > screenCoordTest[0]) && (screenCoord[0] - note_precision < screenCoordTest[0])
                        && (screenCoord[1] + note_precision > screenCoordTest[1]) && (screenCoord[1] - note_precision < screenCoordTest[1]))
                    return XY;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // double[][] XYZ = new double[][]
        // {{.1,.2,.3},{.4,.5,.6},{.7,.8,.9},{1.0,1.1,1.2},{1.3,1.4,1.5},{1.6,1.7,1.8}};
        int n = 100;
        int m = 200;
        Plot3DPanel p = new Plot3DPanel();
        double[] X = new double[n];
        double[] Y = new double[m];
        double[][] Z = new double[n][m];

        for (int i = 0; i < X.length; i++) {
            X[i] = i / (double) X.length;
            for (int j = 0; j < Y.length; j++) {
                Y[j] = j / (double) Y.length;
                Z[j][i] = Math.exp(X[i]) + Y[j];
            }
        }
        p.addGridPlot("toto", X, Y, Z);

        // p.addQuantiletoPlot(0, 2, 0.5);

        p.setLegendOrientation(PlotPanel.SOUTH);
        new FrameView(p);
    }
}
