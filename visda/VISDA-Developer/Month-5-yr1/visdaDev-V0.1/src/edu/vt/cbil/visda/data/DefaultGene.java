package edu.vt.cbil.visda.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */

/**
 * A basic implmementation of <code>Gene</code>.
 */
public class DefaultGene implements Gene, Serializable {
	
	/**
	 * a counter to determine which data column to populate
	 */
	private int currentCol;
	
    /**
     * The data set this gene belongs to
     */
    private DataSet dataSet;
	
    /**
     * the original index of this row in the dataset.
     */
	private int index;

    /**
     * the genes attributes from the data set
     */
    private List userAttributes;

    /**
     * Create a new DefaultGene.
     * @param aDataSet the dataset of which <code>this</code> is a part
     * @param vals the empty array for the parser to populate
     * @param dataIndex the row <code>this</code> corresponds to in the dataset
     */
    public DefaultGene( DataSet aDataSet, int dataIndex ) {
        dataSet = aDataSet;
        userAttributes = new ArrayList();
        currentCol = 0;
        index = dataIndex;

    }

    /**
     * allows attributes to be set one at a time by the data set's parser
     * @param attribute
     */
    public void addAttribute( String attribute ) {
        userAttributes.add( attribute );

    }
    
    /**
     * Turn the String into a byte and record it in the array.
     * @param data a parsed token expected to a byte expression value
     */
    public void addDataValue( String data ) {
    	dataSet.addDataValue( data, index, currentCol );
    	// note that a NumberFormatException would be thrown
    	// before our currentCol is incremented, therefore
    	// the exception can be caught and an empty value added
    	// in the right place.
    	currentCol++;
    	
    }
    
    /**
     * Add the standard empty attribute placeholder, new String().
     */
    public void addEmptyAttribute() {
    	addAttribute( new String() );
    	
    }
    
    /**
     * Add a zero into the matrix for the parser, which found a blank
     * entry in the matrix of data.
     */
    public void addEmptyDataValue() {
    	addDataValue( dataSet.getEmptyDataValue() );
    	
    }

    /**
     * accessor to <code>userAttributes</code>
     * @param index zero-based index of attribute @see DataSet#getIndexOfAttribute
     * @return the attribute for the given index
     */
    public String getAttribute( int index ) {
        if ( index >= userAttributes.size() )
            return new String();
        else
            return (String)userAttributes.get( index );

    }
    
    /**
     * Answer this gene's index into the original dataSet
     * @return this gene's index into the original dataSet
     */
    public int getIndex() {
    	return index;
    	
    }

    /**
     * Answer the name of the gene which should be the first user attribute
     * @return the gene's name
     */
    public String getName() {
        String name;
        if ( userAttributes != null && userAttributes.size() > 0 ){
            name = (String)userAttributes.get(0);

        } else {
            name = "Untitled Gene";

        }
        return name;

    }
    
    /**
     * Answer the experimental value at the given column data index
     * @param c the offset in the column data
     * @return the experimental value at the given offset
     * 
     */
    public Object getValue( int c ) {
    	return dataSet.getValue(index, c);
		
    }
    
    void setDataSet( DataSet aDataSet ) {
    	dataSet = aDataSet;
    	
    }

    /**
     * Returns the name of the gene
     * @return same as getName()
     */
    public String toString() {
        return getName();

    }
    
    /**
     * Answer a perfect copy of <code>this</code> that points to
     * another dataset
     * @param dataSet the dataSet the copied gene will be part of.
     * @return a perfect copy of <code>this</code> that points to
     * another dataset
     */
    public DefaultGene deepCopyFor( DataSet dataSet ) {
    	DefaultGene gene = new DefaultGene( dataSet, index );
    	Iterator i = userAttributes.iterator();
    	while ( i.hasNext() ) {
    		String att = (String)i.next();
    		gene.addAttribute( att );
    		
    	}
    	return gene;
    	
    }

}
