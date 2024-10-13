javac -target 1.1 TestCurrentTimeMillis.java
jar cvfm TestCurrentTimeMillis.jar TestCurrentTimeMillis.mf *.class
del *.class

javac -target 1.1 Sample.java
jar cvfm Sample.jar sample.mf *.class *.gif *.au

javadoc -d doc -charset "big5" -private -author Draw.java Game2D.java InputEventTiny.java Queue.java SoundPalette.java Sprite.java
