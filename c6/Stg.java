// Stg.java
// written by mnagaku

import java.io.*;
import java.util.*;
import java.awt.event.*;


/** Stg類別 */
public class Stg extends Game2D {


/**
 * 建構子
 * 設定畫面大小、重繪、主迴圈處理的速度、方向鍵的按鍵重複輸入速度和延遲時間
 */
	public Stg() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 80;
		KEY_SPEED = 40;
		KEY_DELAY = 3;
	}


/**
 * 程式開始的位置
 * 呼叫Game2D類別的startGame()
 */
	public static void main(String args[]) {
		startGame("Stg");
	}



/**
 * StgMain類別<br>
 * 遊戲本體的處理
 * @author mnagaku
 */
	public class StgMain extends Game2DMain {

/** 圖像檔的副檔名 */
		static final String GRP_EXTENSION = ".gif";
/** 最高分 */
		int hiScore = 0;
/** 遊戲中的分數 */
		int score = 0;
/** 殘機數 */
		int last;
/** 遊戲進行的模式 
    1~3:遊戲(面) 0:遊戲開始 -1:標題 -2:GameOver -3:破關 */
		int mode = -1;
/** 字串 */
		String str = "";
/** 遊戲中登場物體的管理 */
		GameObjects gos;
/** 每次在主迴圈中會增加 */
		int mainLoopCount = 0;


/** 建構子 */
		public StgMain() {
			int i, j;

			gos = new GameObjects(10, sprite, keyQ);
// 讀取畫面
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

// 設定背景
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
// BGM
			sp.addBgm(1, "09.au");
			sp.playBgm(1);
		}


/** 主迴圈。按照模式來切變處理 */
		public boolean mainLoop() {
			InputEventTiny ket;
/* 2002號平面顯示一段時間後設為不顯示 */
			mainLoopCount++;
			if(mainLoopCount == 20)
				sprite.setPlaneView(2002, false);

			mouseQ.removeAllElements();
			switch(mode) {
// 遊戲過關
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
// 標題顯示
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
// 遊戲開始
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
// 遊戲中
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

// 各序列關卡檔案的讀入與關連 ------------------------------------

/** 序列關卡檔的參照位置 */
		int sequenceCount = 0;
/** �V�[�P?�X�t�@�C?����?���p */
		BufferedReader sequenceReader = null;


/** 關卡檔的開啟 */
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


/** 讀入序列關卡檔 */
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


/** 建立顯示分數的字串 */
		String toString(int number, int length, String head) {
			String ret = new String("" + number);
			for(int i = ret.length(); i < length; i++)
				ret = head + ret;
			return ret;
		}
	}
}
