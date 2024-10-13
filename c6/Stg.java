// Stg.java
// written by mnagaku

import java.io.*;
import java.util.*;
import java.awt.event.*;


/** StgÃþ§O */
public class Stg extends Game2D {


/**
 * «Øºc¤l
 * ³]©wµe­±¤j¤p¡B­«Ã¸¡B¥D°j°é³B²zªº³t«×¡B¤è¦VÁäªº«öÁä­«½Æ¿é¤J³t«×©M©µ¿ð®É¶¡
 */
	public Stg() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 80;
		KEY_SPEED = 40;
		KEY_DELAY = 3;
	}


/**
 * µ{¦¡¶}©lªº¦ì¸m
 * ©I¥sGame2DÃþ§OªºstartGame()
 */
	public static void main(String args[]) {
		startGame("Stg");
	}



/**
 * StgMainÃþ§O<br>
 * ¹CÀ¸¥»Åéªº³B²z
 * @author mnagaku
 */
	public class StgMain extends Game2DMain {

/** ¹Ï¹³ÀÉªº°ÆÀÉ¦W */
		static final String GRP_EXTENSION = ".gif";
/** ³Ì°ª¤À */
		int hiScore = 0;
/** ¹CÀ¸¤¤ªº¤À¼Æ */
		int score = 0;
/** ´Ý¾÷¼Æ */
		int last;
/** ¹CÀ¸¶i¦æªº¼Ò¦¡ 
    1~3:¹CÀ¸(­±) 0:¹CÀ¸¶}©l -1:¼ÐÃD -2:GameOver -3:¯}Ãö */
		int mode = -1;
/** ¦r¦ê */
		String str = "";
/** ¹CÀ¸¤¤µn³õª«ÅéªººÞ²z */
		GameObjects gos;
/** ¨C¦¸¦b¥D°j°é¤¤·|¼W¥[ */
		int mainLoopCount = 0;


/** «Øºc¤l */
		public StgMain() {
			int i, j;

			gos = new GameObjects(10, sprite, keyQ);
// Åª¨úµe­±
			sprite.addGrp(1, "enemy1"+GRP_EXTENSION);
			sprite.addGrp(2, "enemy2"+GRP_EXTENSION);
			sprite.addGrp(3, "enemy3"+GRP_EXTENSION);
			sprite.addGrp(4, "menemy1"+GRP_EXTENSION);
			sprite.addGrp(5, "benemy1"+GRP_EXTENSION);
			sprite.addGrp(6, "own"+GRP_EXTENSION);

			sprite.addGrp(11, "benemy2"+GRP_EXTENSION);

			sprite.addGrp(16, "benemy3"+GRP_EXTENSION);
			sprite.addGrp(17, "enemy10"+GRP_EXTENSION);

			sprite.waitLoad();

// ³]©w­I´º
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
// BGM
			sp.addBgm(1, "09.au");
			sp.playBgm(1);
		}


/** ¥D°j°é¡C«ö·Ó¼Ò¦¡¨Ó¤ÁÅÜ³B²z */
		public boolean mainLoop() {
			InputEventTiny ket;
/* 2002¸¹¥­­±Åã¥Ü¤@¬q®É¶¡«á³]¬°¤£Åã¥Ü */
			mainLoopCount++;
			if(mainLoopCount == 20)
				sprite.setPlaneView(2002, false);

			mouseQ.removeAllElements();
			switch(mode) {
// ¹CÀ¸¹LÃö
				case -3:
					gos.clearGOS();

					sprite.setPlaneString(2002, "Game clear");
					sprite.setPlanePos(2002, 100, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					sprite.setPlaneString(2001, "Hit any key");
					sprite.setPlanePos(2001, 100, 250);
					sprite.setPlaneColor(2001, 255, 255, 255);
					mainLoopCount = 0;

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = -1;
					break;
// Gameover
				case -2:
					gos.clearGOS();

					sprite.setPlaneString(2002, "Game over");
					sprite.setPlanePos(2002, 100, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					sprite.setPlaneString(2001, "Hit any key");
					sprite.setPlanePos(2001, 100, 250);
					sprite.setPlaneColor(2001, 255, 255, 255);
					mainLoopCount = 0;

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = -1;
					break;
// ¼ÐÃDÅã¥Ü
				case -1:
					sprite.setPlaneString(2002, "An ordinary shooting game");
					sprite.setPlanePos(2002, 50, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					sprite.setPlaneString(2001, "Hit any key");
					sprite.setPlanePos(2001, 100, 250);
					sprite.setPlaneColor(2001, 255, 255, 255);
					mainLoopCount = 0;

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = 0;
					break;
// ¹CÀ¸¶}©l
				case 0:
					sprite.setPlaneView(2002, false);
					gos.addGOOwn("Own", CANVAS_SIZE_W / 2 - 16, 300, 1000);
					mode = 1;
					openSequence("stage" + mode + ".txt");
					str = "Stage: " + mode;
					sprite.setPlaneString(2002, str);
					sprite.setPlanePos(2002, 100, 200);
					sprite.setPlaneColor(2002, 255, 255, 255);
					mainLoopCount = 0;
					break;
// ¹CÀ¸¤¤
				default:
					if(gos.isAliveOwn() == false) {
						mode = -2;
						sprite.setPlaneView(2000, false);
						sprite.setPlaneView(2001, false);
						keyQ.removeAllElements();
						break;
					}
					if(readSequence() == false) {
						mode++;

						if(mode > 3) {
							mode = -3;
							keyQ.removeAllElements();
							break;
						}

						openSequence("stage" + mode + ".txt");
						str = "Stage: " + mode;
						sprite.setPlaneString(2002, str);
						sprite.setPlanePos(2002, 100, 200);
						sprite.setPlaneColor(2002, 255, 255, 255);
						mainLoopCount = 0;
						keyQ.removeAllElements();
						break;
					}

					str = "Score: " + toString(gos.getScore(), 5, "0");
					sprite.setPlaneString(2000, str);
					str = "";
					sprite.setPlaneColor(2000, 255, 255, 255);
					for(int i = 0; i < gos.getLast() - 1; i++)
						str += "A";
					sprite.setPlaneString(2001, str);
					sprite.setPlanePos(2001, 0, 384);
					sprite.setPlaneColor(2001, 255, 0, 0);

					gos.moveAll();
					gos.hitCheckOwn();
					gos.hitCheckOwnBow();
			}
			return true;
		}

// ¦U§Ç¦CÃö¥dÀÉ®×ªºÅª¤J»PÃö³s ------------------------------------

/** §Ç¦CÃö¥dÀÉªº°Ñ·Ó¦ì¸m */
		int sequenceCount = 0;
/** ƒV[ƒP?ƒXƒtƒ@ƒC?“Ç‚Ý?‚Ý—p */
		BufferedReader sequenceReader = null;


/** Ãö¥dÀÉªº¶}±Ò */
		boolean openSequence(String filename) {
			sequenceCount = 0;

			try {
				if(sequenceReader != null)
					sequenceReader.close();
				InputStream is = getClass().getResource(filename).openStream();
				InputStreamReader isr = new InputStreamReader(is);
				sequenceReader = new BufferedReader(isr);
			} catch(Exception e) {e.printStackTrace();}
			return true;
		}


/** Åª¤J§Ç¦CÃö¥dÀÉ */
		boolean readSequence() {

			if(sequenceCount < 0) {
				GameObject go;
				Enumeration enum = gos.elements();
				while(enum.hasMoreElements()) {
					go = ((GameObject)(enum.nextElement()));
					if(go.attribute == GameObject.ENEMY)
						return true;
				}
				sequenceCount = 0;
			}

			if(sequenceCount > 0) {
				sequenceCount--;
				return true;
			}

			try {
				String str = sequenceReader.readLine();
				if(str.length() == 0)
					return true;
				StringTokenizer st = new StringTokenizer(str);
				String cmd = st.nextToken();
				if(cmd.equals("sleep") == true) {
					sequenceCount = Integer.parseInt(st.nextToken());
					return true;
				} else if(cmd.equals("wait") == true) {
					sequenceCount = -1;
					return true;
				} else {
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					gos.addGO(cmd, x, y, 1000);
					return true;
				}
			} catch(Exception e) {
				return false;
			}
		}
//--------------------------------------------------------------------


/** «Ø¥ßÅã¥Ü¤À¼Æªº¦r¦ê */
		String toString(int number, int length, String head) {
			String ret = new String("" + number);
			for(int i = ret.length(); i < length; i++)
				ret = head + ret;
			return ret;
		}
	}
}
