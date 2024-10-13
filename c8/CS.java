// CS.java
// written by mnagaku

import java.net.*;
import java.util.*;

/**
 * CS
 */
public class CS {
// �ϥΪ���
	static final int DEFAULT_PORT = 4001;

	int portNo;
	String sendString;


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
		new CS(params);
	}


// �غc�l
	CS(Hashtable params) {
// ���o�R�O�C���s�u�𸹰Ѽ�
		portNo = DEFAULT_PORT;
		String tmpStr;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
// �üƪ�l��
		Math.random();
// ���o�R�O�C���s�u�D���Ѽ�
		String serverName = (String)(params.get("server"));
		try {
// �s�u���wclient�Ҧ�
			if(serverName != null) {
				System.out.println("�s�userver : " + serverName);
				System.out.println("�� : " + portNo);
				new Communicate(new Socket(serverName, portNo));
// �s�u���wserver�Ҧ�
			} else {
				System.out.println("���ݱ����� : " + portNo);
// ���ݱ����s�u
				ServerSocket serversocket = new ServerSocket(portNo);
				while(true)
					new Communicate(serversocket.accept());
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}
