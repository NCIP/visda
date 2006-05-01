package edu.vt.cbil.visda.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import java.util.ArrayList;

import org.math.plot.Plot2DPanel;


/**
 * The class to generate the main view tree
 * 
 * @author Jiajing Wang
 * @version visda_v0.3
 * 
 * Version Log:
 * 	visda_v0.2 (08/30/2005):  The file is created and basic view tree is implemented.
 * 	visda_v0.3 (09/29/2005):  Support multiple data and multiple analysis view;
 * 								  add history log; Node can be deleted
 */

public class MainViewer extends JPanel {
    
    protected JFrame mainframe;
    protected boolean firstLoad = true;
    protected String currentDataPath;     

    private JSplitPane splitPane;
    
    private JScrollPane viewScrollPane;
    
    private JScrollPane treeScrollPane;
    
    //private ResultTree tree;
    private JTree tree;
    private DefaultMutableTreeNode expNode;
    //private DefaultMutableTreeNode analysisNode;
    //private DefaultMutableTreeNode scriptNode;
    private DefaultMutableTreeNode historyNode;
    
    //  current viewer
    private IViewer viewer;
    
    private int dataCount = 0;
    private ArrayList resultCount;
    
    private boolean modifiedResult = false;
    
    public Plot2DPanel plotTobeAnalysis;
    
    private HistoryViewer historyLog;
    
    
    public MainViewer(JFrame mainframe) {
        this.mainframe = mainframe;
        
        EventListener eventListener = new EventListener();
        //mainframe.addWindowListener(eventListener);
        
        viewScrollPane = createViewScrollPane(eventListener);
        viewScrollPane.setBackground(Color.white);
        
        treeScrollPane = createTreeScrollPane(eventListener);
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, viewScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200);
        mainframe.getContentPane().add(splitPane, BorderLayout.CENTER);   
        
