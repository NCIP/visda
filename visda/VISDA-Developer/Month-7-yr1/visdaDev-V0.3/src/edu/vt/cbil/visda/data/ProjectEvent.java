package edu.vt.cbil.visda.data;

import java.util.EventObject;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */
public class ProjectEvent extends EventObject {
	
	boolean rawChanged, quantizedChanged, classChanged;
	
	
	public ProjectEvent( Object source, 
						 boolean raw, 
						 boolean quantized, 
						 boolean classifiers ) {
		super( source );
		rawChanged = raw;
		quantizedChanged = quantized;
		classChanged = classifiers;
		
	}
	
	public boolean getRawChanged() {
		return rawChanged;
		
	}
	
	public boolean getQuantizedChanged() {
		return quantizedChanged;
		
	}
	
	public boolean getClassifiersChanged() {
		return classChanged;
		
	}
	
}
