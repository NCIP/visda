package edu.vt.cbil.visda.view;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTable;
import edu.vt.cbil.visda.comp.*;
import edu.vt.cbil.visda.data.*;

/**
 * Created with Eclipse
 * Date: Sep 13, 2005
 */

/**
 * The class to view the selected features after pre-processing
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class FeaturePreViewer {
	
	public DefaultMutableTreeNode node;
	
	public FeaturePreViewer(ExperimentData expData) {
		
		int N = expData.numSamples;
		int P = expData.numFeatures;
		double[][] data = expData.rawData;
		
		//DefaultMutableTreeNode node = null;
        long start = System.currentTimeMillis();  
            
        //logger.append("Creating the result viewers");
        long time = System.currentTimeMillis() - start;             
        
        JTable table = createTable(expData.featureNames, expData.attributes, 
        		expData.getSNRs(), expData.getOrgMeans(), expData.getOrgVars());
    	  	
        String title = P+" Selected Feature Performance";
    	TableViewer tableViewer = new TableViewer(table, title);
    	node = new DefaultMutableTreeNode(new LeafInfo(title, tableViewer,  
    			tableViewer.getJPopupMenu()));
    	 
	}
    
    private JTable createTable(ArrayList featureNames, ArrayList attributes, 
    		double[] SNR, double[][] mean, double[][] var) {
    	
    	int featuresNum = featureNames.size();
    	int classNum = mean.length;
    	int attributeSize = attributes.size();
    	String[] columnNames = new String[attributeSize+1+2*classNum];
    	Object[][] rowData = new Object[featuresNum][columnNames.length];
    	
    	for (int i=0; i<attributeSize+1; i++) {
    		if (i < attributeSize) { 			
    			columnNames[i] = (String) attributes.get(i);
    		} else if (i==attributeSize) {
    			columnNames[i] = "SNR performance";
    		}
    	}
    	
    	for (int i=0; i<classNum; i++) { 			
    		columnNames[2*i+attributeSize+1] = "Class_"+(i+1)+"_Mean";
    		columnNames[2*i+1+attributeSize+1] = "Class_"+(i+1)+"_Variance";
    	}
    	
    	for (int i=0; i<rowData.length; i++) {
    		DefaultGene curGene = (DefaultGene) featureNames.get(i);
    		for (int j=0; j<attributeSize+1; j++) {
    			if (j < attributeSize) { 
    				rowData[i][j] = curGene.getAttribute(j);
    			} else if (j==attributeSize) {
    				rowData[i][j] = SNR[i];
    			}
    		}
    		for (int j=0; j<classNum; j++) {
        		rowData[i][2*j+attributeSize+1] = mean[j][i];
        		rowData[i][2*j+1+attributeSize+1] = var[j][i];
    		}
    	}
    		
    	JTable table = new JTable(rowData, columnNames);
    	table.setGridColor(Color.DARK_GRAY);
    	return table;
    }
}
