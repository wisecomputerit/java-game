// ScrollSpace.java
// written by mnagaku

import java.awt.*;

/**
 * ScrollSpace���O<br>
 * �y�e�I���α��ʪ��P�šC
 * @author mnagaku
 */
class ScrollSpace implements Draw {

/** �O���P�P����m */
	int dot[] = new int[40];
/** �O���P�P�����ʪ��A */
	int count = 0;
/** ø�e�������j�p */
	int w, h;


/**
 * �غc�l
 * ø�Ϸǳ�
 * @param w ø�ϰϰ쪺�e
 * @param h ø�ϰϰ쪺��
 */
	ScrollSpace(int w, int h) {
		this.w = w;
		this.h = h;
		for(int i = 0; i < 40; i++) {
			dot[i] = (int)(Math.random() * w);
		}
	}


/**
 * ø��
 * ��@ø�Ϫ��ʧ@�C��@Sprite��ø�ϸ�T�C
 * @param g ø�ϭ�
 * @param pln ����
 */
	public boolean drawing(Graphics g, Plane pln) {
		dot[count] = (int)(Math.random() * w);
		count++;
		count %= 40;

		g.setColor(Color.black);
		g.fillRect(0, 0, w, h);

		g.setColor(Color.white);
		for(int i = 0; i < 40; i++, count = (count + 1) % 40) {
			int size = dot[count] % 3;
			g.drawLine(dot[count] - size, h - (i * 10),
				dot[count] + size, h - (i * 10));
			g.drawLine(dot[count], h - (i * 10) - size,
				dot[count], h - (i * 10) + size);
		}

		return true;
	}
}
