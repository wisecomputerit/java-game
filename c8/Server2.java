// Server2.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Server2
 */
public class Server2 extends Thread {
// ���ݱ����ϥΪ���
	static final int DEFAULT_PORT = 4001;
// �ǰe���T��
	static final String DEFAULT_MESSAGE = "Hello, I am Server2.";
// �ǰe�T�������ơC���������Ƥ]�P�ˡC
	static final int MESSAGE_COUNT = 10;

	static Socket usocket;
	static int recivedCount = 0;


// main()��k
	public static void main(String args[]) {
// params�s��R�O�檺�Ѽ�
		Hashtable params = new Hashtable();
		for(int i = 0; i < args.length; i++) {
			if(args[i].indexOf("-") == 0) {
				if(i + 1 < args.length && args[i + 1].indexOf("-") != 0)
					params.put(args[i].substring(1), args[i + 1]);
				else
					params.put(args[i].substring(1), args[i].substring(1));
			}
		}
// ���o�R�O�C���s�u�𸹰ѼƓ�
		int portNo = DEFAULT_PORT;
		String tmpStr;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("���ݳs�u�� : " + portNo);
// ���o�R�O�C���ǰe���T���Ѽ�
		String sendString = DEFAULT_MESSAGE;
		if((tmpStr = (String)(params.get("message"))) != null)
			sendString = tmpStr;
		System.out.println("�ǰe�T�� : " + sendString);
// �üƪ�l��
		Math.random();
		try {
// �s�u
			ServerSocket serversocket = new ServerSocket(portNo);
			usocket = serversocket.accept();
// �}�l����
			(new Server2()).start();
// �ǰe
			ObjectOutputStream os
				= new ObjectOutputStream(usocket.getOutputStream());
			for(int i = 0; i < MESSAGE_COUNT; i++) {
				sleep((long)(Math.random() * 1000.));
				os.writeObject(sendString + " : count " + i);
				os.flush();
				System.out.println("�ǰe���� : count " + i);
			}
		} catch(Exception e) {e.printStackTrace();}
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
				System.out.println("�����T�� : " + recivedString);
				recivedCount++;
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}
