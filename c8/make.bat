javac -target 1.1 Sugoroku.java
jar cvfm Sugoroku.jar sugoroku.mf *.class
del *.class

javac -target 1.1 SugorokuServer.java
jar cvfm SugorokuServer.jar sugorokuserver.mf *.class
del *.class

javac -target 1.1 Server1.java
javac -target 1.1 Client1.java
javac -target 1.1 Server2.java
javac -target 1.1 Client2.java
javac -target 1.1 CS.java
