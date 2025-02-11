// SugorokuServer.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * SugorokuServer
 */
public class SugorokuServer extends Thread {
// 同時存在的玩家數上限
	static final int MAX_MEMBER = 8;
// MAP數
	static final int MAX_MAP = 24;
// 伺服器等待使用的埠號
	static final int DEFAULT_PORT = 4001;

	static final int MAX_RANKING = 10;

	boolean execFlag = false;
	boolean preAddFlag = false;
	boolean usedID[] = new boolean[MAX_MEMBER];
	boolean debugFlag = false;
	boolean exitFlag = false;
	int nextMoveKoma = 0;
	int reciveOKCount = 0;
	int moveKomaNo = 0;
	int moveCount = (int)(Math.random() * 5.99) + 1;
	String newPlayerString = "", goalPlayerString = "";
	static Hashtable params = new Hashtable();
	KomaVector prePlayer = new KomaVector();
	KomaVector player = new KomaVector();
	KomaVector removeMember = new KomaVector();
	Ranking ranking = new Ranking(MAX_RANKING);


// main()方法
	public static void main(String args[]) {
// params存放命令行的參數
		for(int i = 0; i < args.length; i++) {
			if(args[i].indexOf("-") == 0) {
				if(i + 1 < args.length && args[i + 1].indexOf("-") != 0)
					params.put(args[i].substring(1), args[i + 1]);
				else
					params.put(args[i].substring(1), args[i].substring(1));
			}
		}
		(new SugorokuServer()).execute();
	}


// 建構子
	SugorokuServer() {
// 初始化變數
		for(int i = 0; i < MAX_MEMBER; i++)
			usedID[i] = false;
		ranking.small2big();
	}


// 用戶連線等待
	public void run() {
		int portNo = DEFAULT_PORT;
		String tmpStr;
// 取得命令列的連線埠號參數
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("用戶等待連線埠號 : " + portNo);
// 取得從命令列用debug模式啟動的參數
		if(params.get("debug") != null) {
			debugFlag = true;
			System.out.println("debug模式啟動");
		}
		params = null;
// 等待用戶連線
		try {
			ServerSocket serversocket = new ServerSocket(portNo);
			while(!exitFlag) {
				Socket usocket = serversocket.accept();
				connectClient(usocket);
			}
		} catch(Exception e) {
			exitFlag = true;
			if(debugFlag)
				e.printStackTrace();
			System.out.println("等待用戶連線發生問題");
		}
	}


// 處理新用戶的連線要求
	boolean connectClient(Socket usocket) {
		byte[] recivedBytes;
		String recived;
// 接收連線要求字串
		try {
			ObjectInputStream is
				= new ObjectInputStream((usocket.getInputStream()));
			recived = (String)is.readObject();
		} catch(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			return false;
		}
// 以"join name cID[0] cID[1]"格式抽出連線字串的資料
		if(recived.indexOf("join") != 0)
			return false;
		StringTokenizer st = new StringTokenizer(recived);
		if(st.countTokens() != 4)
			return false;
		st.nextToken();
		String name = st.nextToken();
		long cID[] = {0, 0};
		cID[0] = Long.parseLong(st.nextToken());
		cID[1] = Long.parseLong(st.nextToken());
// 如果已有重複的用戶則不准許連接
		for(int i = 0; i < prePlayer.size(); i++) {
			Koma tmpKoma = prePlayer.getKoma(i);
			if(tmpKoma.name == name
				|| (tmpKoma.cID[0]==cID[0] && tmpKoma.cID[1]==cID[1]))
				return false;
		}

		preAddFlag = true;
		while(execFlag)
			yield();
		prePlayer.addElement(new Koma(usocket, name, cID));
		System.out.println(name + "(" + cID[0] + "," + cID[1]
			+ ")加入了");
		System.out.println("現在的用戶端有 "
			+ (player.size() + prePlayer.size()) + " 人");
		preAddFlag = false;
		return true;
	}


