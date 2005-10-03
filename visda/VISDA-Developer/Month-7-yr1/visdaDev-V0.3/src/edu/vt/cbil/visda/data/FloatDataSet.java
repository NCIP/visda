package edu.vt.cbil.visda.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */
 
/**
 * A dataset to represent raw experimental expression values, or otherwise
 * calculated double values.
 */
public class FloatDataSet implements RawDataSet {
		
	// added by JJW;  Sep 29, 2005
    /**
     * the name of the dataset file
     */
    private String dataSetName;
    
	// added by JJW;  Sep 12, 2005
    /**
     * the complete list of user defined label types in the data
     */
    private ArrayList labelKeyNames;
    
    /**
     * the complete list of user defined attributes in the data
     */
    private ArrayList attributeNames;

	/**
	 * a placeholder for calls to #nextRow
	 * @see #nextRow
	 */
	private int currentRow;

    /**
     * a Collection of Gene objects representing the data
     */
    private ArrayList genes;

    /**
     * number of columns
     */
    private int nColumns;

    /**
     * number of rows
     */
    private int nRows;

    /**
     * our superset, if any
     */
    private RawDataSet parent;

    /**
     * a Collection of Sample objects representing the data
     */
    private ArrayList samples;

    /**
     * the location from which we came.
     */
    private File file;

    /**
     * the matrix of ternary expression values.
     */
    private double[][] values;

    /**
     * True, iff init() has been called and postParsingInit() has not.
     * 
     */
	private boolean isPopulating;
	
	/**
	 * the highest expression value in the data
	 */
	private double highest;
	
	/**
	 * the lowest expression value in the data
	 */
	private double lowest;

	
    public FloatDataSet() {
    	parent = null;
    	isPopulating = true;
    	attributeNames = new ArrayList();
    	labelKeyNames = new ArrayList();  // added by JJW; Sep 12, 2005
    	genes = new ArrayList();
    	samples = new ArrayList();
    	highest = -100;
    	lowest = 100;
    	
    }

    /**
     * Create a new dataset by copying the genes, samples, and 
     * attribute names passed in.
     * @param someGenes
     * @param someSamples
     * @param someValues
     * @param someAttributeNames
     * @param someParent
     */
    public FloatDataSet( ArrayList someGenes,
                            ArrayList someSamples,
                            double[][] someValues,
                            ArrayList someAttributeNames,
                            RawDataSet someParent ) {
    	genes = new ArrayList();
    	Iterator i = someGenes.iterator();
    	while (i.hasNext()) {
    		DefaultGene someGene = (DefaultGene)i.next();
    		genes.add( someGene.deepCopyFor( this ) );
    		
    	}
    	
    	samples = new ArrayList();
    	i = someSamples.iterator();
    	while ( i.hasNext() ) {
    		DefaultSample someSample = (DefaultSample)i.next();
    		samples.add( someSample.deepCopy() );
    		
    	}
        nRows = genes.size();
        nColumns = samples.size();
        values = someValues;
        
        attributeNames = new ArrayList();
        i = someAttributeNames.iterator();
        while (i.hasNext()) {
        	attributeNames.add( new String( (String)i.next() ));
        	
        }
        attributeNames = someAttributeNames;
        parent = someParent;
        findMaxAndMin();
        
    }
    
    /**
     * finds and sets the values of <code>lowest</code> and <code>highest</code>
     * @see #postParsingInit()
     *
     */
    private void findMaxAndMin() {
    	for( int i = values.length - 1; i >= 0; --i ) {
    		for ( int j = values[0].length - 1; j >=0; --j ) {
    			if ( values[i][j] < lowest )
    				lowest = values[i][j];
    			if ( values[i][j] > highest )
    				highest = values[i][j];
    			
    		}
    		
    	}
    	
    }
    
    /**
     * Keep track of this new attribute ( non data column ) name
     */
    public void addAttributeName( String attName ) {
    	attributeNames.add( attName );
    }
	
    /**
     * answer a Collection of all Genes
     * @return a List of all genes in the order they are found in the file
     */
    public List getAllGenes() {
        return genes;
    }

    /**
     * answer a Collection of all Samples.
     * @return a List of all samples in the order they are found in the file
     */
    public List getAllSamples() {
        return samples;
    }
    /**
     * The Collection of Strings that the user has choosen to name the
     * columns in the data preceding sample names.
     *
     * @return a List of attributes names for the gene related data preceding
     * the expression values.
     */
    public List getAttributeNames() {
        return attributeNames;

    }

    /**
     * returns an entire column of expression values.  this method
     * must copy data to perform and is therefore inherently slow.
     *
     * @param col
     * @return a byte array copied from the matrix of data values
     */
    public double[] getColumn( int col ) {
        int rows = genes.size();
        double copy[] = new double[ rows ];
        for ( int i = 0; i < copy.length; ++i )
            copy[i] = values[i][col];

        return copy;

    }

    /**
     * returns an entire column of expression values.  this method
     * must copy data to perform and is therefore inherently slow.
     *
     * @param sample
     * @return a byte array copied from the matrix of data values
     */
    public double[] getColumn( Sample sample ) {
        return getColumn( samples.indexOf( sample ) );

    }

