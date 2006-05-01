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

public class PlotToolBar extends JToolBar {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    // private int actionMode;

    protected ButtonGroup buttonGroup;

    protected JToggleButton buttonCenter;

    protected JToggleButton buttonEdit;

    protected JToggleButton buttonZoom;

    protected JToggleButton buttonRotate;

    protected JToggleButton buttonViewCoords;

    protected JButton buttonSetScales;

    protected JButton buttonDatas;

    protected JButton buttonSavePNGFile;

    protected JButton buttonReset;

    /*--- added by JJWang. July 22, 2005 ---*/
    protected JButton buttonSelectCenter;
    /*--------------------------------------*/
    
    private boolean denySaveSecurity;

    private JFileChooser pngFileChooser;

    /** the currently selected PlotPanel */
    private PlotCanvas plotCanvas;

    private PlotPanel plotPanel;

    public PlotToolBar(PlotPanel pp) {
        plotPanel = pp;
        plotCanvas = pp.plotCanvas;

        try {
            pngFileChooser = new JFileChooser();
            pngFileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".png");
                }

                public String getDescription() {
                    return "Portable Network Graphic file";
                }
            });
        } catch (AccessControlException ace) {
            denySaveSecurity = true;
        }

        buttonGroup = new ButtonGroup();

        buttonCenter = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/center.png")));
        buttonCenter.setToolTipText("Center axes");

        buttonZoom = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/zoom.png")));
        buttonZoom.setToolTipText("Zoom");

        buttonEdit = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/edit.png")));
        buttonEdit.setToolTipText("Edit mode");

        buttonViewCoords = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/position.png")));
        buttonViewCoords.setToolTipText("Highlight coordinates / Highlight plot");

        buttonSetScales = new JButton(new ImageIcon(PlotPanel.class.getResource("icons/scale.png")));
        buttonSetScales.setToolTipText("Set scales");

        buttonDatas = new JButton(new ImageIcon(PlotPanel.class.getResource("icons/data.png")));
        buttonDatas.setToolTipText("Get datas");

        buttonSavePNGFile = new JButton(new ImageIcon(PlotPanel.class.getResource("icons/topngfile.png")));
        buttonSavePNGFile.setToolTipText("Save graphics in a .PNG File");

        buttonReset = new JButton(new ImageIcon(PlotPanel.class.getResource("icons/back.png")));
        buttonReset.setToolTipText("Reset zoom & axes");

        /*--- added by JJWang. July 22, 2005 ---*/
        buttonSelectCenter= new JButton(new ImageIcon(PlotPanel.class.getResource("icons/zoom.png")));
        buttonSelectCenter.setToolTipText("Select Cluster Center");
        
        buttonSelectCenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.ActionMode = PlotCanvas.SELCENTER;
            }
        });
        /*--------------------------------------*/
        
        buttonEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.ActionMode = PlotCanvas.EDIT;
            }
        });

        buttonZoom.setSelected(true);
        buttonZoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.ActionMode = PlotCanvas.ZOOM;
            }
        });

        buttonCenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.ActionMode = PlotCanvas.TRANSLATION;
            }
        });

        buttonViewCoords.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.setNoteCoords(buttonViewCoords.isSelected());
            }
        });

        buttonSetScales.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.displaySetScalesFrame();
            }
        });

        buttonDatas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.displayDatasFrame();
            }
        });

        buttonSavePNGFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choosePNGFile();
            }
        });

        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.resetBase();
            }
        });

        buttonGroup.add(buttonCenter);
        buttonGroup.add(buttonZoom);
        buttonGroup.add(buttonEdit);

        add(buttonCenter, null);
        add(buttonZoom, null);
        add(buttonReset, null);
        add(buttonViewCoords, null);
        add(buttonSetScales, null);
        add(buttonEdit, null);
        add(buttonSavePNGFile, null);
        add(buttonDatas, null);
        
        /*--- added by JJWang. July 22, 2005 ---*/
        buttonGroup.add(buttonSelectCenter);
        add(buttonSelectCenter, null);
        /*--------------------------------------*/
        
        if (!denySaveSecurity) {
            pngFileChooser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveGraphicFile();
                }
            });
        } else {
            buttonSavePNGFile.setEnabled(false);
        }

        buttonEdit.setEnabled(plotCanvas.getEditable());

        buttonViewCoords.setEnabled(plotCanvas.getNotable());

        // allow mixed (2D/3D) plots managed by one toolbar
        if (plotCanvas instanceof Plot3DCanvas) {
            if (buttonRotate == null) {
                buttonRotate = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/rotation.png")));
                buttonRotate.setToolTipText("Rotate axes");

                buttonRotate.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        plotCanvas.ActionMode = Plot3DCanvas.ROTATION;
                    }
                });
                buttonGroup.add(buttonRotate);
                add(buttonRotate, null, 2);
            } else {
                buttonRotate.setEnabled(true);
            }
        } else {
            if (buttonRotate != null) {
                // no removal/disabling just disable
                if (plotCanvas.ActionMode == Plot3DCanvas.ROTATION) {
                    plotCanvas.ActionMode = PlotCanvas.ZOOM;
                }
                buttonRotate.setEnabled(false);
            }
        }
    }

    public int getActionMode() {
        return plotCanvas.ActionMode;
    }

    void choosePNGFile() {
        pngFileChooser.showSaveDialog(this);
    }

    void saveGraphicFile() {
        java.io.File file = pngFileChooser.getSelectedFile();
        try {
            plotPanel.toGraphicFile(file);
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Save failed : " + e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }
}
