// Draw.java
// written by mnagaku

import java.awt.*;

/**
 * �ΨӦbSprite�Wø�Ϫ������C<br>
 * @author mnagaku
*/
interface Draw {

/**
 * ��@ø�Ϫ��ʧ@�C
 * ��@Sprite��ø�ϸ�T�C
 * @param g ø�ϭ�
 * @param plane ����
 * @return ø�Ϧ��\�ɶǦ^true
*/
	public boolean drawing(Graphics g, Plane plane);
}
