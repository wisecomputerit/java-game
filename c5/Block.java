// Block.java
// written by mnagaku

import java.awt.event.*;
import java.awt.*;

/**
 * Block類別<br>
 * 實作方塊降落的處理
 * @author mnagaku
 */
public class Block extends Game2D {


/**
 * 建構子。
 * 設定畫面尺寸、主迴圈速度與按鍵重複輸入間隔。
 */
    public Block() {
        CANVAS_SIZE_W = 200;
        CANVAS_SIZE_H = 400;
        SPEED = 80;
        KEY_SPEED = 40;
        KEY_DELAY = 3;
    }


/**
 * 設定以應用程式形式執行時視窗預設顯示的位置。
 * 呼叫Game2D類別的startGame()。
 */
    public static void main(String args[]) {
        startGame("Block");
    }



/**
 * BlockMain類別<br>
 * 遊戲本體的處理。
 * @author mnagaku
 */
    public class BlockMain extends Game2DMain {

/** 產生新方塊的旗標。為true時產生新的方塊 */
        boolean newBlock = true;
/** 表示目前產生了多少個方塊 */
        int blockCount = 1;
/** 計算方塊降落一格所花的時間 */
        int blockWait = 0;
/** 表示方塊的堆積狀況 */
        int map[][] = new int[10][17];
/** 表示畫出方塊使用的顏色 */
        Color colorList[] = {Color.red, Color.green, Color.blue};
/**
 * 用來繪製方塊之實作了Draw介面的DrawRect類別的實體
 */
        DrawRect nowBlock;


/**
 * 建構子。
 * 初始化map並設定背景。
 */
        public BlockMain() {
// 初始化用來管理block堆積情形的map[][]
            for(int i = 0; i < 10; i++)
                for(int j = 0; j < 17; j++)
                    map[i][j] = 1;
            for(int i = 1; i < 9; i++)
                for(int j = 0; j < 16; j++)
                    map[i][j] = 0;
// 設定背景
            Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
            sprite.setPlaneDraw(0, back);
        }


/**
 * 主迴圈一圈的處理。
 */
        public boolean mainLoop() {
// 忽略滑鼠事件
            mouseQ.removeAllElements();
// 建立新的block
            if(newBlock) {
                sprite.setPlaneDraw(blockCount, nowBlock = new DrawRect(25, 25,
                    colorList[(int)(Math.random() * 3)]));
                sprite.setPlanePos(blockCount,
                    (int)(Math.random() * 8) * 25, 0);
                newBlock = false;
                blockWait = 0;
            }
// 每10次mainLoop()10降落1格
            blockWait++;
            if(blockWait % 10 == 0) {
                if(map[sprite.getPlanePosX(blockCount)/25+1]
                    [sprite.getPlanePosY(blockCount)/25+1] != 0) {
                    map[sprite.getPlanePosX(blockCount)/25+1]
                        [sprite.getPlanePosY(blockCount)/25] = 1;
                    nowBlock.darker();
                    newBlock = true;
                    blockCount++;
                    keyQ.removeAllElements();
                    return true;
                }
                sprite.setPlaneMov(blockCount, 0, 25);
            }
// 按鍵輸入處理
            InputEventTiny ket;
            while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
                if(ket.getID() != KeyEvent.KEY_PRESSED)
                    continue;
                switch(ket.getKeyCode()) {
// 使用者按下的話降落1格
                    case KeyEvent.VK_DOWN:
                    if(map[sprite.getPlanePosX(blockCount)/25+1]
                        [sprite.getPlanePosY(blockCount)/25+1] == 0)
                        sprite.setPlaneMov(blockCount, 0, 25);
                        break;
                    case KeyEvent.VK_UP:
                        break;
// 左右移動
                    case KeyEvent.VK_RIGHT:
                        if(map[sprite.getPlanePosX(blockCount)/25+2]
                            [sprite.getPlanePosY(blockCount)/25] == 0)
                            sprite.setPlaneMov(blockCount, 25, 0);
                        break;
                    case KeyEvent.VK_LEFT:
                        if(map[sprite.getPlanePosX(blockCount)/25]
                            [sprite.getPlanePosY(blockCount)/25] == 0)
                            sprite.setPlaneMov(blockCount, -25, 0);
                        break;
                }
            }
            return true;
        }
    }
}
