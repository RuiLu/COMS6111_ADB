JFLAGS = -cp
JC = javac
CLASSPATH = lib/commons-codec-1.10.jar:lib/org.json.jar:

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(CLASSPATH) $*.java

CLASSES = \
	Tools.java \
	Category.java \
	GetWordsLynx.java \
	Main.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