	String keyword[] = {"MAP", "NEXT", "GOALPLAYER"};

// 遊戲進行時
// 實際的main()處理
	public void execute() {
		int i, j;
		String ss;
		System.out.println("SugorokuServer 啟動");
// 開始用戶端連接等待
		start();
// 遊戲繼續進行中
		while(!exitFlag) {
// 等待用戶端加入
			while(preAddFlag)
				yield();
// 判斷用戶端送來訊息，如果OK就繼續產生移動資訊
			if(reciveAllOK()) {
// 有用戶加入
				execFlag = true;
// 取得接收字串
				ss = popRecivedString();
// 解析"OK MAP id pos �c NEXT id forward GOALPLAYER goaledId ..."字串
				if(player.size() > 0 && ss != null) {
					StringTokenizer st = new StringTokenizer(ss);
					int mode = -1;
					goalPlayerString = "";
					while(st.hasMoreTokens()) {
						String token = st.nextToken();
						for(i = 0; i < keyword.length; i++)
							if(token.indexOf(keyword[i]) == 0) {
								mode = i;
								break;
							}
						if(i < keyword.length)
							continue;
						switch(mode) {
// MAP
							case 0:
								Koma tmpKoma
									= player.saerch(Integer.parseInt(token));
								if(tmpKoma != null)
									tmpKoma.pos
										= Integer.parseInt(st.nextToken());
								break;
// NEXT
							case 1:
								nextMoveKoma = Integer.parseInt(token);
								moveCount = Integer.parseInt(st.nextToken());
								break;
// GOALPLAYER
							case 2:
								int id = Integer.parseInt(token);
								usedID[id] = false;
								tmpKoma = player.saerch(id);
								if(tmpKoma != null) {
									int rank = ranking.addRanking(tmpKoma.name,
										tmpKoma.forwardCount);
									prePlayer.addElement(tmpKoma);
									player.removeElement(tmpKoma);
									tmpKoma.forwardCount = 0;
									System.out.println(tmpKoma.name + "("
										+ tmpKoma.cID[0] + "," + tmpKoma.cID[1]
										+ ")到達目標了");
									if(rank < MAX_RANKING)
										System.out.println("排行第 "+ (rank + 1)
											+ " 位");
									goalPlayerString += tmpKoma.name + " "
										+ tmpKoma.cID[0] + " " + tmpKoma.cID[1]
										+ " " + rank + " ";
								}
								break;
						}
					}
				}
// 離線則消去用戶
				for(i = 0; i < removeMember.size(); i++) {
					Koma tmpKoma = removeMember.getKoma(i);
					if(player.indexOf(tmpKoma) >= 0) {
						usedID[tmpKoma.id] = false;
						if(nextMoveKoma >= tmpKoma.id)
							nextMoveKoma--;
						player.removeElement(tmpKoma);
					} else
						prePlayer.removeElement(tmpKoma);
					System.out.println(tmpKoma.name + "(" + tmpKoma.cID[0]
						+ "," + tmpKoma.cID[1] + ")離線了");
				}
				if(i > 0)
					System.out.println("現在用戶端有 "
						+ (player.size() + prePlayer.size()) + " 人");
				removeMember.removeAllElements();
// 選出新player變成的人物
				newPlayerString = "";
				for(i = player.size(); prePlayer.size() > 0
					&& i < MAX_MEMBER; i++) {
					Koma tmpKoma = prePlayer.getKoma(0);
					prePlayer.removeElementAt(0);
					joinMap(tmpKoma);
					newPlayerString += tmpKoma.name + " " + tmpKoma.cID[0] + " "
						+ tmpKoma.cID[1] + " ";
				}
// 產生下面的訊息，對全部用戶發送
				if(player.size() > 0) {
					nextMoveKoma %= player.size();
					player.getKoma(nextMoveKoma).forwardCount++;
					ss = makeSendString(nextMoveKoma, moveCount);
					for(i = 0; i < prePlayer.size(); i++)
						prePlayer.getKoma(i).sendString(ss);
					for(i = 0; i < player.size(); i++)
						player.getKoma(i).sendString(ss);
				}

				execFlag = false;
			}
			yield();
		}
	}


// 判斷全部的用戶能接收下一訊息的狀態
	boolean reciveAllOK() {
		int i, j;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
// 如果沒有使用者, 則視為不能接收訊息狀態
		if(player.size() + prePlayer.size() <= 0)
			return false;
//關於所有使用者, 請確認接收字串的最前面是以下哪一組, "NEW"則表示為新使用者,
//如果使用者為可接收接下來的資訊狀態時, 則顯示為"OK",
//如果使用者已解除連線, 則顯示為"DEL"
		for(j = 0; j < 2; j++) {
			for(i = 0; i < users[j].size(); i++) {
				tmpKoma = users[j].getKoma(i);
				if(tmpKoma.recivedString.indexOf("OK") != 0
					&& tmpKoma.recivedString.indexOf("NEW") != 0
					&& tmpKoma.recivedString.indexOf("DEL") != 0)
					break;
			}
			if(i < users[j].size())
				return false;
		}
		return true;
	}


// 取得接收完成了的字串，預備下面的接收設定Dummy字串
	String popRecivedString() {
		int i, j;
		String ret = null;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
		for(j = 0; j < 2; j++)
			for(i = 0; i < users[j].size(); i++) {
				tmpKoma = users[j].getKoma(i);
// 取得"OK MAP id pos �c NEXT id forward GOALPLAYER goaledId ..."接收字串
				if(ret == null && tmpKoma.recivedString.indexOf("OK") == 0)
					ret = tmpKoma.recivedString;
// 預備下面的接收設定Dummy字串
				tmpKoma.recivedString = "NO_STRING";
			}
		return ret;
	}


// 加入新的player
	boolean joinMap(Koma koma) {
		int i, j;
// 決定棋子的顏色
		for(i = 0; i < MAX_MEMBER && usedID[i]; i++);
		koma.id = i;
		usedID[i] = true;
// 設定棋子的初始位置
		int map[] = createMap();
		int pos = (int)(Math.random() * (MAX_MAP - .01));
		for(j = 1; j < MAX_MAP; j++)
			for(i = pos; i != (pos + MAX_MAP - 1) % MAX_MAP;
				i = (i + 1) % MAX_MAP)
				if((map[i] & 2) == 0 && (map[(i + j) % MAX_MAP] & 1) == 0) {
					koma.endPos = i;
					koma.pos = (i+j) % MAX_MAP;
					player.addElement(koma);
					return true;
				}
		return false;
	}


// 建立現在棋盤狀況的map
	int[] createMap() {
		int ret[] = new int[MAX_MAP], i;
		for(i = 0; i < MAX_MAP; i++)
			ret[i] = 0;
		for(i = 0; i < player.size(); i++) {
			Koma tmpKoma = player.getKoma(i);
			ret[tmpKoma.pos] += 1;
			ret[tmpKoma.endPos] += 2;
		}
		return ret;
	}


// map資訊產生&move
	String makeSendString(int nextMoveKoma, int moveCount) {
		int i;
// map資訊產生
		String ret = "MAP ";
		for(i = 0; i < player.size(); i++) {
			Koma tmpKoma = player.getKoma(i);
			ret += Integer.toString(tmpKoma.id) + " ";
			ret += Integer.toString(tmpKoma.pos) + " ";
			ret += Integer.toString(tmpKoma.endPos) + " ";
			ret += tmpKoma.name + " ";
		}
// 移動資訊產生
		ret += "NEXT " + Integer.toString(nextMoveKoma) + " ";
		ret += Integer.toString(moveCount) + " ";
// ranking資訊產生
		ret += ranking.makeString();
// 新加入玩家資訊的產生
		ret += "NEWPLAYER " + newPlayerString;
// GOALPLAYER資訊的產生
		ret += "GOALPLAYER " + goalPlayerString;
// 全部USER數
		ret += "USER " + (player.size() + prePlayer.size());
		return ret;
	}



// user管理用類別Koma的Pool KomaVector
	class KomaVector extends Vector {
		KomaVector() {
			super();
		}


// 傳回Pool內Koma指定位置(編號)
		Koma getKoma(int no) {
			return (Koma)elementAt(no);
		}


// 指定Koma唯一的ID來檢索
		Koma saerch(int id) {
			int i;
			Koma tmpKoma = null;
			for(i = 0; i < size(); i++) {
				tmpKoma = getKoma(i);
				if(tmpKoma.id == id)
					break;
			}
			if(i < size())
				return tmpKoma;
			else
				return null;
		}
	}



// user管理用類別 Koma
	class Koma extends Thread {
		int id = -1, pos, endPos, forwardCount = 0;
		long cID[] = {0, 0};
		boolean stopFlag = false;
		String name, recivedString = "NEW";
		Socket soc;
		ObjectInputStream is;
		ObjectOutputStream os;


		Koma(Socket soc, String name, long[] cID) {
			this.soc = soc;
			this.name = name;
			this.cID = cID;
			start();
		}


// User傳送
		synchronized boolean sendString(String data) {
			try {
				os = new ObjectOutputStream(soc.getOutputStream());
				os.writeObject(data);
				os.flush();
				if(debugFlag)
					System.out.println("send: " + data);
				return true;
			} catch(Exception e) {
				errorSocket(e);
				return false;
			}
		}


// User接收
		String reciveString() {
			try {
				is = new ObjectInputStream(soc.getInputStream());
				recivedString = (String)is.readObject();
				if(debugFlag)
					System.out.println("recive: " + recivedString);
				return recivedString;
			} catch(Exception e) {
				errorSocket(e);
				return null;
			}
		}


// 如果通訊處理有例外發生了，刪除該用戶
		void errorSocket(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			stopFlag = true;
			recivedString = "DEL";
			removeMember.add(this);
		}


// 接收專用執行緒
		public void run() {
			while(!stopFlag)
				reciveString();
		}
	}
}
