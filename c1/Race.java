// Race.java
// written by mnagaku

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Race extends Applet implements Runnable {

    static int x = 19;
    TextAreaEx ta;

    class TextAreaEx extends TextArea {

        public TextAreaEx(int w, int h) {
            super("", h, w, TextArea.SCROLLBARS_NONE);
            enableEvents(AWTEvent.KEY_EVENT_MASK);
            setEditable(false);
        }

        public void processKeyEvent(KeyEvent e) {
            if(e.getID() != KeyEvent.KEY_PRESSED)
                return;
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(x > 0)
                        x--;
                    break;
                case KeyEvent.VK_RIGHT:
                    if(x < 38)
                        x++;
                    break;
            }
        }
    }

    public void init() {
        ta = new TextAreaEx(35, 10);
        add(ta);
        new Thread(this).start();
        ta.requestFocus();
    }

    public void run() {
        int i, kabe = 14, speed = 200, point = 0;
        String str;

// 主迴圈
        while(true) {

// 撞到牆壁就結束
            if(x <= kabe || x >= kabe + 10) {
                ta.insert("Boom!\n", 0);
                ta.insert("point : " + point + "\n", 0);
                break;
            }

// 如果沒有撞到牆壁，就顯示出狀況
            else {
                str = "";
                for(i = 0; i < 40; i++) {
                    if(i == kabe || i == kabe + 10)
                        str += "#";
                    else if(i == x)
                        str += "*";
                    else
                        str += " ";
                }
                str += "\n";
                ta.insert(str, 0);
            }

// 隨機左右移動牆壁，製作出道路
            if((int)(Math.random() * 2.) % 2 == 0) {
                if(kabe > 0)
                    kabe--;
            }
            else if(kabe + 10 < 38)
                kabe++;

            speed--;
            point++;

// wait
            try {
                Thread.sleep(speed);
            } catch(Exception e) {}
        }
    }
}
