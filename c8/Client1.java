// Client1.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Client1
 */
public class Client1 {
// 連線主機
	static final String DEFAULT_SERVER = "localhost";
// 連線埠號
	static final int DEFAULT_PORT = 4001;
// 送出的訊息
	static final String DEFAULT_MESSAGE = "Hello, I am Client1.";


// main()方法
	public static void main(String args[]) {
// params存放命令行的參數
		Hashtable params = new Hashtable();
		for(int i = 0; i < args.length; i++) {
			if(args[i].indexOf("-") == 0) {
				if(i + 1 < args.length && args[i + 1].indexOf("-") != 0)
					params.put(args[i].substring(1), args[i + 1]);
				else
					params.put(args[i].substring(1), args[i].substring(1));
			}
		}
// 取得命令列的連線主機參數
		String serverName = DEFAULT_SERVER;
		String tmpStr;
		if((tmpStr = (String)(params.get("server"))) != null)
			serverName = tmpStr;
		System.out.println("連線server : " + serverName);
// 取得命令列的連線埠號參數
		int portNo = DEFAULT_PORT;
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("連線埠號 : " + portNo);
// 取得命令列的傳送的訊息參數
		String sendString = DEFAULT_MESSAGE;
		if((tmpStr = (String)(params.get("message"))) != null)
			sendString = tmpStr;
		System.out.println("傳送訊息 : " + sendString);
		String recivedString = null;
		try {
// 連線
			Socket usocket = new Socket(serverName, portNo);
// 傳送
			ObjectOutputStream os
				= new ObjectOutputStream(usocket.getOutputStream());
			os.writeObject(sendString);
			os.flush();
			System.out.println("傳送完畢");
// 接收
			ObjectInputStream is
				= new ObjectInputStream(usocket.getInputStream());
			recivedString = (String)is.readObject();
			System.out.println("接收完畢");
		} catch(Exception e) {e.printStackTrace();}
		System.out.println("接收訊息 : " + recivedString);
	}
}
