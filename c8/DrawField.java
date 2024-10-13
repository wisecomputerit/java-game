// DrawField.java
// written by mnagaku

import java.awt.*;

/**
 * DrawField���O<br>
 * �սu�yø
 * @author mnagaku
 */
class DrawField implements Draw {

/** �yø�d�򪺤j�p */
	int w, h;


/**
 * �غc�l
 * �yø�ǳ�
 * @param w ø�Ͻd�򪺼e
 * @param h ø�Ͻd�򪺰�
 */
	DrawField(int w, int h) {
		this.w = w;
		this.h = h;
	}


/**
 * �yø
 * �O�s�򥻸�T�A�yø�սu�خ�
 * @param g ø�ϭ�
 * @param pln ����
 */
	public boolean drawing(Graphics g, Plane pln) {
		g.setColor(Color.white);
		g.drawLine(50, 0, 50, 400);
		g.drawLine(250, 0, 250, 400);
		g.drawLine(0, 50, 300, 50);
		g.drawLine(0, 350, 300, 350);
		for(int i = 0; i < 3; i++) {
			g.drawLine(100 + i * 50, 0, 100 + i * 50, 50);
			g.drawLine(100 + i * 50, 350, 100 + i * 50, 400);
		}
		for(int i = 0; i < 5; i++) {
			g.drawLine(0, 100 + i * 50, 50, 100 + i * 50);
			g.drawLine(250, 100 + i * 50, 300, 100 + i * 50);
		}
		return true;
	}
}
