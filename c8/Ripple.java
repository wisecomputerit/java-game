// Ripple.java
// written by mnagaku

import java.awt.*;

/**
 * Ripple���O<br>
 * 40�Ӫi�����yø
 * @author mnagaku
 */
class Ripple implements Draw {

/**
 * RippleData���O<br>
 * �s��i�������
 * @author mnagaku
 */
	class RippleData {
		int x, y;
		Color color;

		RippleData(int x, int y, Color color) {
			this.x = x;
			this.y = y;
			this.color = color;
		}
	}

	RippleData dot[] = new RippleData[40];
	int count = 0, w, h;


/**
 * �غc�l
 * �ǳƴyø
 * @param w ø�Ͻd�򪺼e
 * @param h ø�Ͻd�򪺰�
 */
	Ripple(int w, int h) {
		this.w = w;
		this.h = h;
		for(int i = 0; i < 40; i++) {
			dot[i] = new RippleData((int)(Math.random() * w),
				(int)(Math.random() * h),
				new Color((int)(Math.random() * 255.),
				(int)(Math.random() * 255.),
				(int)(Math.random() * 255.)));
		}
	}


/**
 * �yø�C
 * �O�s�򥻸�T�A�yø40�Ӫi��
 * @param g ø�ϭ�
 * @param pln ����
 */
	public boolean drawing(Graphics g, Plane pln) {
		dot[count].x = (int)(Math.random() * w);
		dot[count].y = (int)(Math.random() * h);
		dot[count].color = new Color((int)(Math.random() * 255.),
			(int)(Math.random() * 255.),
			(int)(Math.random() * 255.));
		count++;
		count %= 40;

		g.setColor(Color.black);
		g.fillRect(0, 0, w, h);

		for(int i = 0; i < 40; i++, count = (count + 1) % 40) {
			if(i < 5)
				dot[count].color = dot[count].color.darker();

			g.setColor(dot[count].color);
			g.drawOval(dot[count].x - 40 + i, dot[count].y - 40 + i,
				(40 - i) * 2, (40 - i) * 2);
		}

		return true;
	}
}
