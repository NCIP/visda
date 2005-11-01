package edu.vt.cbil.visda.data;


/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */

/**
 * An interface to govern how gene attributes are stored and retrieved.
 */
public interface Gene extends DataRow {

    /**
     * Answer the name of the gene which should be the first user attribute
     * @return the gene's name
     */
    public String getName();

}
