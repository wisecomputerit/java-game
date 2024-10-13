// PenApp.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;

public class PenApp extends Frame {

    static final int CANVAS_SIZE = 256; // 視窗內部繪圖區域的大小

    Image img, img2;
    int x = 0, y = 0;

    public static void main(String args[]) {
        new PenApp();
    }

    public PenApp() {
// 將視窗的標題設定為「PenApp」
        super("PenApp");
// 確定視窗的外觀
        pack();
        setVisible(true);
        setVisible(false);
        pack();
// 使視窗不能調整大小，在這裡再次確定Windows的外框寬度
        setResizable(false);
        pack();
// 取得畫面的尺寸，將視窗顯示在螢幕中央
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - CANVAS_SIZE) / 2, (d.height - CANVAS_SIZE) / 2);
// 將繪圖區域設定為CANVAS_SIZExCANVAS_SIZE
        setSize(CANVAS_SIZE + getInsets().left + getInsets().right,
            CANVAS_SIZE + getInsets().top + getInsets().bottom);
// 設定視窗關閉後結束程式
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

// 顯示視窗
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