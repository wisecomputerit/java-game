// Ripple.java
// written by mnagaku

import java.awt.*;

/**
 * Ripple類別<br>
 * 40個波紋的描繪
 * @author mnagaku
 */
class Ripple implements Draw {

/**
 * RippleData類別<br>
 * 存放波紋的資料
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
 * 建構子
 * 準備描繪
 * @param w 繪圖範圍的寬
 * @param h 繪圖範圍的高
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
 * 描繪。
 * 保存基本資訊，描繪40個波紋
 * @param g 繪圖面
 * @param pln 平面
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
