// AudioApp3.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;

public class AudioApp3 extends Frame {

    static final int CANVAS_SIZE = 256;

    Image img, img2;
    int x = 0, y = 0;
    SoundPalette soundPalette;

    public static void main(String args[]) {
        new AudioApp3();
    }

    public AudioApp3() {
        super("AudioApp3");
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
        soundPalette = new SoundPalette(null);
// 登錄BGM、SE
        soundPalette.addBgm(0, "09.au");
        soundPalette.addSe(0, "05_t02.au");
// 播放BGM
        soundPalette.playBgm(0);

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
// 播放SE
            soundPalette.playSe(0);
        }
    }

    public void paint(Graphics g) {
        g.drawImage(img, getInsets().left, getInsets().top, this);
        g.drawImage(img2, x, y, this);
    }
}

