// Client2.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Client2
 */
public class Client2 extends Thread {
// �s�u�D��
	static final String DEFAULT_SERVER = "localhost";
// �s�u��
	static final int DEFAULT_PORT = 4001;
// �e�X���T��
	static final String DEFAULT_MESSAGE = "Hello, I am Client2.";
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
// ���o�R�O�C���s�u�D���Ѽ�
		String serverName = DEFAULT_SERVER;
		String tmpStr;
		if((tmpStr = (String)(params.get("server"))) != null)
			serverName = tmpStr;
		System.out.println("�s�userver : " + serverName);
// ���o�R�O�C���s�u�𸹰Ѽ�
		int portNo = DEFAULT_PORT;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("�s�u�� : " + portNo);
// ���o�R�O�C���ǰe���T���Ѽ�
		String sendString = DEFAULT_MESSAGE;
		if((tmpStr = (String)(params.get("message"))) != null)
			sendString = tmpStr;
		System.out.println("�ǰe�T�� : " + sendString);
// �üƪ�l��
		Math.random();
		try {
// �s�u
			usocket = new Socket(serverName, portNo);
// �}�l����
		(new Client2()).start();
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
