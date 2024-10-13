// Server1.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Server1
 */
public class Server1 {
// ���ݱ����ϥΪ���
	static final int DEFAULT_PORT = 4001;
// �ǰe���T��
	static final String DEFAULT_MESSAGE = "Hello, I am Server1.";


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
// ���o�R�O�C���s�u�𸹰Ѽ�
		int portNo = DEFAULT_PORT;
		String tmpStr;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("�s�u�� : " + portNo);
// ���o�R�O�C���ǰe���T���Ѽ�
		String sendString = DEFAULT_MESSAGE;
		if((tmpStr = (String)(params.get("message"))) != null)
			sendString = tmpStr;
		System.out.println("�ǰe�T�� : " + sendString);
		String recivedString = null;
		try {
// �s�u
			ServerSocket serversocket = new ServerSocket(portNo);
			Socket usocket = serversocket.accept();
// �ǰe
			ObjectInputStream is
				= new ObjectInputStream(usocket.getInputStream());
			recivedString = (String)is.readObject();
			System.out.println("�ǰe����");
// ����
			ObjectOutputStream os
				= new ObjectOutputStream(usocket.getOutputStream());
			os.writeObject(sendString);
			os.flush();
			System.out.println("��������");
		} catch(Exception e) {e.printStackTrace();}
		System.out.println("�����T�� : " + recivedString);
	}
}
