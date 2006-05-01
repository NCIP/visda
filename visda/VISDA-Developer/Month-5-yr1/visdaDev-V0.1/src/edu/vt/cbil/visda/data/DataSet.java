package edu.vt.cbil.visda.data;

import java.io.File;
import java.util.List;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 * This interface defines the mininal contract between tab delimited data and a 
 * Parser, and also the most basic commonalities between tab delimited data sets:
 * obtaining a list os attribute names, and knowing the URL or origin for example.
 */
public interface DataSet {
	
	/**
	 * Gives the dataSet another non experimental data column during parsing.
	 * @param attribute
	 */
	public void addAttributeName( String attribute );
	
	/**
	 * The dataset will create the correct primitive for <code>data</code>
	 * and store it at (row, column) in it's matrix of values.
	 * @param data parsed value in the dataset file
	 * @param row row index in data
	 * @param column column index in data
	 */
	public void addDataValue( String data, int row, int column );

    /**
     * The Collection of Strings that the user has choosen to name the
     * columns in the data preceding sample names.
     *
     * @return a List of attributes names for the gene related data preceding
     * the expression values.
     */
    public List getAttributeNames();
    
    /**
     * Answer the parsed DataColumns
     * @return a List of DataColumns.
     */
    public List getDataColumns();
    
    /**
     * Answer the parsed DataRows
     * @return a List of DataRows.
     */
    public List getDataRows();
    
    /**
     * Answer the <code>DataRow</code> at the given <code>index</code>
     * @param index the original zero-based position of the DataRow desired.
     * @return the <code>DataRow</code> at the given <code>index</code>
     */
    public DataRow getDataRow( int index );
    
    /**
     * Answer the <code>String</code> representing the value that should
     * be used when an empty space is found in the dataset file.
     * @return a numeric string to fill the empty space in the data
     */
    public String getEmptyDataValue();
    
    /**
     * Answer the experimental value at the intersection of row and column.
     * @param row row index into the data
     * @param column data column index into the data
     * @return Object the value of the experimental data at row & column.
     */
    public Object getValue( int row, int column );

    /**
     * file from which the dataSet comes
     * @return a File denoting the file's location
     */
    public File getFile();
	
	/**
	 * Prepares the dataSet for the given size before parins begins
	 * @param rows # of 
	 * @param cols # of 
	 */
	public void init( int rows, int cols );
	
	/**
	 * True iff the dataset has zero rows
	 * @param id
	 */
	public boolean isEmpty();

	
	/**
	 * Initializes the next experimental data column with the given id.
	 * There really no reason for the parser to use this object, so it's
	 * not returned.  This method should return null after #postParsingInit()
	 * is called.
	 * 
	 * @param id
	 */
	public void nextColumn( String id );
	
	/**
	 * Answers a new, or the next rowObject ready for population
	 * during parsing.  This method should return null after #postParsingInit()
	 * is called.
	 * 
	 * @return the next rowObject ready for population during parsing
	 */
	public DataRow nextRow();
	
	/**
	 * Allows the dataSet to perform any post-parsing calculations
	 * or functions.
	 *
	 */
	public void postParsingInit();
	

    /**
     * Set the file
     * @param aFile the File containing the dataSet
     */
    public void setFile( File aFile );

	
}
