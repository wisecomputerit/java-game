// AudioApp2.java
// written by mnagaku

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class AudioApp2 extends Frame {

    static final int CANVAS_SIZE = 256;

    Image img, img2;
    int x = 0, y = 0;
    AudioClip bgm, se;

    public static void main(String args[]) {
        new AudioApp2();
    }

    public AudioApp2() {
        super("AudioApp2");
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

// 播放聲音的準備
        bgm = new GameAudioClip("09.au");
        se = new GameAudioClip("05_t02.au");

        bgm.loop();
        img = Toolkit.getDefaultToolkit().getImage
            (getClass().getResource("penguin.gif"));
        img2 = Toolkit.getDefaultToolkit().getImage
            (getClass().getResource("kuwa.gif"));
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        x = getInsets().left;
        y = getInsets().top;
        setVisible(true);
    }

    public void processMouseEvent(MouseEvent e) {
        if(e.getID() == MouseEvent.MOUSE_PRESSED) {
            x = e.getX();
            y = e.getY();
            repaint();
            se.play();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(img, getInsets().left, getInsets().top, this);
        g.drawImage(img2, x, y, this);
    }
}

