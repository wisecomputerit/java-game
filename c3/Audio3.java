// Audio3.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Audio3 extends Applet {

    Image img, img2;
    int x = 0, y = 0;
    SoundPalette soundPalette;

    public void init() {

// �����n�����ǳơA�n����ۤv�OApplet
        soundPalette = new SoundPalette(this);
// �n��BGM�BSE
        soundPalette.addBgm(0, "09.au");
        soundPalette.addSe(0, "05_t02.au");
// ����BGM
        soundPalette.playBgm(0);

        img = getImage(getClass().getResource("penguin.gif"));
        img2 = getImage(getClass().getResource("kuwa.gif"));
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    public void processMouseEvent(MouseEvent e) {
        if(e.getID() == MouseEvent.MOUSE_PRESSED) {
            x = e.getX();
            y = e.getY();
            repaint();
// ����SE
            soundPalette.playSe(0);
        }
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
        g.drawImage(img2, x, y, this);
    }
}

