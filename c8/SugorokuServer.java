// SugorokuServer.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * SugorokuServer
 */
public class SugorokuServer extends Thread {
// ¦P®É¦s¦bªºª±®a¼Æ¤W­­
	static final int MAX_MEMBER = 8;
// MAP¼Æ
	static final int MAX_MAP = 24;
// ¦øªA¾¹µ¥«Ý¨Ï¥Îªº°ð¸¹
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


// main()¤èªk
	public static void main(String args[]) {
// params¦s©ñ©R¥O¦æªº°Ñ¼Æ
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


// «Øºc¤l
	SugorokuServer() {
// ªì©l¤ÆÅÜ¼Æ
		for(int i = 0; i < MAX_MEMBER; i++)
			usedID[i] = false;
		ranking.small2big();
	}


// ¥Î¤á³s½uµ¥«Ý
	public void run() {
		int portNo = DEFAULT_PORT;
		String tmpStr;
// ¨ú±o©R¥O¦Cªº³s½u°ð¸¹°Ñ¼Æ
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("¥Î¤áµ¥«Ý³s½u°ð¸¹ : " + portNo);
// ¨ú±o±q©R¥O¦C¥Îdebug¼Ò¦¡±Ò°Êªº°Ñ¼Æ
		if(params.get("debug") != null) {
			debugFlag = true;
			System.out.println("debug¼Ò¦¡±Ò°Ê");
		}
		params = null;
// µ¥«Ý¥Î¤á³s½u
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
			System.out.println("µ¥«Ý¥Î¤á³s½uµo¥Í°ÝÃD");
		}
	}


// ³B²z·s¥Î¤áªº³s½u­n¨D
	boolean connectClient(Socket usocket) {
		byte[] recivedBytes;
		String recived;
// ±µ¦¬³s½u­n¨D¦r¦ê
		try {
			ObjectInputStream is
				= new ObjectInputStream((usocket.getInputStream()));
			recived = (String)is.readObject();
		} catch(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			return false;
		}
// ¥H"join name cID[0] cID[1]"®æ¦¡©â¥X³s½u¦r¦êªº¸ê®Æ
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
// ¦pªG¤w¦³­«½Æªº¥Î¤á«h¤£­ã³\³s±µ
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
			+ ")¥[¤J¤F");
		System.out.println("²{¦bªº¥Î¤áºÝ¦³ "
			+ (player.size() + prePlayer.size()) + " ¤H");
		preAddFlag = false;
		return true;
	}


	String keyword[] = {"MAP", "NEXT", "GOALPLAYER"};

// ¹CÀ¸¶i¦æ®É
// ¹ê»Úªºmain()³B²z
	public void execute() {
		int i, j;
		String ss;
		System.out.println("SugorokuServer ±Ò°Ê");
// ¶}©l¥Î¤áºÝ³s±µµ¥«Ý
		start();
// ¹CÀ¸Ä~Äò¶i¦æ¤¤
		while(!exitFlag) {
// µ¥«Ý¥Î¤áºÝ¥[¤J
			while(preAddFlag)
				yield();
// §PÂ_¥Î¤áºÝ°e¨Ó°T®§¡A¦pªGOK´NÄ~Äò²£¥Í²¾°Ê¸ê°T
			if(reciveAllOK()) {
// ¦³¥Î¤á¥[¤J
				execFlag = true;
// ¨ú±o±µ¦¬¦r¦ê
				ss = popRecivedString();
// ¸ÑªR"OK MAP id pos c NEXT id forward GOALPLAYER goaledId ..."¦r¦ê
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
										+ ")¨ì¹F¥Ø¼Ð¤F");
									if(rank < MAX_RANKING)
										System.out.println("±Æ¦æ²Ä "+ (rank + 1)
											+ " ¦ì");
									goalPlayerString += tmpKoma.name + " "
										+ tmpKoma.cID[0] + " " + tmpKoma.cID[1]
										+ " " + rank + " ";
								}
								break;
						}
					}
				}
// Â÷½u«h®ø¥h¥Î¤á
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
						+ "," + tmpKoma.cID[1] + ")Â÷½u¤F");
				}
				if(i > 0)
					System.out.println("²{¦b¥Î¤áºÝ¦³ "
						+ (player.size() + prePlayer.size()) + " ¤H");
				removeMember.removeAllElements();
// ¿ï¥X·splayerÅÜ¦¨ªº¤Hª«
				newPlayerString = "";
				for(i = player.size(); prePlayer.size() > 0
					&& i < MAX_MEMBER; i++) {
					Koma tmpKoma = prePlayer.getKoma(0);
					prePlayer.removeElementAt(0);
					joinMap(tmpKoma);
					newPlayerString += tmpKoma.name + " " + tmpKoma.cID[0] + " "
						+ tmpKoma.cID[1] + " ";
				}
