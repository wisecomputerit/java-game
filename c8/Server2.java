// Server2.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Server2
 */
public class Server2 extends Thread {
// µ¥«Ý±µ¦¬¨Ï¥Îªº°ð¸¹
	static final int DEFAULT_PORT = 4001;
// ¶Ç°eªº°T®§
	static final String DEFAULT_MESSAGE = "Hello, I am Server2.";
// ¶Ç°e°T®§ªº¦¸¼Æ¡C±µ¦¬ªº¦¸¼Æ¤]¦P¼Ë¡C
	static final int MESSAGE_COUNT = 10;

	static Socket usocket;
	static int recivedCount = 0;


// main()¤èªk
	public static void main(String args[]) {
// params¦s©ñ©R¥O¦æªº°Ñ¼Æ
		Hashtable params = new Hashtable();
		for(int i = 0; i < args.length; i++) {
			if(args[i].indexOf("-") == 0) {
				if(i + 1 < args.length && args[i + 1].indexOf("-") != 0)
					params.put(args[i].substring(1), args[i + 1]);
				else
					params.put(args[i].substring(1), args[i].substring(1));
			}
		}
// ¨ú±o©R¥O¦Cªº³s½u°ð¸¹°Ñ¼Æ“¾
		int portNo = DEFAULT_PORT;
		String tmpStr;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("µ¥«Ý³s½u°ð¸¹ : " + portNo);
// ¨ú±o©R¥O¦Cªº¶Ç°eªº°T®§°Ñ¼Æ
		String sendString = DEFAULT_MESSAGE;
		if((tmpStr = (String)(params.get("message"))) != null)
			sendString = tmpStr;
		System.out.println("¶Ç°e°T®§ : " + sendString);
// ¶Ã¼Æªì©l¤Æ
		Math.random();
		try {
// ³s½u
			ServerSocket serversocket = new ServerSocket(portNo);
			usocket = serversocket.accept();
// ¶}©l±µ¦¬
			(new Server2()).start();
// ¶Ç°e
			ObjectOutputStream os
				= new ObjectOutputStream(usocket.getOutputStream());
			for(int i = 0; i < MESSAGE_COUNT; i++) {
				sleep((long)(Math.random() * 1000.));
				os.writeObject(sendString + " : count " + i);
				os.flush();
				System.out.println("¶Ç°e§¹²¦ : count " + i);
			}
		} catch(Exception e) {e.printStackTrace();}
	}


// ±µ¦¬±M¥Î°õ¦æºü
	public void run() {
		String recivedString = null;
		try {
// ±µ¦¬
			ObjectInputStream is
				= new ObjectInputStream(usocket.getInputStream());
			while(recivedCount < MESSAGE_COUNT) {
				recivedString = (String)is.readObject();
				System.out.println("±µ¦¬°T®§ : " + recivedString);
				recivedCount++;
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}
