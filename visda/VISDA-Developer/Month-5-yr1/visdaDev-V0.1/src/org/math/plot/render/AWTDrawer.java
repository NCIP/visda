/*
 * Created on 31 mai 2005 by richet
 */
package org.math.plot.render;

import java.awt.*;
import java.awt.font.*;

import org.math.plot.canvas.*;

public abstract class AWTDrawer extends AbstractDrawer {

    //protected Projection projection;  JJW change 'protected' to 'public'
	public Projection projection;
	
    public AWTDrawer(PlotCanvas _canvas) {
        super(_canvas);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#resetProjection()
     */
    public void resetBaseProjection() {
        projection.initBaseCoordsProjection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#setColor(java.awt.Color)
     */
    public void setColor(Color c) {
        comp2D.setColor(c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#setFont(java.awt.Font)
     */
    public void setFont(Font f) {
        comp2D.setFont(f);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#getColor()
     */
    public Color getColor() {
        return comp2D.getColor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#getFont()
     */
    public Font getFont() {
        return comp2D.getFont();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#project(double[])
     */
    public int[] project(double[] pC) {
        return projection.screenProjection(pC);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#projectRatio(double[])
     */
    public int[] projectRatio(double[] rC) {
        return projection.screenProjectionBaseRatio(rC);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#translate(int[])
     */
    public void translate(int[] t) {
        projection.translate(t);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#dilate(int[], double[])
     */
    public void dilate(int[] screenOrigin, double[] screenRatio) {
        projection.dilate(screenOrigin, screenRatio);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#drawString(java.lang.String,
     *      double[], double, double, double)
     */
    public void drawString(String label, double[] pC, double angle, double cornerE, double cornerN) {
        int[] sC = projection.screenProjection(pC);

        // Corner offset adjustment
        FontRenderContext frc = comp2D.getFontRenderContext();
        Font font1 = comp2D.getFont();
        int x = sC[0];
        int y = sC[1];
        double w = font1.getStringBounds(label, frc).getWidth();
        double h = font1.getSize2D();
        x -= (int) (w * cornerE);
        y += (int) (h * cornerN);

        if (angle != 0)
            comp2D.rotate(angle, x + w / 2, y - h / 2);

        comp2D.drawString(label, x, y);

        if (angle != 0)
            comp2D.rotate(-angle, x + w / 2, y - h / 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#drawStringRatio(java.lang.String,
     *      double[], double, double, double)
     */
    public void drawStringRatio(String label, double[] rC, double angle, double cornerE, double cornerN) {
        int[] sC = projection.screenProjectionBaseRatio(rC);

        // Corner offset adjustment
        FontRenderContext frc = comp2D.getFontRenderContext();
        Font font1 = comp2D.getFont();
        int x = sC[0];
        int y = sC[1];
        double w = font1.getStringBounds(label, frc).getWidth();
        double h = font1.getSize2D();
        x -= (int) (w * cornerE);
        y += (int) (h * cornerN);

        if (angle != 0)
            comp2D.rotate(angle, x + w / 2, y - h / 2);

        comp2D.drawString(label, x, y);

        if (angle != 0)
            comp2D.rotate(-angle, x + w / 2, y - h / 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#drawLineRatio(double[],
     *      double[])
     */
    public void drawLineRatio(double[] rC1, double[] rC2) {
        int[] sC1 = projection.screenProjectionBaseRatio(rC1);
        int[] sC2 = projection.screenProjectionBaseRatio(rC2);
        comp2D.drawLine(sC1[0], sC1[1], sC2[0], sC2[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#drawLine(double[], double[])
     */
    public void drawLine(double[] pC1, double[] pC2) {
        int[] sC1 = projection.screenProjection(pC1);
        int[] sC2 = projection.screenProjection(pC2);
        comp2D.drawLine(sC1[0], sC1[1], sC2[0], sC2[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#drawLargeLine(double[],
     *      double[])
     */
    public void drawLargeLine(double[] pC1, double[] pC2) {
        int[] sC1 = projection.screenProjection(pC1);
        int[] sC2 = projection.screenProjection(pC2);
        comp2D.drawLine(sC1[0] + 1, sC1[1], sC2[0] + 1, sC2[1]);
        comp2D.drawLine(sC1[0] - 1, sC1[1], sC2[0] - 1, sC2[1]);
        comp2D.drawLine(sC1[0], sC1[1] + 1, sC2[0], sC2[1] + 1);
        comp2D.drawLine(sC1[0], sC1[1] - 1, sC2[0], sC2[1] - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#drawDot(double[])
     */
    public void drawDot(double[] pC) {
        int[] sC = projection.screenProjection(pC);
        switch (dot_type) {
        case ROUND:
            comp2D.fillOval(sC[0] - dot_radius, sC[1] - dot_radius, 2 * dot_radius, 2 * dot_radius);
            break;
        case CROSS:
            comp2D.drawLine(sC[0] - dot_radius, sC[1] - dot_radius, sC[0] + dot_radius, sC[1] + dot_radius);
            comp2D.drawLine(sC[0] + dot_radius, sC[1] - dot_radius, sC[0] - dot_radius, sC[1] + dot_radius);
            break;
        case PATTERN:
            int yoffset = dot_pattern.length / 2;
            int xoffset = dot_pattern[0].length / 2;
            for (int i = 0; i < dot_pattern.length; i++)
                for (int j = 0; j < dot_pattern[i].length; j++)
                    if (dot_pattern[i][j])
                        // comp2D.setColor(new Color(getColor())
                        comp2D.fillRect(sC[0] - xoffset + j, sC[1] - yoffset + i, 1, 1);
            break;

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#drawPloygon(double[][])
     */
    public void drawPloygon(double[][] pC) {
        int[][] c = new int[pC.length][2];
        for (int i = 0; i < pC.length; i++)
            c[i] = projection.screenProjection(pC[i]);

        int[] x = new int[c.length];
        for (int i = 0; i < c.length; i++)
            x[i] = c[i][0];
        int[] y = new int[c.length];
        for (int i = 0; i < c.length; i++)
            y[i] = c[i][1];
        comp2D.drawPolygon(x, y, c.length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.math.plot.render.AbstractDrawer#fillPloygon(double[][])
     */
    public void fillPloygon(double[][] pC) {
        int[][] c = new int[pC.length][2];
        for (int i = 0; i < pC.length; i++)
            c[i] = projection.screenProjection(pC[i]);

        int[] x = new int[c.length];
        for (int i = 0; i < c.length; i++)
            x[i] = c[i][0];
        int[] y = new int[c.length];
        for (int i = 0; i < c.length; i++)
            y[i] = c[i][1];
        comp2D.fillPolygon(x, y, c.length);
    }

    public void drawImage(Image im, double[][] pC1, double[][] pC2) {
        // TODO
    }
    
}
