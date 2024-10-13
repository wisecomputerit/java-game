javac -target 1.1 Stg.java
jar cvfm Stg.jar stg.mf *.class *.gif *.au stage?.txt
del *.class
