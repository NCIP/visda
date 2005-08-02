package org.math.plot.components;

import java.awt.event.*;
import java.io.*;
import java.security.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.math.plot.*;
import org.math.plot.canvas.*;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */

public class MyPlotToolBar extends JToolBar {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    // private int actionMode;

    protected ButtonGroup buttonGroup;

    /*--- added by JJWang. July 22, 2005 ---*/
    protected JButton buttonSelectCenter;
    /*--------------------------------------*/
    
    /** the currently selected PlotPanel */
    private PlotCanvas plotCanvas;

    private PlotPanel plotPanel;

    public MyPlotToolBar(PlotPanel pp) {
        plotPanel = pp;
        plotCanvas = pp.plotCanvas;

        buttonGroup = new ButtonGroup();

        /*--- added by JJWang. July 22, 2005 ---*/
        buttonSelectCenter= new JButton(new ImageIcon(PlotPanel.class.getResource("icons/back.png")));
        buttonSelectCenter.setToolTipText("Cluster Center Selection");
        
        buttonSelectCenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.ActionMode = PlotCanvas.SELCENTER;
            }
        });
        /*--------------------------------------*/
        
        buttonGroup.add(buttonSelectCenter);
        
        add(buttonSelectCenter, null);
        
    }

    public int getActionMode() {
        return plotCanvas.ActionMode;
    }

}