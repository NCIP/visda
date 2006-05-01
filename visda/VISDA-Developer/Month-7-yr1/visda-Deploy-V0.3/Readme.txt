                           Readme.txt
    
                             visda
                          Version 0.3
                         October 1, 2005

================================================================
                            Contents
================================================================
    
    1.0 Introduction and Installation
    2.0 Samples
    3.0 Steps to Run VISDA
    4.0 New features of version 0.2 
    5.0 Interim Release Status
    
================================================================
                        1.0 Introduction and Installation
================================================================
    
This distribution consists of jar and external libraries for 
VISDA Java/C version 0.3 with Graphical User Interface.

JRE5.0 or later is needed for running VISDA.
You can download JRE5.0 at http://java.sun.com/j2se

To run it directly, just copy visda-Deploy-V0.3 directory to your local
machine and double click RunVISDA.bat. 

Note: VISDA_CJavaInterface.dll is required in the same directory as visda.jar.

To open and compile the source code, Eclipse 3.1 is required. 

    
================================================================
                       2.0 Samples
================================================================

Current Java/C version 0.3 only support pre-defined tab-delimited data file. 
An example data file is located under '/visda-Deploy-V0.3'
	 	
    
================================================================
                       3.0 Steps to Run VISDA
================================================================

1. File -> Open a Tab-Delimited DataSet 

   An example data 'NCI_63Test.txt' file is attached. After loading the data, 
   one 'Dataset' node will be added under view tree.


2. Config -> Configure Parameters 

   Chooses the papameters, including the tree level, feature number, label, 
   and display threshold percentage of Posterior Probability. 
   Clicks 'config' button. Then feature selected data table and their performance 
   table can be viewed under the 'Analysis' node of view tree.


3. Analysis -> Start Top-level Analysis 

   Follows the instructions in Dialogs. The first level results will be stored under 
   the 'Level_1' node of view tree.


4. If lower level clustering is needed, then go to:  Analysis -> Start Deeper-level Analysis

   The next lower level results will be stored under the 'Level_2' node of view tree.


5. Repeat step 4 to perform more deeper level analysis.


6. If new analysis for the current dataset is needed, go from step 2 to reconfigure the parameters.
 

7. If new dataset will be loaded, go from step 1 to open a new data file. 



================================================================
                   4.0 New features of version 0.3
================================================================
    

(1) Has an analysis history panel in Java GUI.

(2) All the plots and data tables can be saved by right-click the leaf on the view tree.

(3) Support a preview of inputing a pre-defined tab-delimited data file; The starting data 
    point can be determined by user.

(4) Support class annotation view. On each plot, right click the dot, the annotation message 
    for the corresponding sample will appear in a pop-up window

(5) Input file can have multiple rows for different label annotation.

(6) The selected features' performance table is added.

(7) figures can be saved as eps format.

(8) On each plot node of the view tree, the right-click pop-up menu has option to ZOOM in/out 
    the plot.

(9) On each plot node of the view tree, the right-click pop-up menu has option to display 
    different kinds of label names on the plot.

(10) New analysis for the same data can be performed and viewed in the same window. 
     And analysis for new data file can be performed and viewed following the previous data.

(11) Analysis node and Dataset node can be deleted from the view tree.

(12) History log can be viewed and saved.

================================================================
                   5.0 Interim Release Status
================================================================
    
This code is not for redistribution - it constitutes an interim
release for adopter use only. 

