package org.math.plot.components;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import org.math.plot.*;
import org.math.plot.canvas.*;
import org.math.plot.plots.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */

public class DatasFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private PlotCanvas plotCanvas;

    private LegendPanel legend;

    public JTabbedPane panels;

    public DatasFrame(PlotCanvas p, LegendPanel l) {
        super("Datas");
        plotCanvas = p;
        legend = l;
        JPanel panel = new JPanel();
        panels = new JTabbedPane();
        for (int i = 0; i < plotCanvas.getPlots().length; i++) {
            panels.add(new DataPanel(/*this, */plotCanvas.getPlot(i)), plotCanvas.getPlot(i).getName());
        }
        panel.add(panels);
        setContentPane(panel);
        pack();
        setVisible(true);
    }

    public class DataPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        MatrixTablePanel XY;

        JCheckBox visible;

        JButton color;

        JPanel plottoolspanel;

        Plot plot;

        DatasFrame dframe;

        public DataPanel(/*DatasFrame _dframe,*/Plot _plot) {
            plot = _plot;
            //dframe = _dframe;
            visible = new JCheckBox("visible");
            visible.setSelected(plot.getVisible());
            color = new JButton();
            color.setBackground(plot.getColor());
            XY = new MatrixTablePanel(plot.getData());

            visible.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (visible.isSelected())
                        plot.setVisible(true);
                    else
                        plot.setVisible(false);
                    /*dframe.*/plotCanvas.repaint();
                }
            });
            color.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color c = JColorChooser.showDialog(plotCanvas, "Choose plot color", plot.getColor());
                    color.setBackground(c);
                    plot.setColor(c);
                    legend.updateLegends();
                    /*dframe.*/plotCanvas.linkedLegendPanel.updateUI();
                    /*dframe.*/plotCanvas.updateUI();
                }
            });

            this.setLayout(new BorderLayout());
            plottoolspanel = new JPanel();
            plottoolspanel.add(visible);
            plottoolspanel.add(color);
            this.add(plottoolspanel, BorderLayout.NORTH);
            this.add(XY, BorderLayout.CENTER);
        }
    }
}
