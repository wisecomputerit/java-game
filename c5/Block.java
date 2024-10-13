// Block.java
// written by mnagaku

import java.awt.event.*;
import java.awt.*;

/**
 * Block���O<br>
 * ��@����������B�z
 * @author mnagaku
 */
public class Block extends Game2D {


/**
 * �غc�l�C
 * �]�w�e���ؤo�B�D�j��t�׻P���䭫�ƿ�J���j�C
 */
    public Block() {
        CANVAS_SIZE_W = 200;
        CANVAS_SIZE_H = 400;
        SPEED = 80;
        KEY_SPEED = 40;
        KEY_DELAY = 3;
    }


/**
 * �]�w�H���ε{���Φ�����ɵ����w�]��ܪ���m�C
 * �I�sGame2D���O��startGame()�C
 */
    public static void main(String args[]) {
        startGame("Block");
    }



/**
 * BlockMain���O<br>
 * �C�����骺�B�z�C
 * @author mnagaku
 */
    public class BlockMain extends Game2DMain {

/** ���ͷs������X�СC��true�ɲ��ͷs����� */
        boolean newBlock = true;
/** ��ܥثe���ͤF�h�֭Ӥ�� */
        int blockCount = 1;
/** �p���������@��Ҫ᪺�ɶ� */
        int blockWait = 0;
/** ��ܤ������n���p */
        int map[][] = new int[10][17];
/** ��ܵe�X����ϥΪ��C�� */
        Color colorList[] = {Color.red, Color.green, Color.blue};
/**
 * �Ψ�ø�s�������@�FDraw������DrawRect���O������
 */
        DrawRect nowBlock;


/**
 * �غc�l�C
 * ��l��map�ó]�w�I���C
 */
        public BlockMain() {
// ��l�ƥΨӺ޲zblock��n���Ϊ�map[][]
            for(int i = 0; i < 10; i++)
                for(int j = 0; j < 17; j++)
                    map[i][j] = 1;
            for(int i = 1; i < 9; i++)
                for(int j = 0; j < 16; j++)
                    map[i][j] = 0;
// �]�w�I��
            Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
            sprite.setPlaneDraw(0, back);
        }


/**
 * �D�j��@�骺�B�z�C
 */
        public boolean mainLoop() {
// �����ƹ��ƥ�
            mouseQ.removeAllElements();
// �إ߷s��block
            if(newBlock) {
                sprite.setPlaneDraw(blockCount, nowBlock = new DrawRect(25, 25,
                    colorList[(int)(Math.random() * 3)]));
                sprite.setPlanePos(blockCount,
                    (int)(Math.random() * 8) * 25, 0);
                newBlock = false;
                blockWait = 0;
            }
// �C10��mainLoop()10����1��
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
// �����J�B�z
            InputEventTiny ket;
            while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
                if(ket.getID() != KeyEvent.KEY_PRESSED)
                    continue;
                switch(ket.getKeyCode()) {
// �ϥΪ̫��U���ܭ���1��
                    case KeyEvent.VK_DOWN:
                    if(map[sprite.getPlanePosX(blockCount)/25+1]
                        [sprite.getPlanePosY(blockCount)/25+1] == 0)
                        sprite.setPlaneMov(blockCount, 0, 25);
                        break;
                    case KeyEvent.VK_UP:
                        break;
// ���k����
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
