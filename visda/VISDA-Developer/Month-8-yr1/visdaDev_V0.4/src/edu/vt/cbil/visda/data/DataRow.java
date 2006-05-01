package edu.vt.cbil.visda.data;
/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface DataRow {
	
    /**
     * allows attributes to be set one at a time by the data set's parser
     * @param attribute
     */
    public void addAttribute( String attribute );
    
	/**
	 * Append another data value to the row.
	 * @param data a parsed String to be converted to a specific type
	 * @throws NumberFormatException when an unexpected data value is encountered - 
	 * 'addition' of this value is ignored internally so that the Exception may be
	 * gracefully caught and fixed by subsequently adding a valid data value.
	 */
	public void addDataValue( String data ) throws NumberFormatException;
	
	/**
	 * Add the DataSet-specific value indicating that nothing but a blank was parsed.
	 *
	 */
	public void addEmptyAttribute();
	
	/**
	 * Add the DataSet-specific value indicating that nothing but a blank was parsed.
	 *
	 */
	public void addEmptyDataValue();
	
    /**
     * accessor to user defined attributes in the data file
     * @param index zero-based index of attribute @see DataSet#getIndexOfAttribute
     * @return the attribute for the given index
     */
    public String getAttribute( int index );
    
    /**
     * accesor to experimental data in this row
     * @param dataColumn index into the experimental data column
     * @return the experimental datum at <code>dataColumn</code> index
     */
    public Object getValue( int dataColumn );
    
    /**
     * Answer this row's index into the original dataSet
     * @return this row's index into the original dataSet
     */
    public int getIndex();
	
}
