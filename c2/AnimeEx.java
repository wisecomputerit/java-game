// AnimeEx.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;
import java.math.*;

public class AnimeEx extends Anime {

    MediaTracker tracker;

    public static void main(String args[]) {
        new Thread(new AnimeEx()).start();
    }

    public AnimeEx() {
        setTitle("AnimeEx");
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
        img3 = new Image[4];
        int i;
        for(i = 0; i < 4; i++)
            img3[i] = Toolkit.getDefaultToolkit().getImage
                (getClass().getResource("num" + (i + 1) + ".gif"));

// 對MediaTracker登錄正在讀取的Image
        tracker = new MediaTracker(this);
        tracker.addImage(img, 1);
        tracker.addImage(img2, 1);
        for(i = 0; i < 4; i++)
            tracker.addImage(img3[i], 1);
// 等到讀取完畢為止
        try {
            tracker.waitForID(1);
        } catch(Exception e) {}

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);

        nx = 0;
        ny = 0;
        animeCount = 0;
        x = 0;
        y = 0;
        xv = 2;
        yv = 2;
        xa = 1;
        ya = 1;

        setVisible(true);
    }
}
