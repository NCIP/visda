                           Readme.txt
    
                             visda
                          Version 0.4
                         November 1, 2005

================================================================
                            Contents
================================================================
    
    1.0 Introduction and Installation
    2.0 Samples
    3.0 Steps to Run VISDA
    4.0 New features of version 0.4 
    5.0 Interim Release Status
    
================================================================
                        1.0 Introduction and Installation
================================================================
    
This distribution consists of jar and external libraries for 
VISDA Java/C version 0.4 with Graphical User Interface.

JRE5.0 or later is needed for running VISDA.
You can download JRE5.0 at http://java.sun.com/j2se

To run it directly, just copy visda-Deploy-V0.4 directory to your local
machine and double click RunVISDA.bat. 

Note: VISDA_CJavaInterface.dll is required in the same directory as visda.jar.

To open and compile the source code, Eclipse 3.1 is required. 

    
================================================================
                       2.0 Samples
================================================================

Current Java/C version 0.4 only support pre-defined tab-delimited data file. 
An example data file "NCI_63Test.txt" is located under '/visda-Deploy-V0.4'
	 	
    
================================================================
                       3.0 Steps to Run VISDA
================================================================

1. File -> Open a Tab-Delimited DataSet 

   An example data 'NCI_63Test.txt' file is attached. After loading the data, 
   one 'Dataset' node will be added under view tree.


2. Config -> Configure Parameters 

   Chooses the papameters, including the feature number, label, display threshold 
   percentage of Posterior Probability, and MDL option. Clicks 'config' button. 
   Then feature selected data table and their performance table can be viewed under 
   the 'Analysis' node.


3. Analysis -> Start Top-level Analysis 

   Follows the instructions in dialogs. The top level result will be put under 
   the 'Levels/Level_1'. The top cluster result will be put under the 'Clusters/Cluster 1'.


4. If lower level clustering is needed, then go to:  Analysis -> Start Deeper-level Analysis

   The next lower level results will be put under the 'Levels/Level_2'. The new-gerenated 
   clusters will be put under their parent-cluster node.



5. Repeat step 4 to perform more deeper level analysis.


6. If new analysis for the current dataset is needed, go from step 2 to reconfigure 
   the parameters.
 

7. If new dataset will be loaded, go from step 1 to open a new data file. 



================================================================
                   4.0 New features of version 0.4
================================================================
    

1. MDL function is implemented. User can choose to use MDL or NOT use MDL to guide the 
   model selection.

2. The human-interaction procedures are simplified. Only the clusters that will be further 
   partitioned into multiple subclusters, will need to initialize centers. And only new 
   generated clusters will need to select the projection type. 

3. Node 'Clusters' shows the hierarchical growth of the clusters. The new generated 
   subclusters will be put under their parent-cluster node. 

4. User can freely perform more lower level analysis or stop analysis at any time when needed. 
   So the level depth control before analysis is unnecessary.

5. The warning message is provided when the mixture model is failed to generate. 
   In this case, user can retry the analysis on this level by reselect the initial centers, 
   or stop deeper level analysis by assuming no more sub-clusters can be further partitioned.





================================================================
                   5.0 Interim Release Status
================================================================
    
This code is not for redistribution - it constitutes an interim
release for adopter use only. 

