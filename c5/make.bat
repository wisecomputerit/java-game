javac -target 1.1 Block.java
jar cvfm Block.jar block.mf *.class
del *.class

javac -target 1.1 Kuneris.java
jar cvfm Kuneris.jar kuneris.mf *.class *.gif *.au
del *.class
