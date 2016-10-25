<<<<<<< HEAD
# Projects Repo for COMS6111-ADB
=======
# First Project - Relevant Feedback
Member
======
Rui Lu (rl2784), Ruijie Zhang (rz2337)

Run
===
1. Compile 

   In command line, enter the src directory, and run "make".

2. Run

   Run "java -cp lib/commons-codec-1.10.jar:lib/org.json.jar: Main <api> <precision> <query>"
  
   api -- Bing search API key.
  
   precision -- The goal precision.
  
   query -- The original query, format: 'keyword1 keyword2 ...'
  
   Example:
  
   `java -cp lib/commons-codec-1.10.jar:lib/org.json.jar: Main 3qAFSNS6zpcHFJjzsvWN4khq90Gv/8kMci8s3jQ6cAM 0.9 'iron man'`

Workflow
========
    First of all, the main class gets the goal precision and the initial key word(s) and creat a corresponding Query object and starts the first search.

    It will tokenlize the results, 10 documents, store them in Document objects and show them to user so that user could mark relevant documents.

    If the precision of this search reach the goal, or is zero, the program will stop.
 
    Then, the main class will divide the documents into two sets, the relevant set and irrelevant set. The Query object and the two sets will be passed into the Rocchio object.

    Inside the Rocchio object, it will calculate the df term of each token, get the weight of each token from query and all document and using Rocchio algorithm to generate a new vector.

    The Rocchio object will return a new query according to the input query and the result vector.

    Then the main class will start a new search using the new query.
    
Query Modification
==================
    The basic idea is based on Rocchio algorithm.    
    We transform queries and documents into vector in different ways.
    
    For documents, the weight of a term in a document is the Euclidean normalized tf value.
    
    For queries, the weight of a term in a document is the tf-idf weight while the df is calculated by the 10 documents returned by this query.
    
    And we set alpha=1.0, beta=0.75 and gamma=0.25.
    
    After getting the vector of the new "query" using Rocchio algorithm, we first pick two keywords with the highest weight among those terms that are not in the current query.
    
    Then, we will reorder all the keywords we have according the the weigts in the result vector in decending order. (Though we did the two steps at the same time in our program.)

