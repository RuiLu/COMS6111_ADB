# First Project - Relevant Feedback
Member
======
Rui Lu (rl2784), Ruijie Zhang (rz2337)

Run
===
##Compile
In command line, enter the src directory, and run "make".
##Run
  Run "java -cp lib/commons-codec-1.10.jar:lib/org.json.jar: Main <api> <precision> <key words>"
  <api> -- Bing search API key.
  <precision> -- The goal precision.
  <query> -- The original query, format: 'keyword1 keyword2 ...'
  Example:  
  java -cp lib/commons-codec-1.10.jar:lib/org.json.jar: Main 3qAFSNS6zpcHFJjzsvWN4khq90Gv/8kMci8s3jQ6cAM 0.9 'iron man'
