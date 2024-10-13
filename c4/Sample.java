// Sample.java
// written by mnagaku

import java.awt.event.*;

/**
 * Sample���O<br>
 * Sample���O�ϥΤFGame2D���O
 * @author mnagaku
 */
public class Sample extends Game2D {


/**
 * �غc�l
 * �]�w�e���j�p�B��ø�B�D�j��B�z���t�סB��V�䪺���䭫�ƿ�J�t�שM����ɶ�
 */
	public Sample() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 80;
		KEY_SPEED = 40;
		KEY_DELAY = 3;
	}


/**
 * �{���}�l����m
 * �I�sGame2D���O��startGame()
 */
	public static void main(String args[]) {
		startGame("Sample");
	}



/**
 * SampleMain���O<br>
 * �C�����骺�B�z
 * @author mnagaku
 */
	public class SampleMain extends Game2DMain {

/** �e���ɪ����ɦW */
		static final String GRP_EXTENSION = ".gif";


/**
 * �غc�l
 * Ū�J���ĩM����
 */
		public SampleMain() {
// Ū��GRP
			sprite.addGrp(1, "own" + GRP_EXTENSION);
			sprite.addGrp(2, "benemy2" + GRP_EXTENSION);
			sprite.waitLoad();
// ���GRP
			sprite.setPlaneGrp(1, 0, 1);
			sprite.setPlanePos(1, 100, 300);
			sprite.setPlaneGrp(2, 0, 2);
			sprite.setPlanePos(2, 200, 100);
// �I��
			Draw back = new ScrollSpace(CANVAS_SIZE_W, CANVAS_SIZE_H);
			sprite.setPlaneDraw(0, back);
// ��ܦr��
			sprite.setPlaneString(3, "string");
			sprite.setPlanePos(3, 100, 200);
			sprite.setPlaneColor(3, 255, 255, 255);
// BGM
			sp.addBgm(1, "09.au");
			sp.playBgm(1);
		}


/**
 * �D�j��@������B�z
 * �B�z��СA���s����J�A���ܦr�ꪺø�Ϧ�m�C
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
