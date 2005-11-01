package org.math.plot;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

import org.math.io.files.*;
import org.math.plot.canvas.*;
import org.math.plot.components.*;
import org.math.plot.plotObjects.*;
import org.math.plot.plots.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */

public abstract class PlotPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public PlotToolBar plotToolBar;

    public PlotCanvas plotCanvas;

    public LegendPanel plotLegend;

    public final static String EAST = BorderLayout.EAST;

    public final static String SOUTH = BorderLayout.SOUTH;

    public final static String NORTH = BorderLayout.NORTH;

    public final static String WEST = BorderLayout.WEST;

    public final static String INVISIBLE = "INVISIBLE";

    public final static String SCATTER = "SCATTER";

    public final static String LINE = "LINE";

    public final static String BAR = "BAR";

    public final static String HISTOGRAM = "HISTOGRAM";

    public final static String BOX = "BOX";

    public final static String STAIRCASE = "STAIRCASE";

    public final static String GRID = "GRID";

    public PlotPanel(PlotCanvas _canvas, String legendOrientation) {
        plotCanvas = _canvas;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        addPlotToolBar(NORTH);

        addLegend(legendOrientation);

        add(plotCanvas, BorderLayout.CENTER);
    }

    public PlotPanel(PlotCanvas _canvas) {
        this(_canvas, INVISIBLE);
    }

    public void addLegend(String o) {
        if (o.equalsIgnoreCase(EAST)) {
            plotLegend = new LegendPanel(this, LegendPanel.VERTICAL);
            add(plotLegend, EAST);
        } else if (o.equalsIgnoreCase(SOUTH)) {
            plotLegend = new LegendPanel(this, LegendPanel.HORIZONTAL);
            add(plotLegend, SOUTH);
        } else if (o.equalsIgnoreCase(WEST)) {
            plotLegend = new LegendPanel(this, LegendPanel.VERTICAL);
            add(plotLegend, EAST);
        } else if (o.equalsIgnoreCase(NORTH)) {
            plotLegend = new LegendPanel(this, LegendPanel.HORIZONTAL);
            add(plotLegend, NORTH);
        } else if (o.equalsIgnoreCase(INVISIBLE)) {
            plotLegend = new LegendPanel(this, LegendPanel.INVISIBLE);
            // add(legends, BorderLayout.NORTH);
        } else
            System.err.println("Orientation " + o + " is unknonw.");
    }

    public void removeLegend() {
        remove(plotLegend);
    }

    public void setLegendOrientation(String o) {
        removeLegend();
        addLegend(o);
    }

    public void addPlotToolBar(String o) {
        if (o.equalsIgnoreCase(EAST)) {
            plotToolBar = new PlotToolBar(this);
            plotToolBar.setFloatable(false);
            add(plotToolBar, EAST);
        } else if (o.equalsIgnoreCase(SOUTH)) {
            plotToolBar = new PlotToolBar(this);
            plotToolBar.setFloatable(false);
            add(plotToolBar, SOUTH);
        } else if (o.equalsIgnoreCase(WEST)) {
            plotToolBar = new PlotToolBar(this);
            plotToolBar.setFloatable(false);
            add(plotToolBar, WEST);
        } else if (o.equalsIgnoreCase(NORTH)) {
            plotToolBar = new PlotToolBar(this);
            plotToolBar.setFloatable(false);
            add(plotToolBar, NORTH);
        } else
            System.err.println("Orientation " + o + " is unknonw.");
    }

    public void removePlotToolBar() {
        remove(plotToolBar);
    }

    public void setPlotToolBarOrientation(String o) {
        removePlotToolBar();
        addPlotToolBar(o);
    }

    // ///////////////////////////////////////////
    // ////// add/remove elements on plot ////////
    // ///////////////////////////////////////////

    public void addLabel(String text, double[] where, Color c) {
        plotCanvas.addLabel(text, where, c);
    }

    public void addBaseLabel(String text, double[] where, Color c) {
        plotCanvas.addBaseLabel(text, where, c);
    }

    public void addPlotable(Plotable p) {
        plotCanvas.addPlotable(p);
    }

    public void removePlotable(Plotable p) {
        plotCanvas.removePlotable(p);
    }

    public void removePlotable(int i) {
        plotCanvas.removePlotable(i);
    }

    public int addPlot(Plot newPlot) {
        return plotCanvas.addPlot(newPlot);
    }

    public abstract int addPlot(String type, String name, double[][] v);

    public int addPlot(String type, String name, Color c, double[][] v) {
        int i = addPlot(type, name, v);
        changePlotColor(i, c);
        return i;
    }

    public void setPlot(int I, Plot p) {
        plotCanvas.setPlot(I, p);
    }

    public void changePlotData(int I, double[][] XY) {
        plotCanvas.changePlotData(I, XY);
    }

    public void changePlotName(int I, String name) {
        plotCanvas.changePlotName(I, name);
    }

    public void changePlotColor(int I, Color c) {
        plotCanvas.changePlotColor(I, c);
    }

    public void removePlot(int I) {
        plotCanvas.removePlot(I);
    }

    public void removePlot(Plot p) {
        plotCanvas.removePlot(p);
    }

    public void removeAllPlots() {
        plotCanvas.removeAllPlots();
    }

    public void addQuantiletoPlot(int numPlot, double[][] q) {
        plotCanvas.addQuantiletoPlot(numPlot, q);
    }

    public void addQuantiletoPlot(int numPlot, int numAxe, double[] q) {
        plotCanvas.addQuantiletoPlot(numPlot, numAxe, q);
    }

    public void addQuantiletoPlot(int numPlot, int numAxe, double q) {
        plotCanvas.addQuantiletoPlot(numPlot, numAxe, q);
    }

    public void toGraphicFile(File file) throws IOException {
        // otherwise toolbar appears
        plotToolBar.setVisible(false);

        Image image = createImage(getWidth(), getHeight());
        paint(image.getGraphics());
        image = new ImageIcon(image).getImage();

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, Color.WHITE, null);
        g.dispose();

        // make it reappear
        plotToolBar.setVisible(true);

        try {
            ImageIO.write((RenderedImage) bufferedImage, "PNG", file);
        } catch (IllegalArgumentException ex) {
        }
    }

    public static void main(String[] args) {
        String man = "Usage: Plot <-2D|-3D, default = -2D> [options] <ascii file> [[options] other ascii file]\n[options] are:\n  -t <SCATTER|LINE|BAR>\n  -n name\n  -qX <1D ascii file>\n  -qY <1D ascii file>\n  -qZ <1D ascii file>\n";

        PlotPanel p = null;
        if (args[0].equals("-2D"))
            p = new Plot2DPanel();
        else if (args[0].equals("-3D"))
            p = new Plot3DPanel();
        else
            System.out.println(man);

        String leg = "INVISIBLE";
        String type = SCATTER;
        String name = "";
        double[][] v = null;
        double[] qXp = null;
        double[] qYp = null;
        double[] qZp = null;
        double[] qXm = null;
        double[] qYm = null;
        double[] qZm = null;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-l")) {
                leg = args[i + 1];
                i++;
            } else if (args[i].equals("-t")) {
                type = args[i + 1];
                i++;
            } else if (args[i].equals("-n")) {
                name = args[i + 1];
                i++;
            } else if (args[i].equals("-qX")) {
                double[][] q = ASCIIFile.readDoubleArray(new File(args[i + 1]));
                if (q.length != 1) {
                    qXp = new double[q.length];
                    qXm = new double[q.length];
                    for (int j = 0; j < qXp.length; j++) {
                        qXp[j] = q[j][0];
                        qXm[j] = -q[j][0];
                    }
                } else {
                    qXp = new double[q.length];
                    qXm = new double[q.length];
                    for (int j = 0; j < qXp.length; j++) {
                        qXp[j] = q[0][j];
                        qXm[j] = -q[0][j];
                    }
                }
                i++;
            } else if (args[i].equals("-qY")) {
                double[][] q = ASCIIFile.readDoubleArray(new File(args[i + 1]));
                if (q.length != 1) {
                    qYp = new double[q.length];
                    qYm = new double[q.length];
                    for (int j = 0; j < qYp.length; j++) {
                        qYp[j] = q[j][0];
                        qYm[j] = -q[j][0];
                    }
                } else {
                    qYp = new double[q.length];
                    qYm = new double[q.length];
                    for (int j = 0; j < qYp.length; j++) {
                        qYp[j] = q[0][j];
                        qYm[j] = -q[0][j];
                    }
                }
                i++;
            } else if (args[i].equals("-qZ")) {
                double[][] q = ASCIIFile.readDoubleArray(new File(args[i + 1]));
                if (q.length != 1) {
                    qZp = new double[q.length];
                    qZm = new double[q.length];
                    for (int j = 0; j < qZp.length; j++) {
                        qZp[j] = q[j][0];
                        qZm[j] = -q[j][0];
                    }
                } else {
                    qZp = new double[q.length];
                    qZm = new double[q.length];
                    for (int j = 0; j < qZp.length; j++) {
                        qZp[j] = q[0][j];
                        qZm[j] = -q[0][j];
                    }
                }
                i++;
            } else {
                File input_file = new File(args[i]);
                if (input_file.exists()) {
                    v = ASCIIFile.readDoubleArray(input_file);
                    int n = p.addPlot(type, name, v);
                    if (qXp != null)
                        p.addQuantiletoPlot(n, 0, qXp);
                    if (qYp != null)
                        p.addQuantiletoPlot(n, 1, qYp);
                    if (qZp != null)
                        p.addQuantiletoPlot(n, 2, qZp);
                    if (qXm != null)
                        p.addQuantiletoPlot(n, 0, qXm);
                    if (qYm != null)
                        p.addQuantiletoPlot(n, 1, qYm);
                    if (qZm != null)
                        p.addQuantiletoPlot(n, 2, qZm);

                    type = SCATTER;
                    name = "";
                    v = null;
                    qXp = null;
                    qYp = null;
                    qZp = null;
                    qXm = null;
                    qYm = null;
                    qZm = null;
                } else {
                    System.out.println("File " + args[i] + " unknown.");
                    System.out.println(man);
                }
            }
        }
        p.setLegendOrientation(leg);
        FrameView f = new FrameView(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

