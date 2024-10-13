// DrawField.java
// written by mnagaku

import java.awt.*;

/**
 * DrawField類別<br>
 * 白線描繪
 * @author mnagaku
 */
class DrawField implements Draw {

/** 描繪範圍的大小 */
	int w, h;


/**
 * 建構子
 * 描繪準備
 * @param w 繪圖範圍的寬
 * @param h 繪圖範圍的高
 */
	DrawField(int w, int h) {
		this.w = w;
		this.h = h;
	}


/**
 * 描繪
 * 保存基本資訊，描繪白線框格
 * @param g 繪圖面
 * @param pln 平面
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
