// Communicate.java
// written by mnagaku

import java.net.*;
import java.io.*;

/**
 * Communicate
 */
public class Communicate extends Thread {
// �ǰe�T�������ơC���������Ƥ]�ۦP�C
	static final int MESSAGE_COUNT = 10;

	int recivedCount = 0;
	Socket usocket;


// �غc�l
	Communicate(Socket usocket) {
		this.usocket = usocket;
		start();
		(new SendMessages()).start();
	}


// �����M�ΰ����
	public void run() {
		String recivedString = null;
		try {
// ����
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



// �ǰe�M�ΰ����
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
