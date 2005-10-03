package edu.vt.cbil.visda.data;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

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

	private int index;
    private String id;
    private String label;
    
    //  Support multiple sample labels
    private String sampleLabelKey = "Default Sample Name";
    private Hashtable sampleLabels;
    private Vector sampleLabelKeys;

    public DefaultSample( String idString ) {
        id = idString;
        sampleLabelKeys = new Vector();
        sampleLabels = new Hashtable();
    }

    public DefaultSample( int index, String idString ) {
    	this.index = index;
        id = idString;
        sampleLabelKeys = new Vector();
        sampleLabels = new Hashtable();
    }
    
    public int getIndex() {
        return index;

    }
    
    public String getId() {
        return id;

    }

    public String getName() {
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

    /**
     * Returns the current active label.
     */
    public String getSampleLabel() {
        String name = (String)this.sampleLabels.get(this.sampleLabelKey);

        if(name == null)
            return " ";
        else{
            return name;
        }
    }
    
    /**
     * Returns the label for the specified key.
     */
    public String getSampleLabel(String key) {
        String name = (String)this.sampleLabels.get(key);

        if(name == null)
            return " ";
        else{
            return name;
        }
    }
    
    /**
     * Returns the current label key.
     */
    public String getSampleKey() {
        return this.sampleLabelKey;
    }
    
    /**
     * Returns the sample keys.
     */
    public Vector getSampleKeys() {
        return this.sampleLabelKeys;
    }
    
    /**
     * Returns the sample keys and pairs
     */
    public Hashtable getSampleLabels() {
        return this.sampleLabels;
    }
    
    /** Sets the current label index.
     */
    public void setSampleKey(String key) {
        this.sampleLabelKey = key;
    }
    
    /** Adds a new key and label value
     */
    public void addNewSampleLabel(String label, String value) {        
       if ((sampleLabelKeys.size()==0) || (!sampleLabelKeys.contains(label)))
            this.sampleLabelKeys.addElement(label);
        this.sampleLabels.put(label, value);
    }
}
