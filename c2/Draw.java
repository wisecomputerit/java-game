// Draw.java
// written by mnagaku

import java.awt.*;

/**
 * �X�v���C�g�ɕ`�揈�����s��Draw�C���^�[�t�F�C�X<br>
 * @author mnagaku
*/
interface Draw {

/**
 * �`�揈���������B
 * �v���[���̏����g�����`�揈������������B
 * @param g �`���
 * @param plane �v���[��
 * @return �`�悪����������true��Ԃ��悤�Ɏ�������
*/
	public boolean drawing(Graphics g, Plane plane);
}
