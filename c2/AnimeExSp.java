// AnimeExSp.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;
import java.math.*;

public class AnimeExSp extends Frame implements Runnable {

    static final int CANVAS_SIZE = 256;

    int x, y, xv, yv, xa, ya, nx, ny, sx, sy;
    Sprite sprite;

    public static void main(String args[]) {
        new Thread(new AnimeExSp()).start();
    }

    public AnimeExSp() {
        super("AnimeExSp");
        pack();
        setVisible(true);
        setVisible(false);
        pack();
        setResizable(false);
        pack();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - CANVAS_SIZE) / 2, (d.height - CANVAS_SIZE) / 2);
        setSize(CANVAS_SIZE + getInsets().left + getInsets().right,
            CANVAS_SIZE + getInsets().top + getInsets().bottom);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
// �إ�Sprite���O
        sprite = new Sprite(CANVAS_SIZE, CANVAS_SIZE, this);
// Ū���v��
        sprite.addGrp(0, "penguin.gif");
        sprite.addGrp(1, "kuwa.gif");
        int i;
        for(i = 0; i < 4; i++)
            sprite.addGrp(i + 2, "num" + (i + 1) + ".gif");
        sprite.waitLoad();
// �r�ꪺ��ܮy��
        sx = 0;
        sy = CANVAS_SIZE - 16;

        nx = 0;
        ny = 0;
        x = 0;
        y = 0;
        xv = 2;
        yv = 2;
        xa = 1;
        ya = 1;
// �N�I����b����0�W
        sprite.setPlaneGrp(0, 0, 0);
// �N�u�K�v��b����1�A�ó]�w��ܪ��y��
        sprite.setPlaneGrp(1, 0, 1);
        sprite.setPlanePos(1, x, y);
// �N�Ʀr���ʵe��b����2�A�}�l��ܰʵe�A�ó]�w��ܪ��y��
        for(i = 0; i < 4; i++)
            sprite.setPlaneGrp(2, i, i + 2);
        sprite.setPlaneAnime(2, true);
        sprite.setPlanePos(2, nx, ny);
// �N�r���b����3�A�H�զ⪺��r��ܡA�ó]�w��ܪ��y��
        sprite.setPlaneString(3, "�]�i�H��ܤ�r");
        sprite.setPlaneColor(3, 255, 255, 255);
        sprite.setPlanePos(3, sx, sy);

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setVisible(true);
    }

    public void processMouseEvent(MouseEvent e) {
        if(e.getID() == MouseEvent.MOUSE_PRESSED) {
            if(x > e.getX() - getInsets().left)
                xv = -Math.abs(xv);
            else
                xv = Math.abs(xv);
            if(y > e.getY() - getInsets().top)
                yv = -Math.abs(yv);
            else
                yv = Math.abs(yv);
            nx = e.getX() - getInsets().left;
            ny = e.getY() - getInsets().top;
// �]�w�Ʀr�v������ܦ�m
            sprite.setPlanePos(2, nx, ny);
        }
    }

    public void paint(Graphics g) {
// �e�USprite���O�i��ø�Ϫ��ʧ@
        sprite.paintScreen(g);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void run() {
        while(true) {
            x += xv;
            y += yv;
            if(x <= 0 || x + 32 >= CANVAS_SIZE - 1) {
                if(Math.abs(xv) == 2)
                    xa = 1;
                else if(Math.abs(xv) == 8)
                    xa = -1;
                xv *= -Math.pow(2, xa);
            }
            if(y <= 0 || y + 64 >= CANVAS_SIZE - 1) {
                if(Math.abs(yv) == 2)
                    ya = 1;
                else if(Math.abs(yv) == 8)
                    ya = -1;
                yv *= -Math.pow(2, ya);
            }
// �]�w�u�K�v����ܦ�m
            sprite.setPlanePos(1, x, y);
// �]�w�r�ꪺ��ܦ�m
            sx = (sy + y) / 2;
            sy = (sx + x) / 2;
            sprite.setPlanePos(3, sx, sy);

            repaint();
            try {
                Thread.sleep(100);
            } catch(Exception e) {}
        }
    }
}
