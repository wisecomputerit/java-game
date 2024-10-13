// InputEventTiny.java
// written by mnagaku

/**
 * InputEventTiny���O<br>
 * �s����L�B�ƹ������A�����O�C
 * @author mnagaku
 */
public class InputEventTiny {

/** �ʧ@�]�N��ƹ��ƥ�B��L�ƥ�ID�ȡ^ */
	int id;

/** �o�ͷƹ��ƥ�X�y�� */
	int x;

/** �o�ͷƹ��ƥ�Y�y�� */
	int y;

/** �o����L�ƥ󪺫���X */
	int keyCode;

/** �غc�l
 * �ƹ��ƥ��
*/
	InputEventTiny(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.keyCode = 0;
	}


/** �غc�l
 * ��L�ƥ��
*/
	InputEventTiny(int id, int keyCode) {
		this.id = id;
		this.x = 0;
		this.y = 0;
		this.keyCode = keyCode;
	}


/** ���oID�]�ʧ@�^ */
	public int getID() {
		return id;
	}


/** ���oX�y�� */
	public int getX() {
		return x;
	}


/** ���oY�y�� */
	public int getY() {
		return y;
	}


/** ���oKeyCode */
	public int getKeyCode() {
		return keyCode;
	}
}

