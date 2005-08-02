package edu.vt.cbil.visda.data;

import java.io.Serializable;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */

/**
 * A basic implementation of <code>Sample</code>
 */
public class DefaultSample implements Sample, Serializable {

    private String id;
    private String label;

    public DefaultSample( String idString ) {
        id = idString;

    }

    public String getId() {
        return id;

    }

    public String getLabel() {
        return label;

    }
    
    public void setLabel(String labelString) {
    	label = labelString;;

    }
    
    public String toString() {
        return getId();

    }
    
    public DefaultSample deepCopy() {
    	return new DefaultSample( new String( id ) );
    	
    }

}