        // by default there is at least one analysis result for one data
        resultCount = new ArrayList();
        resultCount.add(1); 
    }
    

    /**
     * Creates the scroll pane to display calculation results.
     */
    private JScrollPane createViewScrollPane(EventListener listener) {
        JScrollPane scrollPane = new JScrollPane();
        //scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        //scrollPane.getVerticalScrollBar().setToolTipText("Use up/down/pgup/pgdown to scroll image");
        //KeyStroke up = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0);
        //KeyStroke down = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0);
        //yStroke pgup = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_UP, 0);
        //yStroke pgdown = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_DOWN, 0);
        //scrollPane.registerKeyboardAction(listener, "lineup", up, JComponent.WHEN_IN_FOCUSED_WINDOW);
        //scrollPane.registerKeyboardAction(listener, "linedown", down, JComponent.WHEN_IN_FOCUSED_WINDOW);
        //scrollPane.registerKeyboardAction(listener, "pageup", pgup, JComponent.WHEN_IN_FOCUSED_WINDOW);
        //scrollPane.registerKeyboardAction(listener, "pagedown", pgdown, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return scrollPane;
    }
    
    /**
     * Creates the navigation tree.
     */
    private JScrollPane createTreeScrollPane(EventListener listener) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("VisdaViewer");
        
        //this.viewer = new MultipleArrayCanvas(this.framework, new Insets(0, 10, 0, 20));
        
        expNode = new DefaultMutableTreeNode(new LeafInfo("Datasets"));
        root.add(expNode);
        
        //analysisNode = new DefaultMutableTreeNode(new LeafInfo("Analysis Results"));
        //root.add(analysisNode);
        
        //scriptNode = new DefaultMutableTreeNode(new LeafInfo("Script Manager"));
        //root.add(scriptNode);
        
        historyNode = new DefaultMutableTreeNode(new LeafInfo("History"));
        root.add(historyNode);
        historyLog = new HistoryViewer();
        historyNode.add(new DefaultMutableTreeNode(new LeafInfo("History Log", historyLog, historyLog.getJPopupMenu())));
        
        //tree = new ResultTree(root);
        tree = new JTree(root);
        //tree.setAnalysisNode(analysisNode);
        
        tree.addTreeSelectionListener(listener);
        tree.addMouseListener(listener);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setSelectionPath(new TreePath(expNode.getPath()));
        tree.setEditable(false);
        
        ToolTipManager.sharedInstance().registerComponent(tree);
       
        return new JScrollPane(tree);
    }
    
    /**
     * The listener to listen to mouse, action, tree, keyboard and window events.
     */
    private class EventListener extends MouseAdapter implements ActionListener, TreeSelectionListener, KeyListener, WindowListener, java.io.Serializable {
        
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();
            //if (command.equals(ActionManager.CLOSE_COMMAND)) {
            //    onClose();
            //} else if (command.equals(ActionManager.LOAD_FILE_COMMAND)) {
            //    onLoadFile();
            //}
        }
        
        public void valueChanged(TreeSelectionEvent event) {
            onNodeChanged(event);
        }
        
        public void mouseReleased(MouseEvent event) {
            maybeShowPopup(event);
        }
        
        public void mousePressed(MouseEvent event) {
            maybeShowPopup(event);
        }
        
        public void keyReleased(KeyEvent event) {}
        public void keyPressed(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
        
        public void windowOpened(WindowEvent e) {}
        public void windowClosing(WindowEvent e) {
            //onClose();
        }
        public void windowClosed(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
    }
    
    
    /**
     * Adds a new data node when opening a new file
     */
    //private synchronized void addAnalysisResult(DefaultMutableTreeNode node) {
    public void addDataNode(String dataName) {

    	dataCount++;
    	DefaultMutableTreeNode dataNode = new DefaultNode("Dataset ("+dataCount+")");
    	
    	//DefaultMutableTreeNode mainViewNode = new DefaultMutableTreeNode(new LeafInfo("Main View"));
        //dataNode.add(mainViewNode);
            
        //DefaultMutableTreeNode analysisViewNode = new DefaultMutableTreeNode(new LeafInfo("Analysis Results"));
        //dataNode.add(analysisViewNode);
        
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        treeModel.insertNodeInto(dataNode, expNode, expNode.getChildCount());
        TreeSelectionModel selModel = tree.getSelectionModel();
        TreePath treePath = new TreePath(dataNode.getPath());
        selModel.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
        JScrollBar bar = this.treeScrollPane.getHorizontalScrollBar();
        if(bar != null)
            bar.setValue(0);
        
        addHistory("File '"+dataName+"' is loaded into Dataset ("+dataCount+")");    
        resultCount.add(1);
    }
    
    /**
     * Adds a data view node into the current data node.
     */
    //private synchronized void addAnalysisResult(DefaultMutableTreeNode node) {
    public void addRawDataView(DefaultMutableTreeNode node) {
        if (node == null) {
            return;
        }
        //String nodeTitle = (String) node.getUserObject();
        //node.setUserObject(nodeTitle);
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode curDataNode = (DefaultMutableTreeNode)expNode.getLastChild();
       	treeModel.insertNodeInto(node, curDataNode, curDataNode.getChildCount());
        TreeSelectionModel selModel = tree.getSelectionModel();
        TreePath treePath = new TreePath(node.getPath());
        selModel.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
        JScrollBar bar = this.treeScrollPane.getHorizontalScrollBar();
        if(bar != null)
            bar.setValue(0);   
    }
    
    /**
     * Adds a data view node into the current data node.
     */
    //private synchronized void addAnalysisResult(DefaultMutableTreeNode node) {
    public void addDataView(DefaultMutableTreeNode node) {
        if (node == null) {
            return;
        }
        //String nodeTitle = (String) node.getUserObject();
        //node.setUserObject(nodeTitle);
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode curDataNode = (DefaultMutableTreeNode)expNode.getLastChild();
        DefaultMutableTreeNode curAnalysisNode = (DefaultMutableTreeNode)curDataNode.getLastChild();
       	treeModel.insertNodeInto(node, curAnalysisNode, curAnalysisNode.getChildCount());
        TreeSelectionModel selModel = tree.getSelectionModel();
        TreePath treePath = new TreePath(node.getPath());
        selModel.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
        JScrollBar bar = this.treeScrollPane.getHorizontalScrollBar();
        if(bar != null)
            bar.setValue(0);   
    }
    
    /**
     * removes a specified node into the data node.
     */
    //private synchronized void addAnalysisResult(DefaultMutableTreeNode node) {
    public void removeFstDataViewNode() {
    	DefaultMutableTreeNode curDataNode = (DefaultMutableTreeNode)expNode.getLastChild();
        DefaultMutableTreeNode curAnalysisNode = (DefaultMutableTreeNode)curDataNode.getLastChild();
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode dataViewNode = curAnalysisNode.getFirstLeaf();
        if (dataViewNode != null) {
        	treeModel.removeNodeFromParent(dataViewNode);  
        } else {
        	JOptionPane.showMessageDialog(new JFrame(),
				    "No data view can be deleted.",
				    "warning",
				    JOptionPane.WARNING_MESSAGE);
        }
        TreeSelectionModel selModel = tree.getSelectionModel();
        TreePath treePath = new TreePath(curAnalysisNode.getFirstLeaf().getPath());
        selModel.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
        JScrollBar bar = this.treeScrollPane.getHorizontalScrollBar();
        if(bar != null)
            bar.setValue(0);
    }
    
    /**
     * updates a specified data view node from the current data node.
     */
    //private synchronized void addAnalysisResult(DefaultMutableTreeNode node) {
    public void updateDataView(DefaultMutableTreeNode node) {
    	DefaultMutableTreeNode curDataNode = (DefaultMutableTreeNode)expNode.getLastChild();
        DefaultMutableTreeNode curAnalysisNode = (DefaultMutableTreeNode)curDataNode.getLastChild();
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode dataViewNode = curAnalysisNode.getFirstLeaf();
        int dataViewNodeIndex = curAnalysisNode.getIndex(dataViewNode);
        if (dataViewNode != null) {
        	treeModel.removeNodeFromParent(dataViewNode);  
        	treeModel.insertNodeInto(node, curAnalysisNode, dataViewNodeIndex);
        } else {
        	JOptionPane.showMessageDialog(new JFrame(),
				    "Data view cannot be changed or deleted.",
				    "warning",
				    JOptionPane.WARNING_MESSAGE);
        }
        TreeSelectionModel selModel = tree.getSelectionModel();
        TreePath treePath = new TreePath(curAnalysisNode.getFirstLeaf().getPath());
        selModel.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
        JScrollBar bar = this.treeScrollPane.getHorizontalScrollBar();
        if(bar != null)
            bar.setValue(0);       
    }
    
    
    /**
     * Adds a new analysis top node when starting a new analysis
     */
    //private synchronized void addAnalysisResult(DefaultMutableTreeNode node) {
    public void addAnalysisNode() {
 
        // get the current data node
        DefaultMutableTreeNode curDataNode = (DefaultMutableTreeNode)expNode.getLastChild();
        
        // display the analysis counter by the name of this analysis node
        int index = curDataNode.getChildCount();
        //DefaultMutableTreeNode analysisTopNode = new DefaultMutableTreeNode(new LeafInfo("Analysis ("+index+")"));
        DefaultMutableTreeNode analysisTopNode = new DefaultNode("Analysis ("+index+")");
            
        // insert
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        treeModel.insertNodeInto(analysisTopNode, curDataNode, index);
        TreeSelectionModel selModel = tree.getSelectionModel();
        TreePath treePath = new TreePath(analysisTopNode.getPath());
        selModel.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
        JScrollBar bar = this.treeScrollPane.getHorizontalScrollBar();
        if(bar != null)
            bar.setValue(0);
        
        addHistory("Analysis ("+index+") for Dataset ("+dataCount+") is started");
        resultCount.add(1);
    }
    
    
    /**
     * Adds a specified result node into the analysis node.
     */
    //private synchronized void addAnalysisResult(DefaultMutableTreeNode node) {
    public void addAnalysisResult(DefaultMutableTreeNode node) {
        if (node == null) {
            return;
        }
        
        
        // get the current data and current analysis node
        DefaultMutableTreeNode curDataNode = (DefaultMutableTreeNode)expNode.getLastChild(); 
        DefaultMutableTreeNode curAnalysisNode = (DefaultMutableTreeNode)curDataNode.getLastChild();
        
        // update the node name
        String nodeTitle = (String) node.getUserObject();
        int index = curAnalysisNode.getChildCount();
        //nodeTitle += " ("+(index-1)+")";
        modifiedResult = true;
        //node.setUserObject(nodeTitle);
        
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        treeModel.insertNodeInto(node, curAnalysisNode, index);
        TreeSelectionModel selModel = tree.getSelectionModel();
        TreePath treePath = new TreePath(node.getPath());
        selModel.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
        JScrollBar bar = this.treeScrollPane.getHorizontalScrollBar();
        if(bar != null)
            bar.setValue(0);
       
        //addHistory("Analysis Result: "+nodeTitle);
        // this.saveAnalysis();
        // this.loadAnalysis();
    }
    
    public void showAnalysisResult(DefaultMutableTreeNode node) {
    	// expand to the last leaf from the root
        //DefaultMutableTreeNode  root;
        //root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        tree.scrollPathToVisible(new TreePath(node.getFirstLeaf().getPath())); 
    }
    
    /**
     * Adds info into the history node.
     */
    public void addHistory(String info) {
        historyLog.addHistory(info);
        /*
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(info);
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        treeModel.insertNodeInto(node, historyNode, historyNode.getChildCount());
        TreeSelectionModel selModel = tree.getSelectionModel();
        TreePath treePath = new TreePath(node.getPath());
        selModel.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
        this.treeScrollPane.getHorizontalScrollBar().setValue(0);
         */
    }
    
    /**
     * Invoked when the navigation tree node is changed.
     */
    private void onNodeChanged(TreeSelectionEvent event) {
        JTree tree = (JTree)event.getSource();
        TreePath path = event.getPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        Object userObject = node.getUserObject();
        if (!(userObject instanceof LeafInfo)) {
            return;
        }
        setCurrentViewer(((LeafInfo)userObject).getViewer());
       
        Plot2DPanel projPlot = ((LeafInfo)userObject).getPlot();
        if (projPlot == null) {
        	plotTobeAnalysis = new Plot2DPanel(); 
        	plotTobeAnalysis = projPlot;
        }
    }
    
    /**
     * Sets a current viewer. The viewer content will be inserted
     * into the scroll pane view port and the viewer header will
     * be used as the scroll pane header view.
     */
    private void setCurrentViewer(IViewer viewer) {
    	// && handles the cases where selected node does not have an IViewer
        if(viewer == null) {
            return;
        }
        if (this.viewer != null) {
            this.viewer.onDeselected();
        }
        this.viewer = viewer;
        this.viewScrollPane.setViewportView(this.viewer.getContentComponent());
        
        //Top Header (column header)
        JComponent header = viewer.getHeaderComponent();
        if (header != null) {
            this.viewScrollPane.setColumnHeaderView(header);
        } else {
            this.viewScrollPane.setColumnHeader(null);
        }
        
        //Left header (row header)
        JComponent rowHeader = viewer.getRowHeaderComponent();
        if (rowHeader != null) {
            this.viewScrollPane.setRowHeaderView(rowHeader);
        } else {
            this.viewScrollPane.setRowHeader(null);
        }
        
        //Corner components
        JComponent cornerComponent = viewer.getCornerComponent(IViewer.UPPER_LEFT_CORNER);
        if (cornerComponent != null)
            this.viewScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerComponent);
        
        cornerComponent = viewer.getCornerComponent(IViewer.UPPER_RIGHT_CORNER);
        if (cornerComponent != null)
            this.viewScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, cornerComponent);
        
        cornerComponent = viewer.getCornerComponent(IViewer.LOWER_LEFT_CORNER);
        if (cornerComponent != null)
            this.viewScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, cornerComponent);
        
        //this.viewer.onSelected(framework);
        doViewLayout();
    }
    
    /**
     * Updates the scroll pane size according to a current
     * viewer one.
     */
    private void doViewLayout() {
        JViewport header = viewScrollPane.getColumnHeader();
        if (header != null) {
            header.doLayout();
        }
        viewScrollPane.getViewport().doLayout();
        viewScrollPane.doLayout();
        viewScrollPane.repaint();
    }
    	
    public JFrame getFrame() {
        return mainframe;
    }
    
    /**
     * Shows a popup menu for a selected navigation tree node.
     */
    private void maybeShowPopup(MouseEvent e) {
        
        if (!e.isPopupTrigger()) {
            return;
        }
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if (selPath == null) {
            return;
        }
        tree.setSelectionPaths(new TreePath[] {selPath});
        JPopupMenu popup = null;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
        Object userObject = node.getUserObject();
        if (node instanceof DefaultNode) {
        	popup = ((DefaultNode)node).getJPopupMenu();
        }
        if (userObject instanceof LeafInfo) {
            popup = ((LeafInfo)userObject).getJPopupMenu();
        }
        if (popup != null) {
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    
    /**
     * The class to create a new node with popup menu
     * @author Jiajing
     *
     */
    public class DefaultNode extends DefaultMutableTreeNode {
    	JPopupMenu popup;
    	DefaultNode node = this;
    	String nodeName;
    	
    	public DefaultNode(String name) {
    		setUserObject(name);
    		nodeName = name;
    		// initialize popupMenu
        	popup = new JPopupMenu();	
        	JMenuItem delItem = new JMenuItem("Delete");
        	delItem.setActionCommand("delete");
        	delItem.addActionListener(new Listener());
        	popup.add(delItem); 
        	
    	}
    	
        private class Listener extends MouseAdapter implements ActionListener {
        	
            public void actionPerformed(ActionEvent event) {
                String command = event.getActionCommand();
                if (command.equals("delete")) {
                	DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
                	DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.parent;
                	treeModel.removeNodeFromParent(node);
                	addHistory(nodeName+" is deleted");
                	TreeSelectionModel selModel = tree.getSelectionModel();
                    TreePath treePath = new TreePath(parent.getPath());
                    selModel.setSelectionPath(treePath);
                }
            }                           
        }  
        
        public JPopupMenu getJPopupMenu() {
            return popup;
        }
        
    }
    
}
