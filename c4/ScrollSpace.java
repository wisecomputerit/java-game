// ScrollSpace.java
// written by mnagaku

import java.awt.*;

/**
 * ScrollSpace類別<br>
 * 描畫背景用捲動的星空。
 * @author mnagaku
 */
class ScrollSpace implements Draw {

/** 記錄星星的位置 */
	int dot[] = new int[40];
/** 記錄星星的捲動狀態 */
	int count = 0;
/** 繪畫平面的大小 */
	int w, h;


/**
 * 建構子
 * 繪圖準備
 * @param w 繪圖區域的寬
 * @param h 繪圖區域的高
 */
	ScrollSpace(int w, int h) {
		this.w = w;
		this.h = h;
		for(int i = 0; i < 40; i++) {
			dot[i] = (int)(Math.random() * w);
		}
	}


/**
 * 繪圖
 * 實作繪圖的動作。實作Sprite的繪圖資訊。
 * @param g 繪圖面
 * @param pln 平面
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