    /**
     * answer the Gene at the given, zero-indexed row
     * @param index
     * @return the Gene at <code>index</code>
     */
    public Gene getGene( int index ) {
        return (Gene)genes.get( index );
    }

    /**
     * returns an entire row of expression values
     *
     * @param gene
     * @return a byte array copied from the matrix of data values
     */
    public double[] getRow( Gene gene ) {
        return getRow( genes.indexOf( gene ) );

    }

    /**
     * returns an entire row of expressin values
     *
     * @param row
     * @return a byte array copied from the matrix of data values
     */
    public double[] getRow( int row ) {
        double[] copy = new double[ samples.size()];
        System.arraycopy( values[row], 0, copy, 0, samples.size() );
        return copy;

    }

    /**
     * answer the Sample at the given, zero-indexed col
     * @param index
     * @return the Gene at <code>index</code>
     */
    public Sample getSample( int index ) {
        return (Sample)samples.get( index );
    }
	   
    /**
     * Answers a List of all DefaultSamples
     */
    public List getDataColumns() {
    	return samples;
    	
    }
    
    /**
     * Answer the indexed DataRow instance
     */
    public DataRow getDataRow(int index) {
    	return (DataRow)genes.get(index);
    }
    
    /**
     * Answers a List of all DataRow instances
     */
    public List getDataRows() {
    	return genes;
    	
    }

    /**
     * get the file
     * @return the File containing this dataSet
     */
    public File getFile() {
        return file;

    }
    
    public Object getValue( int row, int col ) {
    	return new Double( values[row][col] );
    	
    }

    /**
     * return a single expersion value from the data
     *
     * @param geneIndex
     * @param sampleIndex
     * @return a single double denoting the value of the given gene
     * for the given sample
     */
    public double getDouble( int geneIndex, int sampleIndex ) {
        return values[geneIndex][sampleIndex];

    }
	   
    /**
     * Allocate a byte array of the given size, and save the size
     * in <code>nRow + nColumns</code>
     */
    public void init( int rows, int cols ) {
    	nRows = rows;
    	nColumns = cols;
    	values = new double[nRows][nColumns];
    	
    }
    
    /**
     * true iff the dataset has zero rows
     */
    public boolean isEmpty() {
    	return genes.size() == 0;
    	
    }
    
    /**
     * Create the next QDataColumn from the given id.
     */
    public void nextColumn( String id ) {
    	if ( isPopulating ) {
    		DefaultSample sample = new DefaultSample( id );
    		samples.add( sample );
    	
    	}
    		
    }
    
    /**
     * Create the next QDataColumn from the given id.
     */
    public void nextColumn( int index, String id ) {
    	if ( isPopulating ) {
    		DefaultSample sample = new DefaultSample( index, id );
    		samples.add( sample );
    	
    	}
    		
    }
    
    /**
     * Answer the next DataRow to be populated by the parser.
     */
    public DataRow nextRow() {
    	DefaultGene gene;
    	if ( isPopulating ) {
	    	gene = new DefaultGene( this, currentRow );
	    	currentRow++;
	    	genes.add( gene );
	    	
    	} else
    		gene = null;
    	
    	return gene;
    	
    }
    
	/**
	 * Note that parsing is complete
	 */
	public void postParsingInit() {
		isPopulating = false;
		findMaxAndMin();
        nRows = genes.size();
        nColumns = samples.size();
		
	}

    /**
     * Set the file
     * @param aFile the File containing this dataSet
     */
    public void setFile( File aFile ) {
        file = aFile;

    }

    /**
     * Answer a snapshot of the data
     * @return a multiline, tab delimited string representing the data set.
     */
    public String toString() {
        StringBuffer buf =
                new StringBuffer( "FloatDataSet " + nRows + " " + nColumns + DataManager.eol);
        for ( int i = 0; i < nRows; ++i ) {
            for ( int j = 0; j < nColumns; ++j ) {
                buf.append( values[i][j] + "\t" );
            }
            buf.append( DataManager.eol );

        }
        return buf.toString();

    }

    /**
     * accessor for lowest value in dataset.  
     */
	public double getLowest() {
		return lowest;
	}

	/**
	 * accessor for highest value in dataset.
	 */
	public double getHighest() {
		return highest;
	}

	public void addDataValue(String data, int row, int column) {
		Double doubleObject = new Double(data);
    	values[row][column] = doubleObject.doubleValue();
		
	}

	public String getEmptyDataValue() {
		return "0.0";
	}
	
	// added by JJW; Sep 12, 2005
	/**
     * Keep track of this new label key ( non data column ) name
     */
    public void addLabelKeyName( String keyName ) {
    	labelKeyNames.add( keyName );
    }
	
    /**
     * The Collection of Strings that the user has choosen to name the
     * rows preceding the expression values.
     *
     * @return a List of label key names for the sample related data preceding
     * the expression values.
     */
    public List getLabelKeyNames() {
        return labelKeyNames;
    }
	
    // added by JJW; Sep 29, 2005
    /**
     * Set the name of this Dataset
     */
    public void setDatasetName( String name ) {
    	dataSetName = name;
    }
    
    /**
     * Get the name of this Dataset
     */
    public String getDatasetName() {
    	return dataSetName;
    }
    
}
