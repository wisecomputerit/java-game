// Communicate.java
// written by mnagaku

import java.net.*;
import java.io.*;

/**
 * Communicate
 */
public class Communicate extends Thread {
// 傳送訊息的次數。接收的次數也相同。
	static final int MESSAGE_COUNT = 10;

	int recivedCount = 0;
	Socket usocket;


// 建構子
	Communicate(Socket usocket) {
		this.usocket = usocket;
		start();
		(new SendMessages()).start();
	}


// 接收專用執行緒
	public void run() {
		String recivedString = null;
		try {
// 接收
			ObjectInputStream is
				= new ObjectInputStream(usocket.getInputStream());
			while(recivedCount < MESSAGE_COUNT) {
				recivedString = (String)is.readObject();
				System.out.println("from "
					+ usocket.toString() + " " + recivedString);
				recivedCount++;
			}
		} catch(Exception e) {e.printStackTrace();}
	}



// 傳送專用執行緒
	class SendMessages extends Thread {
		SendMessages() {}

		public void run() {
			try {
				ObjectOutputStream os
					= new ObjectOutputStream(usocket.getOutputStream());
				long wait;
				for(int i = 0; i < MESSAGE_COUNT; i++) {
					wait = (long)(Math.random() * 1000.);
					sleep(wait);
					os.writeObject("" + wait + " : No." + i);
					os.flush();
					System.out.println("to " + usocket.toString()
						+ " " + wait + " No." + i);
				}
			} catch(Exception e) {e.printStackTrace();}
		}
	}
}
