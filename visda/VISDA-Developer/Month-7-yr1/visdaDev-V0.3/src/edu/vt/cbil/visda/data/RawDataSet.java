package edu.vt.cbil.visda.data;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface RawDataSet extends GeneSampleDataSet {

    /**
     * returns an entire column of expression values.  this method
     * must copy data to perform and is therefore inherently slow.
     *
     * @param col zero-based column index
     * @return a byte array copied from the matrix of data values
     */
    public double[] getColumn( int col );

    /**
     * returns an entire column of expression values.  this method
     * must copy data to perform and is therefore inherently slow.
     *
     * @param sample
     * @return a byte array copied from the matrix of data values
     */
    public double[] getColumn( Sample sample );

    /**
     * returns an entire row of expression values
     *
     * @param gene zero-based row index
     * @return a byte array copied from the matrix of data values
     */
    public double[] getRow( Gene gene );

    /**
     * returns an entire row of expressin values
     *
     * @param row
     * @return a byte array copied from the matrix of data values
     */
    public double[] getRow( int row );

    /**
     * return a single expersion value from the data
     *
     * @param geneIndex
     * @param sampleIndex
     * @return a single byte denoting the value of the given gene
     * for the given sample
     */
    public double getDouble( int geneIndex, int sampleIndex );
    
    /**
     * return the lowest expression value in the data
     * @return the lowest expression value in the data
     */
    public double getLowest();
    
    /**
     * return the highest expression value in the data
     * @return the highest expression value in the data
     */
    public double getHighest();
	
}
