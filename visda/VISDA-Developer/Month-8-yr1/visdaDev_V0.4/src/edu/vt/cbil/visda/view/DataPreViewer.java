package edu.vt.cbil.visda.view;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTable;
import edu.vt.cbil.visda.comp.*;
import edu.vt.cbil.visda.data.*;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class to view the data after pre-processing
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class DataPreViewer {
	
	public DefaultMutableTreeNode node;
	
	public DataPreViewer(ExperimentData expData) {
		
		int N = expData.numSamples;
		int P = expData.numFeatures;
		double[][] data = expData.data; //expData.rawData;
		
		//DefaultMutableTreeNode node = null;
        long start = System.currentTimeMillis();  
            
        //logger.append("Creating the result viewers");
        long time = System.currentTimeMillis() - start;             
        
        JTable table = createTable(expData.sampleNames.size(), expData.featureNames.size(), 
        		expData.sampleNames, expData.featureNames, expData.attributes, data, 
        		expData.getSNRs());
    	  	
        String title = P+" Features Selected Data";
    	TableViewer tableViewer = new TableViewer(table, title);
    	node = new DefaultMutableTreeNode(new LeafInfo(title, tableViewer,  
    			tableViewer.getJPopupMenu()));
    	
    	title = P+" Selected Feature Performance";
    	
        
	}
    
    private JTable createTable(int samplesNum, int featuresNum, ArrayList sampleNames,
    		ArrayList featureNames, ArrayList attributes, double[][] data, double[] SNR) {
    	
    	String[] columnNames = new String[attributes.size()+1+samplesNum];
    	Object[][] rowData = new Object[featuresNum+1][columnNames.length];
    	int attributeSize = attributes.size();
    	
    	for (int i=0; i<columnNames.length; i++) {
    		if (i < attributeSize) { 			
    			columnNames[i] = (String) attributes.get(i);
    			rowData[0][i] = "";
    		} else if (i==attributeSize) {
    			columnNames[i] = "SNR performance";
    			DefaultSample curSample = (DefaultSample) sampleNames.get(i-attributeSize);
    			rowData[0][i] = curSample.getSampleKey();
    		} else if (i >= attributeSize+1) {
    			DefaultSample curSample = (DefaultSample) sampleNames.get(i-attributeSize-1);
    			columnNames[i] = curSample.getId() ;
    			rowData[0][i] = curSample.getSampleLabel();
    		}
    	}
    	
    	for (int i=1; i<rowData.length; i++) {
    		DefaultGene curGene = (DefaultGene) featureNames.get(i-1);
    		for (int j=0; j<columnNames.length; j++) {
    			if (j < attributeSize) { 
    				rowData[i][j] = curGene.getAttribute(j);
    			} else if (j==attributeSize) {
    				rowData[i][j] = SNR[i-1];
    			} else if (j >= attributeSize+1) {
        			rowData[i][j] = data[j-attributeSize-1][i-1];
        		}
    		}
    	}
    		
    	JTable table = new JTable(rowData, columnNames);
    	table.setGridColor(Color.DARK_GRAY);
    	return table;
    }
}
