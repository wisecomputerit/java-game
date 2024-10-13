// DrawRanking.java
// written by mnagaku

import java.awt.*;

/**
 * DrawRanking���O<br>
 * �yø�Ʀ�]
 * @author mnagaku
 */
class DrawRanking implements Draw {
	int x = 70;
	int y_base = 70;
	int y_offset = 13;

	String names[] = null;

	DrawRanking() {}


/**
 * �]�w�Ʀ�]
 * �]�w�Ʀ�]����ܦr�ꪺ�yø
 * @param names �Ʀ�]����ܦr��
 */
	void setRanking(String[] names) {
		this.names = names;
	}


/**
 * �yø
 * �O�s�򥻸�T�A�yø�Ʀ�]
 * @param g ø�ϭ�
 * @param pln ����
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
