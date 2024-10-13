// DrawRect.java
// written by mnagaku

import java.awt.*;

/**
 * DrawRect類別<br>
 * 繪製四方形
 * @author mnagaku
 */
class DrawRect implements Draw {

/** 四方形的大小 */
	int w, h;
/** 四方形的顏色 */
	Color color;


/**
 * 建構子
 * 繪圖準備
 * @param w 四方形的寬
 * @param h 四方形的高
 * @param color >四方形的顏色
 */
	DrawRect(int w, int h, Color color) {
		this.w = w;
		this.h = h;
		this.color = color;
	}


/**
 * 繪圖
 * 按照存放的資訊，在平面的位置繪製四角形。
 * @param g 繪圖面
 * @param pln 平面
 */
	public boolean drawing(Graphics g, Plane pln) {
		g.setColor(color);
		g.fillRect(pln.posX, pln.posY, w, h);
		return true;
	}


/**
 * 存放較暗的顏色
 */
	public void darker() {
		color = color.darker();
	}
}
