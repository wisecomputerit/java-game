// Kuneris.java
// written by mnagaku

import java.util.*;
import java.awt.event.*;


/** Kuneris類別 */
public class Kuneris extends Game2D {


/**
 * 建構子
 * 設定畫面大小、重繪、主迴圈處理的速度、方向鍵的按鍵重複輸入速度和延遲時間
 */
	public Kuneris() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 120;
		KEY_SPEED = 60;
		KEY_DELAY = 3;
	}


/**
 * 程式開始的位置
 * 呼叫Game2D類別的startGame()
 */
	public static void main(String args[]) {
		startGame("Kuneris");
	}



/**
 * KunerisMain類別<br>
 * 遊戲本體的處理
 * @author mnagaku
 */
	public class KunerisMain extends Game2DMain {

/** 畫面檔的副檔名  */
		static final String GRP_EXTENSION = ".gif";
/** 降落的方塊的數字資料表示形式
    扭動方塊以外的所有旋轉的方塊，
    都可以畫在3×3的方格中 */
		final int blockChips[][][] = {
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{5, 5, 5}, {0, 0, 5}, {0, 0, 0}},
			{{5, 5, 0}, {5, 0, 0}, {5, 0, 0}},
			{{5, 0, 0}, {5, 5, 5}, {0, 0, 0}},
			{{0, 5, 0}, {0, 5, 0}, {5, 5, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{0, 0,10},{10,10,10}, {0, 0, 0}},
			{{10,10,0}, {0,10, 0}, {0,10, 0}},
			{{10,10,10},{10,0, 0}, {0, 0, 0}},
			{{10,0, 0}, {10,0, 0}, {10,10,0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{15,15,15},{0,15, 0}, {0, 0, 0}},
			{{15,0, 0}, {15,15,0}, {15,0, 0}},
			{{0,15, 0},{15,15,15}, {0, 0, 0}},
			{{0,15, 0},{15,15, 0}, {0,15, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{18,18,0}, {0,18,18}, {0, 0, 0}},
			{{0,18, 0}, {18,18,0}, {18,0, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{0,21,21}, {21,21,0}, {0, 0, 0}},
			{{21,0, 0}, {21,21,0}, {0,21, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{23,0, 0}, {0, 0, 0}, {0, 0, 0}},
			{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
		};
/** 表示方塊堆積的情形 */
		int[][] fieldMap;
/** 方塊降落途中的座標位置 */
		int x, y;
/** 最高分 */
		int hiScore = 0;
/** 遊戲中的分數 */
		int score = 0;
/** 殘餘數 */
		int last = 2;
/** 扭動方塊的單位矩形各自的坐標 */
		int[] tailX, tailY;
/** 在主迴圈中會不斷增加 */
		long loopCount = 1;
/** 存放了方塊的6種姿勢 */
		BlockDataVector[] blockDatas;
/** 落下途中方塊的種類 */
		int nowBlockNo = -1;
/** 下一個落下方塊的種類 */
		int nextBlockNo = -1;
/** 落下途中方塊的資料 */
		BlockData nowBlock;
/** 遊戲進行的方式 0:標題 1:遊戲 -1:GameOver */
		int mode = 0;
/** 消除方塊 */
		CrashBlockList crashBlockList;



/** 顯示落下方塊的BlockData類別 */
		class BlockData {

/** 方塊的大小 */
			int width, height;
/** 方塊單位矩形的組合 */
			int[][] chips;
/** 方塊的圖像編號 */
			int grpNo;


/** 建構子 */
			BlockData(int grpNo, int width, int height, int[][] array) {
				this.grpNo = grpNo;
				this.width = width;
				this.height = height;
				chips = array;
			}
		}



/** 顯示方塊種類的BlockDataVector類別 */
		class BlockDataVector extends Vector {

/** 方塊的旋轉狀態 */
			int nowIndex = 0;


/** 方塊旋轉狀態的初始化 */
			void clearIndex() {
				nowIndex = 0;
			}


/** 取得方塊現在的姿勢 */
			BlockData getData() {
				BlockData ret = (BlockData)elementAt(nowIndex);
				nowIndex = ((nowIndex + 1) % size());
				return ret;
			}


/** 取得轉回到前1個姿勢的狀態 */
			void rollBack() {
				nowIndex = (nowIndex - 2 + size()) % size();
			}
		}



/** 顯示飛散的方塊的CrashBlockList類別 */
		class CrashBlockList {

/** 存放飛散的方塊的清單 */
			Vector blocks;
/** 記錄在main loop1回前一時刻的飛散開的方塊數量 */
			int prevCount = 0;


/** 建構子 */
			CrashBlockList() {
				blocks = new Vector();
			}


			void setCrash(int line) {
				int i;
				for(i = 1; i < 9; i++)
					if(fieldMap[i][line] != 0)
						blocks.addElement(new CrashBlock((i - 1) * 24 + 8,
							line * 24 + 8, fieldMap[i][line]));
			}

/** 飛散的單位矩形，離開畫面就不會再回來 */
			void crashBlockEffect() {
				int i;
				CrashBlock now;
				for(i = 0; i < blocks.size(); i++) {
					now = (CrashBlock)blocks.elementAt(i);
					if(now.x < -24 || now.x > 300 || now.y > 400) {
						blocks.removeElementAt(i);
						i--;
						continue;
					}
					sprite.setPlaneGrp(200 + i, 0, now.grpNo);
					sprite.setPlanePos(200 + i, now.x, now.y);
					now.x += now.vx;
					now.y += now.vy;
					now.vy++;
				}
				for(; i < prevCount; i++)
					sprite.setPlaneView(200 + i, false);
				prevCount = blocks.size();
			}


/** 傳回正在飛散的單位矩形數量 */
			int crashBlockCount() {
				return blocks.size();
			}
		}



/** CrashBlock類別處理飛散方塊 */
		class CrashBlock {

/** 畫面上的座標 */
			int x, y;
/** 飛散速度 */
			int vx, vy;
/** 圖像編號 */
			int grpNo;


/** 建構子，隨機速度的初始化 */
			CrashBlock(int x, int y, int grpNo) {
				this.x = x;
				this.y = y;
				this.grpNo = grpNo;
				vx = (int)(Math.random() * 33.) - 16;
				vy = -(int)(Math.random() * 16.);
			}
		}



/** 建構子 */
		public KunerisMain() {
			int i, j;

			crashBlockList = new CrashBlockList();

			tailX = new int[8];
			tailY = new int[8];

// 建立降落方塊資料
			blockDatas = new BlockDataVector[6];
			for(i = 0; i < 6; i++)
				blockDatas[i] = new BlockDataVector();
			blockDatas[0].addElement(new BlockData(1, 2, 3, blockChips[1]));
			blockDatas[0].addElement(new BlockData(2, 3, 2, blockChips[2]));
			blockDatas[0].addElement(new BlockData(3, 2, 3, blockChips[3]));
			blockDatas[0].addElement(new BlockData(4, 3, 2, blockChips[4]));
			blockDatas[1].addElement(new BlockData(6, 2, 3, blockChips[6]));
			blockDatas[1].addElement(new BlockData(7, 3, 2, blockChips[7]));
			blockDatas[1].addElement(new BlockData(8, 2, 3, blockChips[8]));
			blockDatas[1].addElement(new BlockData(9, 3, 2, blockChips[9]));
			blockDatas[2].addElement(new BlockData(11, 2, 3, blockChips[11]));
			blockDatas[2].addElement(new BlockData(12, 3, 2, blockChips[12]));
			blockDatas[2].addElement(new BlockData(13, 2, 3, blockChips[13]));
			blockDatas[2].addElement(new BlockData(14, 3, 2, blockChips[14]));
			blockDatas[3].addElement(new BlockData(16, 2, 3, blockChips[16]));
			blockDatas[3].addElement(new BlockData(17, 3, 2, blockChips[17]));
			blockDatas[4].addElement(new BlockData(19, 2, 3, blockChips[19]));
			blockDatas[4].addElement(new BlockData(20, 3, 2, blockChips[20]));
			blockDatas[5].addElement(new BlockData(22, 1, 1, blockChips[22]));
// 建立方塊堆積的資料
			fieldMap = new int[10][17];
			for(i = 1; i < 9; i++)
				for(j = 0; j < 16; j++)
					fieldMap[i][j] = 0;
			for(i = 0; i < 10; i++)
				fieldMap[i][16] = 32;
			for(i = 0; i < 17; i++) {
				fieldMap[0][i] = 32;
				fieldMap[9][i] = 32;
			}
// 讀取圖像
			for(i = 0; i < 24; i++)
				sprite.addGrp(i, "grp"+(i/10)+(i%10)+GRP_EXTENSION);
			sprite.waitLoad();
// 設定背景圖像
			sprite.setPlaneGrp(0, 0, 0);
// 方塊顯示的準備
			getBlock();
			sprite.setPlanePos(140, x * 24 + 8, y * 24 + 8);
// 顯示文字的準備
			sprite.setPlaneString(150, "NextBlock");
			sprite.setPlanePos(150, 214, 9);

			sprite.setPlaneString(151, "HiScore");
			sprite.setPlanePos(151, 216, 110);
			sprite.setPlaneString(152, scoreString(7, hiScore));
			sprite.setPlanePos(152, 216, 128);

			sprite.setPlaneString(153, "Score");
			sprite.setPlanePos(153, 216, 160);
			sprite.setPlaneString(154, scoreString(7, score));
			sprite.setPlanePos(154, 216, 178);

			sprite.setPlaneString(155, "Last");
			sprite.setPlanePos(155, 216, 210);
			sprite.setPlaneString(156, Integer.toString(last));
			sprite.setPlanePos(156, 248, 228);
// BGM與SE
			sp.addBgm(1, "09.au");
			sp.addSe(1, "01_s01.au");
			sp.addSe(2, "02_s02.au");
			sp.addSe(3, "03_s03.au");
			sp.addSe(4, "04_t01.au");
			sp.addSe(5, "05_t02.au");
			sp.addSe(6, "06_t03.au");
			sp.addSe(7, "07_t04.au");
			sp.addSe(8, "08_t05.au");
			sp.addSe(9, "09_e01.au");
			sp.addSe(10, "10_e02.au");
			sp.addSe(11, "11_e03.au");
			sp.playBgm(1);
		}


/** main loop主迴圈。按照模式來改變處理 */
		public boolean mainLoop() {
			boolean ret = true;

			switch(mode) {
				case 0:
					ret = titleMode();
					break;
				case 1:
					ret = gameMode();
					break;
				case -1:
					ret = gameOverMode();
					break;
			}
// 與模式(mode)不相關，且有飛消的單位矩形時進行處理
			crashBlockList.crashBlockEffect();
			return ret;
		}



/** 顯示標題 */
		boolean titleMode() {
			InputEventTiny ket;
			int i, j;

			sprite.setPlaneString(160, "Kuneris");
			sprite.setPlaneFont(160, null, -1, 55);
// 讓在main loop時每次顏色慢慢變化
			sprite.setPlaneColor(160, 255 - (int)(loopCount & 0xff),
				255 - (int)((loopCount & 0xff00) >> 8),
				255 - (int)((loopCount & 0xff0000) >> 16));
			sprite.setPlanePos(160, 10, 150);

			sprite.setPlaneString(161, "Push Space Key to Start");
			sprite.setPlaneFont(161, null, -1, 17);
			sprite.setPlaneColor(161, 255, 255, 255);
			sprite.setPlanePos(161, 14, 230);

			loopCount *= 2;
			loopCount &= 0xffffff;
			if(loopCount == 0)
				loopCount = 1;

			mouseQ.removeAllElements();
// 按下空白鍵時準備以mode為1的模式進行
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED
					|| ket.getKeyCode() != KeyEvent.VK_SPACE)
					continue;

				sprite.setPlaneView(160, false);
				sprite.setPlaneView(161, false);
				mode = 1;

				sp.playSe(1);

				loopCount = 0;
				score = 0;
				last = 2;
				nowBlockNo = -1;
				nextBlockNo = -1;

				for(i = 1; i < 9; i++)
					for(j = 0; j < 16; j++)
						fieldMap[i][j] = 0;

				getBlock();
				buildBrocks();
				sprite.setPlanePos(140, x * 24 + 8, y * 24 + 8);

				sprite.setPlaneString(154, scoreString(7, score));
				sprite.setPlanePos(154, 216, 178);

				sprite.setPlaneString(156, Integer.toString(last));
				sprite.setPlanePos(156, 248, 228);
			}
			return true;
		}


/** 遊戲中 */
		boolean gameMode() {
// 按鍵輸入對應和移動處理
			InputEventTiny ket;
			int i;

			if(loopCount % (20 - loopCount / 100) == 0) {
				if(dropBlock() == false) {
					mode = -1;
					loopCount = 1;
				}
				if(loopCount == 1900)
					loopCount--;
			}
			loopCount++;
// 取消滑鼠游標(不使用)
			mouseQ.removeAllElements();
// 按鍵輸入處理
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED)
					continue;
				switch(ket.getKeyCode()) {
// 使用者按下的話降落1格
					case KeyEvent.VK_DOWN:
						if(dropBlock() == false) {
							mode = -1;
							loopCount = 1;
						}
						break;
// 按上的話旋轉
					case KeyEvent.VK_UP:
						if(nowBlockNo == 5)
							break;
						nowBlock = blockDatas[nowBlockNo].getData();
						if(hitBlockMap()) {
							blockDatas[nowBlockNo].rollBack();
							nowBlock = blockDatas[nowBlockNo].getData();
						} else {
							sprite.setPlaneGrp(140, 0, nowBlock.grpNo);
							sp.playSe(5);
						}
						break;
// 左右移動
					case KeyEvent.VK_RIGHT:
						x++;
						if(hitBlockMap())
							x--;
						else if(nowBlockNo == 5) {
							for(i = 7; i > 0; i--) {
								tailX[i] = tailX[i - 1];
								tailY[i] = tailY[i - 1];
							}
							tailX[0] = x;
							tailY[0] = y;
						}
						break;
					case KeyEvent.VK_LEFT:
							x--;
						if(hitBlockMap())
							x++;
						else if(nowBlockNo == 5) {
							for(i = 7; i > 0; i--) {
								tailX[i] = tailX[i - 1];
								tailY[i] = tailY[i - 1];
							}
							tailX[0] = x;
							tailY[0] = y;
						}
						break;
				}
			}
// 操作反應在方塊繪出
			sprite.setPlanePos(140, x * 24 + 8, y * 24 + 8);
			if(nowBlockNo == 5)
				for(i = 1; i < 8; i++)
					sprite.setPlanePos(140 + i, tailX[i] * 24 + 8,
						tailY[i] * 24 + 8);
			return true;
		}


/** GameOver模式 */
		boolean gameOverMode() {
			InputEventTiny ket;
			int i, j;

			sprite.setPlaneString(160, "Game Over");
			sprite.setPlaneFont(160, null, -1, 35);
			sprite.setPlaneColor(160, (int)(loopCount & 0xff),
				(int)((loopCount & 0xff00) >> 8),
				(int)((loopCount & 0xff0000) >> 16));
			sprite.setPlanePos(160, 14, 150);

			loopCount *= 2;
			loopCount &= 0xffffff;
			if(loopCount == 0)
				loopCount = 1;

			mouseQ.removeAllElements();
// 按下空白鍵時準備以mode為0的模式進行
			while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
				if(ket.getID() != KeyEvent.KEY_PRESSED
					|| ket.getKeyCode() != KeyEvent.VK_SPACE)
					continue;

				sprite.setPlaneView(160, false);
				sprite.setPlaneView(161, false);
				mode = 0;
				loopCount = 1;
			}
			return true;
		}


/** 取得降落方塊 */
		void getBlock() {
			int i, j;
			i = (int)(Math.random() * 5.);
			if(nextBlockNo < 0) {
				nextBlockNo = i;
				i = (int)(Math.random() * 5.);
			}
			nowBlockNo = nextBlockNo;
			nextBlockNo = i;

			if((int)(Math.random() * 6.) == 0) {
				nextBlockNo = 5;
				for(i = 0; i < 2; i++)
					for(j = 0; j < 3; j++) {
						sprite.setPlaneGrp(130 + i * 3 + j, 0,
							((BlockData)blockDatas[nextBlockNo]
							.elementAt(0)).grpNo);
						sprite.setPlanePos(130+i*3+j, 226+i*24, 26+j*24);
					}
			} else {
				sprite.setPlaneGrp(130, 0,
					((BlockData)blockDatas[nextBlockNo].elementAt(0)).grpNo);
				sprite.setPlanePos(130, 226, 26);
				for(i = 1; i < 6; i++)
					sprite.setPlaneView(130 + i, false);
			}

			nowBlock = (BlockData)blockDatas[nowBlockNo].elementAt(0);
			blockDatas[nowBlockNo].clearIndex();
			sprite.setPlaneGrp(140, 0, blockDatas[nowBlockNo].getData().grpNo);
			x = 3;
			y = 0;

			if(nowBlockNo == 5) {
				tailX[0] = 3;
				tailY[0] = 0;
				for(i = 1; i < 8; i++) {
					sprite.setPlaneGrp(140 + i, 0, nowBlock.grpNo);
					tailX[i] = 3;
					tailY[i] = 0;
					sprite.setPlanePos(140 + i, tailX[i], tailY[i]);
				}
				sp.playSe(6);
			}
			else
				for(i = 1; i < 8; i++)
					sprite.setPlaneView(140 + i, false);
		}


/** 檢查現在操作的方塊是否與現存方塊重疊 */
		boolean hitBlockMap() {
			int i, j;
			for(i = 0; i < nowBlock.width; i++)
				for(j = 0; j < nowBlock.height; j++)
					if(fieldMap[x + i + 1][y + j] != 0
						&& nowBlock.chips[i][j] != 0)
						return true;
			return false;
		}


/** 堆積方塊的繪圖準備 */
		void buildBrocks() {
			int i, j;
			for(i = 0; i < 8; i++)
				for(j = 0; j < 16; j++)
					if(fieldMap[i + 1][j] != 0) {
						sprite.setPlaneGrp(1+j*8+i, 0, fieldMap[i + 1][j]);
						sprite.setPlanePos(1+j*8+i, 8 + i * 24, 8 + j * 24);
					} else
						sprite.setPlaneView(1+j*8+i, false);
		}


/** 方塊逐一格漸向下移動的處理 */
		boolean dropBlock() {
			int i, j, k, top, bottom, scoreValue = 0, bounasValue = 0;
			y++;
// 判斷降落是否碰到堆積起來的部份
			if(hitBlockMap()) {
				y--;
// 堆積方塊
				if(nowBlockNo == 5) {
					tailX[0] = x;
					tailY[0] = y;
					for(i = 0; i < 8; i++)
						fieldMap[tailX[i]+1][tailY[i]] = nowBlock.chips[0][0];
					top = tailY[7];
					bottom = y + 1;
					for(i = 0; i < 6; i++)
						if(tailX[i] != tailX[i + 2]
							&& tailY[i] != tailY[i + 2])
							bounasValue++;
				} else {
					for(i = 0; i < nowBlock.width; i++)
						for(j = 0; j < nowBlock.height; j++)
							if(nowBlock.chips[i][j] != 0)
								fieldMap[x+i+1][y+j] = nowBlock.chips[i][j];
					top = y;
					bottom = y + nowBlock.height;
				}
// 檢查有沒有應該刪除的橫行
				for(j = top; j < bottom; j++) {
					for(i = 1; i < 9; i++)
						if(fieldMap[i][j] == 0)
							break;
					if(i == 9) {
						crashBlockList.setCrash(j);
						for(k = j; k > 0; k--)
							for(i = 1; i < 9; i++)
								fieldMap[i][k] = fieldMap[i][k - 1];
						for(i = 1; i < 9; i++)
							fieldMap[i][0] = 0;
						scoreValue++;
					}
				}
// 算分
				if(scoreValue != 0) {
					score += Math.pow(2, scoreValue + bounasValue - 1);
					score += crashBlockList.crashBlockCount();
					sprite.setPlaneString(154, scoreString(7, score));
					sprite.setPlanePos(154, 216, 178);
					sp.playSe(7);
				}
// 準備下個降落方塊
				buildBrocks();
				getBlock();
// 判斷方塊是否落到堆積上
				if(hitBlockMap()) {
					last--;
					loopCount = 0;

					for(i = 0; i < 16; i++)
						crashBlockList.setCrash(i);

					for(i = 1; i < 9; i++)
						for(j = 0; j < 16; j++)
							fieldMap[i][j] = 0;

					buildBrocks();
// 堆到最上面3次就Gameover
					if(last < 0) {
						if(score > hiScore) {
							hiScore = score;
							sprite.setPlaneString(152,scoreString(7,hiScore));
							sprite.setPlanePos(152, 216, 128);
							sprite.setPlaneString(161, "!! Top Score !!");
							sprite.setPlaneFont(161, null, -1, 19);
							sprite.setPlaneColor(161, 255, 255, 255);
							sprite.setPlanePos(161, 40, 230);
							sp.playSe(9);
						} else
							sp.playSe(11);
						return false;
					} else {
						sprite.setPlaneString(156, Integer.toString(last));
						sprite.setPlanePos(156, 248, 228);
						sp.playSe(11);
					}

					getBlock();

				}
// 扭動方塊下降
			} else if(nowBlockNo == 5) {
				for(i = 7; i > 0; i--) {
					tailX[i] = tailX[i - 1];
					tailY[i] = tailY[i - 1];
				}
				tailX[0] = x;
				tailY[0] = y;
			}
			return true;
		}


/** 建立顯示分數的文字 */
		String scoreString(int w, int c) {
			String ret;
			ret = Integer.toString(c);
			while(ret.length() < w)
				ret = "0" + ret;
			return ret;
		
		}
	}
}

