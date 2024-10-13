// Sample.java
// written by mnagaku

import java.awt.event.*;

/**
 * Sample類別<br>
 * Sample類別使用了Game2D類別
 * @author mnagaku
 */
public class Sample extends Game2D {


/**
 * 建構子
 * 設定畫面大小、重繪、主迴圈處理的速度、方向鍵的按鍵重複輸入速度和延遲時間
 */
	public Sample() {
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
		startGame("Sample");
	}



/**
 * SampleMain類別<br>
 * 遊戲本體的處理
 * @author mnagaku
 */
	public class SampleMain extends Game2DMain {

/** 畫面檔的副檔名 */
		static final String GRP_EXTENSION = ".gif";


/**
 * 建構子
 * 讀入音效和圖檔
 */
		public SampleMain() {
// 讀取GRP
			sprite.addGrp(1, "own" + GRP_EXTENSION);
			sprite.addGrp(2, "benemy2" + GRP_EXTENSION);
			sprite.waitLoad();
// 顯示GRP
			sprite.setPlaneGrp(1, 0, 1);
			sprite.setPlanePos(1, 100, 300);
			sprite.setPlaneGrp(2, 0, 2);
			sprite.setPlanePos(2, 200, 100);
// 背景
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
// 顯示字串
			sprite.setPlaneString(3, "string");
			sprite.setPlanePos(3, 100, 200);
			sprite.setPlaneColor(3, 255, 255, 255);
// BGM
			sp.addBgm(1, "09.au");
			sp.playBgm(1);
		}


/**
 * 主迴圈一圈份的處理
 * 處理游標，按鈕的輸入，改變字串的繪圖位置。
 */
		public boolean mainLoop() {
			InputEventTiny ket;

			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED)
					continue;
				switch(ket.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						sprite.setPlaneMov(1, 0, 2);
						break;
					case KeyEvent.VK_UP:
						sprite.setPlaneMov(1, 0, -2);
						break;
					case KeyEvent.VK_RIGHT:
						sprite.setPlaneMov(1, 2, 0);
						break;
					case KeyEvent.VK_LEFT:
						sprite.setPlaneMov(1, -2, 0);
						break;
				}
			}
			while((ket = (InputEventTiny)(mouseQ.dequeue())) != null) {
				if(ket.getID() != MouseEvent.MOUSE_PRESSED)
					continue;
				sprite.setPlanePos(2, ket.getX(), ket.getY());
			}
			return true;
		}
	}
}
