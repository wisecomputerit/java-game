// PenApp.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;

public class PenApp extends Frame {

    static final int CANVAS_SIZE = 256; // ��������ø�ϰϰ쪺�j�p

    Image img, img2;
    int x = 0, y = 0;

    public static void main(String args[]) {
        new PenApp();
    }

    public PenApp() {
// �N���������D�]�w���uPenApp�v
        super("PenApp");
// �T�w�������~�[
        pack();
        setVisible(true);
        setVisible(false);
        pack();
// �ϵ�������վ�j�p�A�b�o�̦A���T�wWindows���~�ؼe��
        setResizable(false);
        pack();
// ���o�e�����ؤo�A�N������ܦb�ù�����
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - CANVAS_SIZE) / 2, (d.height - CANVAS_SIZE) / 2);
// �Nø�ϰϰ�]�w��CANVAS_SIZExCANVAS_SIZE
        setSize(CANVAS_SIZE + getInsets().left + getInsets().right,
            CANVAS_SIZE + getInsets().top + getInsets().bottom);
// �]�w���������ᵲ���{��
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });

        img = Toolkit.getDefaultToolkit().getImage
            (getClass().getResource("penguin.gif"));
        img2 = Toolkit.getDefaultToolkit().getImage
            (getClass().getResource("kuwa.gif"));
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        x = getInsets().left;
        y = getInsets().top;

// ��ܵ���
        setVisible(true);
    }

    public void processMouseEvent(MouseEvent e) {
        if(e.getID() == MouseEvent.MOUSE_PRESSED) {
            x = e.getX();
            y = e.getY();
            repaint();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(img, getInsets().left, getInsets().top, this);
        g.drawImage(img2, x, y, this);
    }
}