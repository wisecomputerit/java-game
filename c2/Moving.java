// Moving.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;
import java.math.*;

public class Moving extends Frame implements Runnable {

    static final int CANVAS_SIZE = 256;

    Image img, img2;
    int x, y, xv, yv, xa, ya;

    public static void main(String args[]) {
        new Thread(new Moving()).start();
    }

    public Moving() {
        super("Moving");
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

        img = Toolkit.getDefaultToolkit().getImage
            (getClass().getResource("penguin.gif"));
        img2 = Toolkit.getDefaultToolkit().getImage
            (getClass().getResource("kuwa.gif"));
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);

// kuwa.gif一開始的顯示位置
        x = 0;
        y = 0;
// kuwa.gif一開始的移動速度
        xv = 2;
        yv = 2;
// kuwa.gif移動速度控制變數的初始值
        xa = 1;
        ya = 1;

        setVisible(true);
    }

// 維持一樣的移動速度，將速度設定為向滑鼠點選的方向移動。
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
        }
    }

    public void paint(Graphics g) {
        g.drawImage(img, getInsets().left, getInsets().top, this);
        g.drawImage(img2, x + getInsets().left,
            y + getInsets().top, this);
    }

    public void run() {
        while(true) {
// 配合速度移動
            x += xv;
            y += yv;
// 移動到畫面的邊界時，則反轉移動的方向。並調整移動速度。
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
// 重繪
            repaint();
// 等待100ms
            try {
                Thread.sleep(100);
            } catch(Exception e) {}
        }
    }
}
