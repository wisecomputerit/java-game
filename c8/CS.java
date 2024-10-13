// CS.java
// written by mnagaku

import java.net.*;
import java.util.*;

/**
 * CS
 */
public class CS {
// ㄏノ梆腹
	static final int DEFAULT_PORT = 4001;

	int portNo;
	String sendString;


// main()よ猭
	public static void main(String args[]) {
// params㏑︽把计
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


// 篶
	CS(Hashtable params) {
// 眔㏑硈絬梆腹把计
		portNo = DEFAULT_PORT;
		String tmpStr;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
// 睹计﹍て
		Math.random();
// 眔㏑硈絬诀把计
		String serverName = (String)(params.get("server"));
		try {
// 硈絬﹚client家Α
			if(serverName != null) {
				System.out.println("硈絬server : " + serverName);
				System.out.println("梆腹 : " + portNo);
				new Communicate(new Socket(serverName, portNo));
// 硈絬﹚server家Α
			} else {
				System.out.println("单钡梆腹 : " + portNo);
// 单钡硈絬
				ServerSocket serversocket = new ServerSocket(portNo);
				while(true)
					new Communicate(serversocket.accept());
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}
