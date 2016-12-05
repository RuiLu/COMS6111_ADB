JC = javac

.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

CLASSES = \
	Tools.java \
	Lt.java \
	Database.java \
	Rule.java \
	Main.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
