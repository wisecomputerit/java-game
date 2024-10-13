// DrawRect.java
// written by mnagaku

import java.awt.*;

/**
 * DrawRect���O<br>
 * ø�s�|���
 * @author mnagaku
 */
class DrawRect implements Draw {

/** �|��Ϊ��j�p */
	int w, h;
/** �|��Ϊ��C�� */
	Color color;


/**
 * �غc�l
 * ø�Ϸǳ�
 * @param w �|��Ϊ��e
 * @param h �|��Ϊ���
 * @param color >�|��Ϊ��C��
 */
	DrawRect(int w, int h, Color color) {
		this.w = w;
		this.h = h;
		this.color = color;
	}


/**
 * ø��
 * ���Ӧs�񪺸�T�A�b��������mø�s�|���ΡC
 * @param g ø�ϭ�
 * @param pln ����
 */
	public boolean drawing(Graphics g, Plane pln) {
		g.setColor(color);
		g.fillRect(pln.posX, pln.posY, w, h);
		return true;
	}


/**
 * �s����t���C��
 */
	public void darker() {
		color = color.darker();
	}
}
