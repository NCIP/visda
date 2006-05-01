package edu.vt.cbil.visda.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import edu.vt.cbil.visda.util.UserCancelException;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */

/**
 * A Class to parse and populate DataSet implementors.  Data is expected to be simple,
 * tab-delimited data organized into rows and columns with a row of column headers, and
 * a short header preceding all rows with one blank line separating it from the data.  
 * For example:
 * <pre>
 * # this is a comment
 * Name
 * Brand
 * 
 * Name	Brand	Sample1	Sample2	Sample3
 * jon	Merrel	0	3	4
 * joe	Nike	0	4	4
 * zzz	NA	3	3	0
 * </pre>  
 * 
 * The short header allows for any number of comments, and is expected to contain the 
 * names of all columns that are NOT part of an experiment.
 * 
 */
public class DataSetParser {
	
	int lineNumber;
	
	DataSet dataSet;
	
	public DataSetParser(  DataSet aDataSet ) {
		dataSet = aDataSet;
		
	}

    /**
     * skip any number of commented lines before returning a line that
     * represents data.
     *
     * @param reader
     * @return
     * @throws java.io.IOException
     */
    private String nextUncommentedLine( BufferedReader reader ) throws java.io.IOException {
        String line = null;
        try {
	        do {
	            line = reader.readLine();
				++lineNumber;
	
	        } while ( line != null && line.startsWith( "#" ) );
        
        } catch ( java.io.IOException iox ) {
        	throw new java.io.IOException( "Input file ended abruptly in header.");
        	
        }
        return line;

    }

