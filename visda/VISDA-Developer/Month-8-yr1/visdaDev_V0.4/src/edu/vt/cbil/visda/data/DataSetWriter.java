package edu.vt.cbil.visda.data;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 * A class to generically write out a dataSet to the file system.
 */
public class DataSetWriter {

    /**
     * write a brand new dataSet file that we can read in.
     *
     * @param os
     * @throws java.io.IOException
     */
    public void writeToOutputStream( DataSet dataSet, OutputStream os ) throws java.io.IOException {
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( os) );
        List attributeNames = dataSet.getAttributeNames();
        Iterator i = attributeNames.iterator();
        // each user attribute name on it's own line
        while ( i.hasNext() )
            writer.write( (String)i.next() + DataManager.eol );
        // a blank line
        writer.write( DataManager.eol );
        
        // tab delimited columns ( user attributes and data columns )
        i = attributeNames.iterator();
        while ( i.hasNext() ) {
        	writer.write( (String)i.next() + "\t" );
        	
        }
        List dataColumns = dataSet.getDataColumns();
        int nColumns = dataColumns.size();
        i = dataColumns.iterator();
        while ( i.hasNext() ) {
        	DataColumn dc = (DataColumn)i.next();
        	writer.write( dc.getId() + "\t" );
        	
        }
        writer.write( DataManager.eol );
        i = dataSet.getDataRows().iterator();
        // tab delimited row data
        while( i.hasNext() ) {
            DataRow dr = (DataRow)i.next();
            for ( int j=0; j < attributeNames.size(); ++j ) {
                writer.write( dr.getAttribute( j ) + "\t" );

            }
            for ( int j=0; j < nColumns; ++j ) {
                writer.write( dr.getValue( j ) + "\t" );

            }
            writer.write( DataManager.eol );
            writer.flush();

        }

    }
    
}
