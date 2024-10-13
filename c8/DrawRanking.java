// DrawRanking.java
// written by mnagaku

import java.awt.*;

/**
 * DrawRanking類別<br>
 * 描繪排行榜
 * @author mnagaku
 */
class DrawRanking implements Draw {
	int x = 70;
	int y_base = 70;
	int y_offset = 13;

	String names[] = null;

	DrawRanking() {}


/**
 * 設定排行榜
 * 設定排行榜內顯示字串的描繪
 * @param names 排行榜內顯示字串
 */
	void setRanking(String[] names) {
		this.names = names;
	}


/**
 * 描繪
 * 保存基本資訊，描繪排行榜
 * @param g 繪圖面
 * @param pln 平面
 */
	public boolean drawing(Graphics g, Plane pln) {
		if(names == null)
			return true;
		g.setColor(Color.white);
		g.setFont(new Font("Monospaced", Font.PLAIN, 12));
		for(int i = 0; i < names.length; i++)
			g.drawString(names[i], x, y_base + i * y_offset);
		return true;
	}
}
