// SugorokuServer.java
// written by mnagaku

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * SugorokuServer
 */
public class SugorokuServer extends Thread {
// �P�ɦs�b�����a�ƤW��
	static final int MAX_MEMBER = 8;
// MAP��
	static final int MAX_MAP = 24;
// ���A�����ݨϥΪ���
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


// main()��k
	public static void main(String args[]) {
// params�s��R�O�檺�Ѽ�
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


// �غc�l
	SugorokuServer() {
// ��l���ܼ�
		for(int i = 0; i < MAX_MEMBER; i++)
			usedID[i] = false;
		ranking.small2big();
	}


// �Τ�s�u����
	public void run() {
		int portNo = DEFAULT_PORT;
		String tmpStr;
// ���o�R�O�C���s�u�𸹰Ѽ�
		if((tmpStr = (String)(params.get("port"))) != null)
			portNo = Integer.parseInt(tmpStr);
		System.out.println("�Τᵥ�ݳs�u�� : " + portNo);
// ���o�q�R�O�C��debug�Ҧ��Ұʪ��Ѽ�
		if(params.get("debug") != null) {
			debugFlag = true;
			System.out.println("debug�Ҧ��Ұ�");
		}
		params = null;
// ���ݥΤ�s�u
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
			System.out.println("���ݥΤ�s�u�o�Ͱ��D");
		}
	}


// �B�z�s�Τ᪺�s�u�n�D
	boolean connectClient(Socket usocket) {
		byte[] recivedBytes;
		String recived;
// �����s�u�n�D�r��
		try {
			ObjectInputStream is
				= new ObjectInputStream((usocket.getInputStream()));
			recived = (String)is.readObject();
		} catch(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			return false;
		}
// �H"join name cID[0] cID[1]"�榡��X�s�u�r�ꪺ���
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
// �p�G�w�����ƪ��Τ�h����\�s��
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
			+ ")�[�J�F");
		System.out.println("�{�b���Τ�ݦ� "
			+ (player.size() + prePlayer.size()) + " �H");
		preAddFlag = false;
		return true;
	}


	String keyword[] = {"MAP", "NEXT", "GOALPLAYER"};

// �C���i���
// ��ڪ�main()�B�z
	public void execute() {
		int i, j;
		String ss;
		System.out.println("SugorokuServer �Ұ�");
// �}�l�Τ�ݳs������
		start();
// �C���~��i�椤
		while(!exitFlag) {
// ���ݥΤ�ݥ[�J
			while(preAddFlag)
				yield();
// �P�_�Τ�ݰe�ӰT���A�p�GOK�N�~�򲣥Ͳ��ʸ�T
			if(reciveAllOK()) {
// ���Τ�[�J
				execFlag = true;
// ���o�����r��
				ss = popRecivedString();
// �ѪR"OK MAP id pos �c NEXT id forward GOALPLAYER goaledId ..."�r��
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
										+ ")��F�ؼФF");
									if(rank < MAX_RANKING)
										System.out.println("�Ʀ�� "+ (rank + 1)
											+ " ��");
									goalPlayerString += tmpKoma.name + " "
										+ tmpKoma.cID[0] + " " + tmpKoma.cID[1]
										+ " " + rank + " ";
								}
								break;
						}
					}
				}
// ���u�h���h�Τ�
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
						+ "," + tmpKoma.cID[1] + ")���u�F");
				}
				if(i > 0)
					System.out.println("�{�b�Τ�ݦ� "
						+ (player.size() + prePlayer.size()) + " �H");
				removeMember.removeAllElements();
// ��X�splayer�ܦ����H��
				newPlayerString = "";
				for(i = player.size(); prePlayer.size() > 0
					&& i < MAX_MEMBER; i++) {
					Koma tmpKoma = prePlayer.getKoma(0);
					prePlayer.removeElementAt(0);
					joinMap(tmpKoma);
					newPlayerString += tmpKoma.name + " " + tmpKoma.cID[0] + " "
						+ tmpKoma.cID[1] + " ";
				}
// ���ͤU�����T���A������Τ�o�e
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


// �P�_�������Τ�౵���U�@�T�������A
	boolean reciveAllOK() {
		int i, j;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
// �p�G�S���ϥΪ�, �h�������౵���T�����A
		if(player.size() + prePlayer.size() <= 0)
			return false;
//����Ҧ��ϥΪ�, �нT�{�����r�ꪺ�̫e���O�H�U���@��, "NEW"�h��ܬ��s�ϥΪ�,
//�p�G�ϥΪ̬��i�������U�Ӫ���T���A��, �h��ܬ�"OK",
//�p�G�ϥΪ̤w�Ѱ��s�u, �h��ܬ�"DEL"
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


// ���o���������F���r��A�w�ƤU���������]�wDummy�r��
	String popRecivedString() {
		int i, j;
		String ret = null;
		Koma tmpKoma;
		KomaVector users[] = {player, prePlayer};
		for(j = 0; j < 2; j++)
			for(i = 0; i < users[j].size(); i++) {
				tmpKoma = users[j].getKoma(i);
// ���o"OK MAP id pos �c NEXT id forward GOALPLAYER goaledId ..."�����r��
				if(ret == null && tmpKoma.recivedString.indexOf("OK") == 0)
					ret = tmpKoma.recivedString;
// �w�ƤU���������]�wDummy�r��
				tmpKoma.recivedString = "NO_STRING";
			}
		return ret;
	}


// �[�J�s��player
	boolean joinMap(Koma koma) {
		int i, j;
// �M�w�Ѥl���C��
		for(i = 0; i < MAX_MEMBER && usedID[i]; i++);
		koma.id = i;
		usedID[i] = true;
// �]�w�Ѥl����l��m
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


// �إ߲{�b�ѽL���p��map
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


// map��T����&move
	String makeSendString(int nextMoveKoma, int moveCount) {
		int i;
// map��T����
		String ret = "MAP ";
		for(i = 0; i < player.size(); i++) {
			Koma tmpKoma = player.getKoma(i);
			ret += Integer.toString(tmpKoma.id) + " ";
			ret += Integer.toString(tmpKoma.pos) + " ";
			ret += Integer.toString(tmpKoma.endPos) + " ";
			ret += tmpKoma.name + " ";
		}
// ���ʸ�T����
		ret += "NEXT " + Integer.toString(nextMoveKoma) + " ";
		ret += Integer.toString(moveCount) + " ";
// ranking��T����
		ret += ranking.makeString();
// �s�[�J���a��T������
		ret += "NEWPLAYER " + newPlayerString;
// GOALPLAYER��T������
		ret += "GOALPLAYER " + goalPlayerString;
// ����USER��
		ret += "USER " + (player.size() + prePlayer.size());
		return ret;
	}



// user�޲z�����OKoma��Pool KomaVector
	class KomaVector extends Vector {
		KomaVector() {
			super();
		}


// �Ǧ^Pool��Koma���w��m(�s��)
		Koma getKoma(int no) {
			return (Koma)elementAt(no);
		}


// ���wKoma�ߤ@��ID���˯�
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



// user�޲z�����O Koma
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


// User�ǰe
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


// User����
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


// �p�G�q�T�B�z���ҥ~�o�ͤF�A�R���ӥΤ�
		void errorSocket(Exception e) {
			if(debugFlag)
				e.printStackTrace();
			stopFlag = true;
			recivedString = "DEL";
			removeMember.add(this);
		}


// �����M�ΰ����
		public void run() {
			while(!stopFlag)
				reciveString();
		}
	}
}
