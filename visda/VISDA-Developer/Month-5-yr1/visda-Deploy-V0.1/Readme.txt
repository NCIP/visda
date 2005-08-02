                           Readme.txt
    
                             visda
                          Version 0.1
                         August 1, 2005

================================================================
                            Contents
================================================================
    
    1.0 Introduction and Installation
    2.0 Samples
    3.0 Steps to Run VISDA
    4.0 Features 
    5.0 Interim Release Status
    
================================================================
                        1.0 Introduction and Installation
================================================================
    
This distribution consists of source codes and external libraries for 
VISDA initial c and java version 0.1.

VISDA (VIsual and Statistical Data Analyzer) can navigate into a high 
dimensional microarray data set to discover the hidden clustered data structure, 
and model and visualize the discovery.
 
The current initial c/java version is open source and runs in Java environment.  
JRE5.0 or later is needed for running VISDA.
You can download JRE5.0 at http://java.sun.com/j2se

To run it directly, just copy visda-Deploy-V0.1 directory to your local
machine and double click RunVISDA.bat. 

Note: VISDA_CJavaInterface.dll is required in the same directory as visda.jar.

To open and compile the source code, Eclipse 3.1 is required. 

    
================================================================
                       2.0 Samples
================================================================

Current initial version 0.1 only support pre-defined tab-delimited data file. 
An example data file is located under '/visda-Deploy-V0.1/data'
	 	
    
================================================================
                       3.0 Steps to Run VISDA
================================================================

(1) File -> Open a Tab-Delimited DataSet. An example data file is located under '/visdaDev/data'

(2) File -> Config VISDA Parameters. choose the tree level and feature number

(3) File -> Perform VISDA Analysis. Then follows the instructions in Dialogs


================================================================
                   4.0 Features
================================================================
    
The purpose of this initial release verion 0.1 is to test the c functions for 
VISDA core computation. This version only has basic features of VISDA. 
(1) Has a preliminary Java GUI to import data, configure VISDA parameters, and run 
    VISDA analysis;
(2) Support a pre-defined tab-delimited data input file; 
(3) Support sample clustering;
(4) Has PCA and PPM projections;
(5) Has hiearchical statistical modeling and parameter estimation by EM algorithm. 


================================================================
                   5.0 Interim Release Status
================================================================
    
This code is not for redistribution - it constitutes an interim
release for adopter use only. 

