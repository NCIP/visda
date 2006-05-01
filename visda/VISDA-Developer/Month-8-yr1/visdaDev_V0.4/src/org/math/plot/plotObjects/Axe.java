package org.math.plot.plotObjects;

import java.awt.*;

import javax.swing.*;

import org.math.plot.canvas.*;
import org.math.plot.render.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 * 
 * Modified by Jiajing Wang for Changing the number of slices on the Axe (October 20, 2005)
 * 
 */

public class Axe implements Plotable, BaseDependant, Editable {

    public int linear_slicing = 10;

    public double label_offset = 0.05;

    public int note_precision = 5;

    protected int index;

    public Base base;

    boolean visible = true;

    public Color color;

    protected String name;

    boolean gridVisible = true;

    protected double[] linesSlicing;

    protected double[] labelsSlicing;

    public double[] origin;

    public double[] end;

    protected Line darkLine;

    protected Line[][] lightLines;

    protected Label darkLabel;

    protected Label[] lightLabels;

    protected Font lightLabelFont = AbstractDrawer.DEFAULT_FONT;

    protected Font darkLabelFont = AbstractDrawer.DEFAULT_FONT;

    protected double lightLabelAngle = 0;

    protected String[] lightLabelNames;

    public Axe(Base b, String aS, Color c, int i) {
        base = b;
        name = aS;
        index = i;
        color = c;
        init();
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public void setGridVisible(boolean v) {
        gridVisible = v;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setColor(Color c) {
        color = c;
        darkLabel.setColor(color);
    }

    public Color getColor() {
        return color;
    }

    public void setLegend(String n) {
        name = n;
        darkLabel.setText(name);
    }

    public String getLegend() {
        return name;
    }

    public double[] getLegendCoord() {
        return darkLabel.coord;
    }

    private void initOriginEnd() {
        origin = base.getCoords()[0];
        end = base.getCoords()[index + 1];

        // System.out.println("origin: "+Array.toString(origin));
        // System.out.println("end: "+Array.toString(end));
    }

    private void setSlicing() {

        // slicing initialisation
        if (base.getAxeScale(index) == Base.LOG) {
            int numPow10 = (int) Math.rint((Math.log(base.getMaxBounds()[index] / base.getMinBounds()[index]) / Math.log(10)));
            numPow10 = Math.max(numPow10, 1);
            double minPow10 = Math.rint(Math.log(base.getMinBounds()[index]) / Math.log(10));

            linesSlicing = new double[numPow10 * 9 + 1];
            labelsSlicing = new double[numPow10 + 1];

            // set slicing for labels : 0.1 , 1 , 10 , 100 , 1000
            for (int i = 0; i < labelsSlicing.length; i++) {
                labelsSlicing[i] = Math.pow(10, i + minPow10);
            }
            // set slicing for labels : 0.1 , 0.2 , ... , 0.9 , 1 , 2 , ... , 9
            // , 10 , 20 , ...
            for (int i = 0; i < numPow10; i++) {
                for (int j = 0; j < 10; j++) {
                    linesSlicing[i * 9 + j] = Math.pow(10, i + minPow10) * (j + 1);
                }
            }
        } else if (base.getAxeScale(index) == Base.LINEAR) {

            linesSlicing = new double[linear_slicing + 1];
            labelsSlicing = new double[linear_slicing + 1];

            double min = base.getMinBounds()[index];

            double pitch = (base.baseCoords[index + 1][index] - base.baseCoords[0][index]) / (linear_slicing);

            for (int i = 0; i < linear_slicing + 1; i++) {
                // lines and labels slicing are the same
                linesSlicing[i] = min + i * pitch;
                labelsSlicing[i] = min + i * pitch;
            }
        }

        // System.out.println("linesSlicing: "+Array.toString(linesSlicing));
        // System.out.println("labelsSlicing: "+Array.toString(labelsSlicing));
    }

    public void plot(AbstractDrawer draw) {
        if (!visible)
            return;

        if (gridVisible) {
            for (int i = 0; i < lightLines.length; i++)
                // j = 0 overwrites a darkLine of another Axe : so I begin to j
                // = 1.
                for (int j = 1; j < lightLines[i].length; j++)
                    lightLines[i][j].plot(draw);
            for (int i = 0; i < lightLabels.length; i++)
                lightLabels[i].plot(draw);
        }
        darkLine.plot(draw);
        darkLabel.plot(draw);
    }

    private void setLightLabels() {
        // System.out.println(" s setLightLabels");

        // offset of lightLabels
        double[] labelOffset = new double[base.dimension];
        for (int j = 0; j < base.dimension; j++)
            if (j != index)
                labelOffset[j] = -label_offset;

        // local variables initialisation
        int decimal = 0;
        String lab;

        lightLabels = new /* Base */Label[labelsSlicing.length];

        for (int i = 0; i < lightLabels.length; i++) {

            double[] labelCoord = new double[base.dimension];
            System.arraycopy(base.getCoords()[index + 1]/* labelOffset */, 0, labelCoord, 0, base.dimension);
            labelCoord[index] = labelsSlicing[i];

            if (base.getAxeScale(index) == Base.LINEAR)
                decimal = -(int) (log(base.getPrecisionUnit()[index] / 100) / log(10));
            else if (base.getAxeScale(index) == Base.LOG)
                decimal = -(int) (floor(log(labelsSlicing[i]) / log(10)));

            if (lightLabelNames != null)
                lab = lightLabelNames[i % lightLabelNames.length];
            else
                lab = new String(Label.approx(labelsSlicing[i]/* labelCoord[index] */, decimal) + "");

            // System.out.println(Array.toString(labelCoord) + " -> "+lab);

            lightLabels[i] = new /* Base */Label(lab, labelCoord, Color.lightGray);
            lightLabels[i].base_offset = labelOffset;

            if (lightLabelAngle != 0)
                lightLabels[i].rotate(lightLabelAngle);
            if (lightLabelFont != null)
                lightLabels[i].setFont(lightLabelFont);
        }

    }

    private void setLightLines() {
        // System.out.println(" s setLightLines");
        lightLines = new Line[base.dimension - 1][linesSlicing.length];

        int i2 = 0;

        for (int i = 0; i < base.dimension - 1; i++) {
            if (i2 == index)
                i2++;

            for (int j = 0; j < lightLines[i].length; j++) {
                double[] origin_tmp = new double[base.dimension];
                double[] end_tmp = new double[base.dimension];

                System.arraycopy(origin, 0, origin_tmp, 0, base.dimension);
                System.arraycopy(origin, 0, end_tmp, 0, base.dimension);

                end_tmp[i2] = base.getCoords()[i2 + 1][i2];
                origin_tmp[index] = linesSlicing[j];
                end_tmp[index] = linesSlicing[j];

                // System.out.println("index= "+index+"
                // "+Array.toString(origin_tmp));
                // System.out.println("index= "+index+"
                // "+Array.toString(end_tmp)+"\n");

                lightLines[i][j] = new Line(origin_tmp, end_tmp, Color.lightGray);
            }
            i2++;
        }
    }

    private void initDarkLines() {
        // System.out.println(" s setDarkLines");
        darkLine = new Line(origin, end, color);
    }

    private void initDarkLabels() {
        // System.out.println(" s setDarkLabels");
        // offset of lightLabels
        double[] labelOffset = new double[base.dimension];
        labelOffset[index] = 2 * label_offset;

        darkLabel = new Label(name, end, color);
        darkLabel.base_offset = labelOffset;
        if (darkLabelFont != null)
            darkLabel.setFont(darkLabelFont);
    }

    public void init() {
        // System.out.println("Axe.init");
        initOriginEnd();
        setSlicing();

        initDarkLines();
        initDarkLabels();
        if (gridVisible) {
            setLightLines();
            setLightLabels();
        }
    }

    public void resetBase() {
        // System.out.println("Axe.resetBase");
        init();
    }

    public void setEnd(double[] _end) {
        end = _end;
        resetBase();
    }

    public void setOrigin(double[] _origin) {
        origin = _origin;
        resetBase();
    }

    public void setLightLabelText(String[] _lightLabelnames) {
        lightLabelNames = _lightLabelnames;
        resetBase();
    }

    public void setLightLabelFont(Font f) {
        lightLabelFont = f;
        resetBase();
    }

    public void setLightLabelAngle(double angle) {
        lightLabelAngle = angle;
        resetBase();
    }

    private double log(double x) {
        return Math.log(x);
    }

    private double floor(double x) {
        return Math.floor(x);
    }

    public Label getLegendLabel() {
        return darkLabel;
    }

    public void edit(Object plotCanvas) {
        // TODO add other changes possible
        String _name = JOptionPane.showInputDialog((PlotCanvas) plotCanvas, "Choose name", name);
        if (_name != null)
            setLegend(_name);
    }

    public double[] isSelected(int[] screenCoordTest, AbstractDrawer draw) {

        int[] screenCoord = draw.project(darkLabel.coord);

        if ((screenCoord[0] + note_precision > screenCoordTest[0]) && (screenCoord[0] - note_precision < screenCoordTest[0])
                && (screenCoord[1] + note_precision > screenCoordTest[1]) && (screenCoord[1] - note_precision < screenCoordTest[1]))
            return darkLabel.coord;

        return null;
    }

    public void editnote(AbstractDrawer draw) {
        darkLabel.setFont(darkLabelFont.deriveFont(Font.BOLD));
        darkLabel.plot(draw);
        darkLabel.setFont(darkLabelFont.deriveFont(Font.PLAIN));
    }

    /**
     * Change the number of slices in the Axe
     * Created by Jiajing Wang (October 20, 2005)
     * @param num   the number of slices
     * 
     */
    public void setSliceNum(int num) {
    	linear_slicing = num;
    }
    
}
