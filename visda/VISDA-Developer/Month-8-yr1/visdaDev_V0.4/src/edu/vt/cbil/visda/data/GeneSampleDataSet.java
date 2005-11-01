package edu.vt.cbil.visda.data;

import java.util.List;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface GeneSampleDataSet extends DataSet {
	
    /**
     * answer a Collection of all Genes
     * @return a List of all genes in the order they are found in the file
     */
    public List getAllGenes();

    /**
     * answer a Collection of all Samples.
     * @return a List of all samples in the order they are found in the file
     */
    public List getAllSamples();

    /**
     * answer the Gene at the given, zero-indexed row
     * @param index
     * @return the Gene at <code>index</code>
     */
    public Gene getGene( int index );

    /**
     * answer the Sample at the given, zero-indexed col
     * @param index
     * @return the Gene at <code>index</code>
     */
    public Sample getSample( int index );
	
}