// ²£¥Í¤U­±ªº°T®§¡A¹ï¥þ³¡¥Î¤áµo°e
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


// §PÂ_¥þ³¡ªº¥Î¤á¯à±µ¦¬¤U¤@°T®§ªºª¬ºA
	boolean reciveAllOK() {
		int i, j;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
// ¦pªG¨S¦³¨Ï¥ÎªÌ, «hµø¬°¤£¯à±µ¦¬°T®§ª¬ºA
		if(player.size() + prePlayer.size() <= 0)
			return false;
//Ãö©ó©Ò¦³¨Ï¥ÎªÌ, ½Ð½T»{±µ¦¬¦r¦êªº³Ì«e­±¬O¥H¤U­þ¤@²Õ, "NEW"«hªí¥Ü¬°·s¨Ï¥ÎªÌ,
//¦pªG¨Ï¥ÎªÌ¬°¥i±µ¦¬±µ¤U¨Óªº¸ê°Tª¬ºA®É, «hÅã¥Ü¬°"OK",
//¦pªG¨Ï¥ÎªÌ¤w¸Ñ°£³s½u, «hÅã¥Ü¬°"DEL"
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


// ¨ú±o±µ¦¬§¹¦¨¤Fªº¦r¦ê¡A¹w³Æ¤U­±ªº±µ¦¬³]©wDummy¦r¦ê
	String popRecivedString() {
		int i, j;
		String ret = null;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
		for(j = 0; j < 2; j++)
			for(i = 0; i < users[j].size(); i++) {
				tmpKoma = users[j].getKoma(i);
// ¨ú±o"OK MAP id pos c NEXT id forward GOALPLAYER goaledId ..."±µ¦¬¦r¦ê
				if(ret == null && tmpKoma.recivedString.indexOf("OK") == 0)
					ret = tmpKoma.recivedString;
// ¹w³Æ¤U­±ªº±µ¦¬³]©wDummy¦r¦ê
				tmpKoma.recivedString = "NO_STRING";
			}
		return ret;
	}


// ¥[¤J·sªºplayer
	boolean joinMap(Koma koma) {
		int i, j;
// ¨M©w´Ñ¤lªºÃC¦â
		for(i = 0; i < MAX_MEMBER && usedID[i]; i++);
		koma.id = i;
		usedID[i] = true;
// ³]©w´Ñ¤lªºªì©l¦ì¸m
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


// «Ø¥ß²{¦b´Ñ½Lª¬ªpªºmap
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


// map¸ê°T²£¥Í&move
	String makeSendString(int nextMoveKoma, int moveCount) {
		int i;
// map¸ê°T²£¥Í
		String ret = "MAP ";
		for(i = 0; i < player.size(); i++) {
			Koma tmpKoma = player.getKoma(i);
			ret += Integer.toString(tmpKoma.id) + " ";
			ret += Integer.toString(tmpKoma.pos) + " ";
			ret += Integer.toString(tmpKoma.endPos) + " ";
			ret += tmpKoma.name + " ";
		}
// ²¾°Ê¸ê°T²£¥Í
		ret += "NEXT " + Integer.toString(nextMoveKoma) + " ";
		ret += Integer.toString(moveCount) + " ";
// ranking¸ê°T²£¥Í
		ret += ranking.makeString();
// ·s¥[¤Jª±®a¸ê°Tªº²£¥Í
		ret += "NEWPLAYER " + newPlayerString;
// GOALPLAYER¸ê°Tªº²£¥Í
		ret += "GOALPLAYER " + goalPlayerString;
// ¥þ³¡USER¼Æ
		ret += "USER " + (player.size() + prePlayer.size());
		return ret;
	}



// userºÞ²z¥ÎÃþ§OKomaªºPool KomaVector
	class KomaVector extends Vector {
		KomaVector() {
			super();
		}


// ¶Ç¦^Pool¤ºKoma«ü©w¦ì¸m(½s¸¹)
		Koma getKoma(int no) {
			return (Koma)elementAt(no);
		}


// «ü©wKoma°ß¤@ªºID¨ÓÀË¯Á
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



// userºÞ²z¥ÎÃþ§O Koma
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


// User¶Ç°e
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


// User±µ¦¬
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


// ¦pªG³q°T³B²z¦³¨Ò¥~µo¥Í¤F¡A§R°£¸Ó¥Î¤á
		void errorSocket(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			stopFlag = true;
			recivedString = "DEL";
			removeMember.add(this);
		}


// ±µ¦¬±M¥Î°õ¦æºü
		public void run() {
			while(!stopFlag)
				reciveString();
		}
	}
}