    /**
     * Parse the input stream and populate an appropriate instance of DataSet
     *
     * @param is an open inputStream
     * @param the location of the inputStream's source
     * @throws java.io.IOException unhandled parse exception
     * @throws UserCancelException if prompted to fix data, the user maybe choose to simply
     * 		   abort.  
     */
    //public DataSet parseFile( File aFile ) throws java.io.IOException,
	//   UserCancelException {
    //public DataSet parseFile( File aFile, String fieldNames[] ) throws java.io.IOException,
	//									   UserCancelException {
    public DataSet parseFile( File aFile, int firstDataLocation[] ) throws java.io.IOException,
										   UserCancelException {
    	// get the name of the data file
    	String fileName = aFile.getName();
        int extIndex = fileName.indexOf('.');
        dataSet.setDatasetName(fileName.substring(0, extIndex));
        
    	InputStream is = new FileInputStream( aFile );
        BufferedReader reader =
                new BufferedReader( new InputStreamReader( is ) );
        String line = null;
        ArrayList attributeNames = new ArrayList();
		StringTokenizer tokenizer;
        lineNumber = 0;  // member variable used by #nextUncommentedLine as well

        
        final int preSpotRows;
        final int preExperimentColumns;
        
        
       	preSpotRows = firstDataLocation[0]+1;
       	preExperimentColumns = firstDataLocation[1];
        
        // READ HEADER
        try {
        	
        	line = nextUncommentedLine( reader );
        	// tabs and end lines only!  spaces are OK
	        tokenizer = new StringTokenizer( line, "\t\n" );
            
        	for (int i = 0; i < preExperimentColumns; i++) {
        		String att = tokenizer.nextToken();
        		validateTextString( att );
        		dataSet.addAttributeName( att );
        		attributeNames.add( att );
            }
		} catch ( NoSuchElementException nsex ) {
			reader.close();
			throw new IOException( "Bad header.  Line: " + lineNumber );
		
		} catch ( NullPointerException npx ) {
			reader.close();
			if ( line == null )
				throw new IOException( "Unexpected end of file in header." );
			else 
				throw new IOException( "Unexpected NullPointerException from data set" );
			
		}
	        
        // now the samples
        int nColumns = 0;
        String sampleId;
        while ( tokenizer.hasMoreElements() ) {
            sampleId = tokenizer.nextToken();
            validateTextString( sampleId );
            dataSet.nextColumn( ++nColumns, sampleId );
            //nColumns++;
        }
        
        // now the sample annotations
        int nAttributes = attributeNames.size();
        boolean firstLabel = true;
        while (lineNumber < preSpotRows) {
        	line = reader.readLine(); //current only one line contains label information
        	lineNumber++;
        
        	StringTokenizer st1 = new StringTokenizer( line, "\t\n");
        	try {
        		String key = st1.nextToken();
        		dataSet.addLabelKeyName(key);
        		for ( int i = 0; i < nColumns; ++i ) {
        			String token = st1.nextToken();
        			// reading lableing information 
        			((DefaultSample) dataSet.getDataColumns().get(i)).addNewSampleLabel(key, token);
        			// default label is the first one
        			if (firstLabel) {
        				((DefaultSample) dataSet.getDataColumns().get(i)).setSampleKey(key);
        			}
        		}  
        		firstLabel = false;
        	} catch ( NoSuchElementException nsex ) {
        		reader.close();
        		throw new IOException( "Line: " + lineNumber + " is missing at least one column of data." );
        	
        	}
        }
        
        // count the remaining rows
        reader.mark( 40000000 ); // 20K genes * 200 chars?!?!?
        int nRows = 0; // rows of real data
        while( reader.readLine() != null )
            nRows++;
        reader.reset();         // TODO should we do this?  or read the file twice?
        
        // now create a gene and one row of microarray data per line
        dataSet.init( nRows, nColumns );
        
		boolean fixMissingData = false; // substitute a 0 automatically when nothing is found
		boolean fixInvalidData = false; // substitute a 0 automatically when NAN is found
        while ( ( line = reader.readLine() ) != null ) {
			lineNumber++;
            StringTokenizer st = new StringTokenizer( line, "\t\n", true );
            DataRow dataRow = dataSet.nextRow();
            try {
	            for ( int i = 0; i < nAttributes; ++i ) {
	            	String token = st.nextToken();
	            	if ( token.equals( "\t" ) ) {
	            		// a missing value!  
	            		dataRow.addEmptyAttribute();
	            		// do not skip the tab!  we already got it!
	            	} else {
	            		// business as usual
	            		validateTextString( token );
	            		dataRow.addAttribute( token );
	            		st.nextToken(); // skip tab
	            	}
	
	            }         
				boolean valueMissing = false; 
				String token = null;
				int userColumn = 0;
	            for ( int i = 0; i < nColumns; ++i ) {
	            	valueMissing = false;
	            	userColumn = nAttributes + i + 1;  // users will count all columns, starting at 1!
					boolean lastColumnMissing = false;
	            	try {
						try {
							token = st.nextToken();
	            			
						} catch (NoSuchElementException nsex) {
							// this is only recoverable if the cause is that the last column
							// of data is missing
							if( i == nColumns - 1 )
								lastColumnMissing = true;
	            			
						}
						if ( token.equals( "\t" ) || lastColumnMissing ) {
							// a missing value!  
							valueMissing = true;
							if ( fixMissingData ) {
								// cool, just drop through
								
							} else {
								int retVal = DataManager.yesToAll( "Missing Data", "Expression data is missing a value at line "+lineNumber+
																   ", column " + userColumn + ".\n  Substitute 0 for all such occurences?" );
								if( retVal == JOptionPane.YES_OPTION ) {
									fixMissingData = true;
									
								} else {
									reader.close();
									throw new UserCancelException( "Missing Data Value" );
									
								}
								
							}
								
						}	
	            		if ( valueMissing ) 
	            			dataRow.addEmptyDataValue();
	            		else
	            			dataRow.addDataValue( token );
	            		
	            	} catch ( NumberFormatException nfx ) {
	            		if ( fixInvalidData ) {
	            			// cool, just drop through
	            		} else {
	            			int retVal = DataManager.yesToAll( "Invalid Data", "NumberFormatException while reading expression data for " + dataRow + 
	            											   "\non line "+lineNumber+", column " + userColumn + ": \"" +token+"\".  Substitute 0 for all such occurences?" );
	            			if ( retVal == JOptionPane.YES_OPTION ) {
	            				fixInvalidData = true;
	            				
	            			} else {
	            				reader.close();
	            				throw new UserCancelException( "Invalid Data", nfx );
	            				
	            			}
	            			
	            		}
	            		dataRow.addEmptyDataValue();
	            		
	            	} 
	                
	                // reader will strip off the last \n, so make sure there's another value
					if( st.hasMoreTokens() && !valueMissing ) 
						st.nextToken(); // skip tab
	
	            }
            } catch ( NoSuchElementException nsex ) {
            	reader.close();
            	throw new IOException( "Line: " + lineNumber + " is missing at least one column of data." );
            	
            }

        }
        reader.close();
        dataSet.setFile( aFile );
        dataSet.postParsingInit();
        return dataSet;

    }
    
    /**
     * If any characters in <code>text</code> are anything but simple text ( ascii 32-126 )
     * throw java.io.IOException.
     * @param text string to validate
     * @throws java.io.IOException if any ascii values for characters are not between 32 and 126.
     */
    private void validateTextString( String text ) throws java.io.IOException {
		for ( int i = 0; i < text.length(); ++i ) {
			char ascii = text.charAt( i );
			if ( ascii < 32 || ascii > 126 ) {
				// not visible ascii char
				throw new IOException( "File appears to be wrong the format.  " + 
						"If this file is\n" +
						"text, it contains an invalid character on line: " + 
						lineNumber + "." );
			}
	        		
		}
		
    }
	
}
