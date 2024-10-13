// Sprite.java
// written by mnagaku

import java.util.*;
import java.awt.*;

/**
 * 實作Sprite（貼圖）功能的Sprite類別<br>
 * 這個類別可將影像視為零件，提供繪圖、動畫等功能。
 * 讀入的影像放置在影像池裡，可配合使用的需要放在適當的地方。
 * 除了影像，也可以顯示文字。
 * 文字與影像，都應放在Sprite平面上，
 * 將所有平面疊在一起，就是整個畫面
 * @author mnagaku
 */
public class Sprite {
/** Sprite平面的模式 */
    static final int NULL_MODE = 0, GRP_MODE = 1, STR_MODE = 2, DRW_MODE = 8,
        CENTER_STR_MODE = 6;
/** 繪圖對象畫面的大小 */
    int canvasWidth, canvasHeight;
/** 存放繪圖對象Container的變數 */
    Container owner;
/** 讀取影像存放的影像池 */
    Hashtable grp;
/** 緩衝區 */
    Image backGrp = null;
/** 用來監視影像的讀取狀態 */
    MediaTracker tracker;
/** Sprite平面 */
    Hashtable planes;
/** 繪製平面的順序 */
    Integer spriteList[];


/**
 * 建構子。
 * 建立影像池、Sprite平面，並儲存畫面相關資訊。
 * @param canvasWidth 繪圖對象畫面的寬度
 * @param canvasHeight 繪圖對象畫面的高度
 * @param owner 繪圖對象的Container
*/
    public Sprite(int canvasWidth, int canvasHeight, Container owner) {
        int i, j;
// 建立影像池
        grp = new Hashtable();
// 建立Sprite平面
        planes = new Hashtable();
// 儲存繪圖平面的資訊
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.owner = owner;
// 建立用來管理影像讀取狀態的MediaTracker
        tracker = new MediaTracker(owner);
    }


/**
 * 顯示錯誤、警告。
*/
    void infomation(String info, Exception e) {
        System.out.println(info);
        System.out.println("java.version : "
            + System.getProperty("java.version"));
        System.out.println("java.vendor : "
            + System.getProperty("java.vendor"));
        if(e != null)
            e.printStackTrace();
    }


/**
 * 將影像讀入影像池。
 * 可指定要將影像讀入池內幾號位置，並指定影像的名稱。
 * 讀入的影像，可放到Sprite平面上。
 * @param no 存放讀入之影像的位置(索引、0～)
 * @param file 要讀取之影像的檔明
 * @return 若正常讀取完成，會傳回true
*/
    public boolean addGrp(int no, String file) {
        try {
            grp.put(new Integer(no),
                Toolkit.getDefaultToolkit()
                    .getImage(getClass().getResource(file)));
        } catch (Exception e) {
            infomation("Warning : Do not create image data.", e);
            return false;
        }
        tracker.addImage((Image)(grp.get(new Integer(no))), 1);
        return true;
    }


/**
 * 等待影像的讀取。
 * 雖然已經將影像讀入影像池，但還沒有真正讀取完畢的影像會無法使用，
 * 這個方法可以用來等待所有池內的影像讀取完畢。
 * @return 讀取中發生例外時會傳回false
*/
    public boolean waitLoad() {
        try {
            tracker.waitForID(1);
        } catch (InterruptedException e) {
            infomation("Warning : Problem occurred in waitLoad().", e);
            return false;
        }
        return true;
    }


/**
 * 查詢影像的讀取狀態。
 * 雖然已經將影像讀入影像池，但還沒有真正讀取完畢的影像會無法使用，
 * 這個方法可以用來查詢影像是否已經全部讀取完成。
 * 用在想在背景讀取影像時使用。
 * @return 正常讀取結束為1、讀取中為0、發生錯誤則為-1
*/
    public int isLoaded() {
        if(tracker.checkID(1) == false)
            return 0;
        if(tracker.isErrorID(1) == false)
            return 1;
        return -1;
    }


/**
 * 將影像登錄在Sprite平面上。
 * 將影像池內的影像登錄在實際用來顯示的平面上。
 * 為了讓平面處理動畫，故允許對同一個平面登錄多個影像。
 * 登錄影像後，平面會自動被設定為「影像顯示」。
 * @param planeNo 要登錄的平面號碼(0～)
 * @param animeNo 若要登錄動畫時，指定影像是第幾張。靜畫則指定為0。
 * @param grpNo 要登錄的影像。在影像池內的編號(0～)
 * @return 登錄成功時傳回true
*/
    public boolean setPlaneGrp(int planeNo, int animeNo, int grpNo) {
        Integer pno = new Integer(planeNo);
        Plane pln;
        if((pln = (Plane)(planes.get(pno))) == null) {
            pln = new Plane();
            planes.put(pno, pln);
        }
        pln.animeNo = new Integer(animeNo);
        pln.grp.put(pln.animeNo, grp.get(new Integer(grpNo)));
        pln.planeMode = GRP_MODE;
        pln.view = true;
        pln.str = null;
        pln.font = null;
        pln.color = null;
        pln.draw = null;
        return true;
    }


/**
 * 將影像登錄在Sprite平面上。
 * 將影像池內的影像登錄在實際用來顯示的平面上。
 * 登錄影像後，平面會自動被設定為「影像顯示」。
 * 這個版本用在登錄靜畫影像到平面上。
 * @param planeNo 要登錄的平面號碼(0～)
 * @param grpNo 要登錄的影像。在影像池內的編號(0～)
 * @return 登錄成功時傳回true
*/
    public boolean setPlaneGrp(int planeNo, int grpNo) {
        Integer pno = new Integer(planeNo);
        Plane pln;
        if((pln = (Plane)(planes.get(pno))) == null) {
            pln = new Plane();
            planes.put(pno, pln);
        }
        pln.animeNo = new Integer(0);
        pln.grp.put(pln.animeNo, grp.get(new Integer(grpNo)));
        pln.planeMode = GRP_MODE;
        pln.view = true;
        pln.str = null;
        pln.font = null;
        pln.color = null;
        pln.draw = null;
        return true;
    }


/**
 * 設定Sprite平面的座標。
 * 指定Sprite平面在畫面上的顯示位置。
 * @param planeNo 要設定的平面編號(0～)
 * @param x x座標
 * @param y y座標
 * @return 成功時傳回true
*/
    public boolean setPlanePos(int planeNo, int x, int y) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return false;
        pln.posX = x;
        pln.posY = y;
        return true;
    }


/**
 * 相對Sprite平面的座標。
 * 將Sprite平面現在在畫面上的顯示位置加上指定的移動量。
 * @param planeNo 要設定的平面編號(0～)
 * @param x x方向移動量
 * @param y y方向移動量
 * @return 成功時傳回true
*/
    public boolean setPlaneMov(int planeNo, int x, int y) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return false;
        pln.posX += x;
        pln.posY += y;
        return true;
    }


/**
 * 傳回Sprite平面的x座標。
 * 傳回Sprite平面在畫面上的x座標。
 * @param planeNo 要設定的平面編號(0～)
 * @return x方向的顯示位置
*/
    public int getPlanePosX(int planeNo) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return 0xffff;
        return pln.posX;
    }


/**
 * 傳回Sprite平面的y座標。
 * 傳回Sprite平面在畫面上的y座標。
 * @param planeNo 要設定的平面編號(0～)
 * @return y方向的顯示位置
*/
    public int getPlanePosY(int planeNo) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return 0xffff;
        return pln.posY;
    }


/**
 * 設定Sprite平面為動畫格式。
 * 設定平面是否使用動畫。
 * @param planeNo 要設定的平面編號(0～)
 * @param mode 動畫模式設定true、靜化模式設定false
 * @return 設定成功時傳回true
*/
    public boolean setPlaneAnime(int planeNo, boolean mode) {
        int i, j;
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return false;
        if(pln.planeMode != GRP_MODE)
            return false;
        if((pln.anime = mode) == true) {
            pln.animeList = new Integer[pln.grp.size()];
            Enumeration enum = pln.grp.keys();
            for(i = 0; enum.hasMoreElements(); i++)
                pln.animeList[i] = (Integer)(enum.nextElement());
// 因為Java1.1.x沒有sort()，所以自己排序
//          Arrays.sort(pln.animeList);
            Integer tmp;
            for(i = 0; i < pln.animeList.length - 1; i++)
                for(j = i + 1; j < pln.animeList.length; j++)
                    if(pln.animeList[i].intValue()
                        > pln.animeList[j].intValue()) {
                        tmp = pln.animeList[i];
                        pln.animeList[i] = pln.animeList[j];
                        pln.animeList[j] = tmp;
                    }
        }
        else
            pln.animeList = null;
        return true;
    }


/**
 * 在Sprite平面上設定字串。
 * 並將平面設定為「字串顯示用」。
 * @param planeNo 要設定的平面編號(0～)
 * @param str 要顯示在這個字串上的字串
 * @return 設定成功時傳回true
*/
    public boolean setPlaneString(int planeNo, String str) {
        Integer pno = new Integer(planeNo);
        Plane pln;
        if((pln = (Plane)(planes.get(pno))) == null) {
            pln = new Plane();
            planes.put(pno, pln);
        }
        pln.font = new Font("Monospaced", Font.PLAIN, 16);
        pln.color = new Color(0, 0, 0);
        pln.str = str;
        pln.planeMode = STR_MODE;
        pln.view = true;
        pln.grp.clear();
        pln.anime = false;
        pln.animeNo = null;
        pln.draw = null;
        return true;
    }


/**
 * 在Sprite平面上設定向中對齊的字串。
 * 並將平面設定為「字串顯示用」。
 * @param planeNo 要設定的平面編號(0～)
 * @param str 要顯示在這個字串上的字串
 * @return 設定成功時傳回true
*/
    public boolean setPlaneCenterString(int planeNo, String str) {
        Integer pno = new Integer(planeNo);
        Plane pln;
        if((pln = (Plane)(planes.get(pno))) == null) {
            pln = new Plane();
            planes.put(pno, pln);
        }
        pln.font = new Font("Monospaced", Font.PLAIN, 16);
        pln.color = new Color(0, 0, 0);
        pln.str = str;
        pln.planeMode = CENTER_STR_MODE;
        pln.view = true;
        pln.grp.clear();
        pln.anime = false;
        pln.animeNo = null;
        pln.draw = null;
        return true;
    }


/**
 * 設定Sprite平面的Font屬性。
 * 設定平面上顯示文字使用的字形。
 * 依據傳入的資訊建立Font類別。
 * @param planeNo 要設定的平面編號(0～)
 * @param name 字型名稱
 * @param style 樣式
 * @param size 大小
 * @return 設定成功時傳回true
*/
    public boolean setPlaneFont(int planeNo,String name,int style,int size) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return false;
        if((pln.planeMode & STR_MODE) == 0)
            return false;
        if(name == null)
            name = "Monospaced";
        if(style < 0)
            style = Font.PLAIN;
        if(size < 0)
            size = 16;
        pln.font = new Font(name, style, size);
        return true;
    }


/**
 * 設定Sprite平面的顏色屬性。
 * 定義平面的RGB值。用在顯示文字時。
 * @param planeNo 要設定的平面編號(0～)
 * @param r 紅(0～255)
 * @param g 綠(0～255)
 * @param b 藍(0～255)
 * @return 設定成功時傳回true
*/
    public boolean setPlaneColor(int planeNo, int r, int g, int b) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return false;
        if((pln.planeMode & STR_MODE) == 0)
            return false;
        pln.color = new Color(r, g, b);
        return true;
    }


/**
 * 設定Sprite平面的繪圖程序。
 * 並將平面設定為「繪圖程序顯示用」。
 * @param planeNo 要設定的平面編號(0～)
 * @param draw 表示這個平面之繪圖程序的Draw類別實體
 * @return 設定成功時傳回true
*/
    public boolean setPlaneDraw(int planeNo, Draw draw) {
        Integer pno = new Integer(planeNo);
        Plane pln;
        if((pln = (Plane)(planes.get(pno))) == null) {
            pln = new Plane();
            planes.put(pno, pln);
        }
        pln.font = null;
        pln.color = null;
        pln.str = null;
        pln.planeMode = DRW_MODE;
        pln.view = true;
        pln.grp.clear();
        pln.anime = false;
        pln.animeNo = null;
        pln.draw = draw;
        return true;
    }


/**
 * 開/關Sprite平面的顯示。
 * 顯示或隱藏平面的顯示狀態。
 * @param planeNo 要設定的平面編號(0～)
 * @param view 顯示為true、隱藏為false
 * @return 設定成功時傳回true
*/
    public boolean setPlaneView(int planeNo, boolean view) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return false;
        pln.view = view;
        return true;
    }


/**
 * 刪除Sprite平面所存放的資訊。
 * 刪除已經不需要的Sprite平面，以供後續重複使用。
 * @param planeNo 要清除的平面編號(0～)
 * @return 成功刪除時傳回true
*/
    public boolean delPlane(int planeNo) {
        Integer pno = new Integer(planeNo);
        planes.remove(pno);
        return true;
    }


/**
 * 清除所有平面所存放的資訊。
 * 清除所有Sprite平面，恢復原始的狀態。
 * @return 成功清除時傳回true
*/
    public boolean delPlaneAll() {
        planes.clear();
        return true;
    }


/**
 * 根據現在的內容，在螢幕上繪圖。
 * 根據Sprite類別裡所存放的資訊，進行繪圖的動作。
 * @param g 繪圖目標的Graphics物件
 * @return 成功繪圖時傳回true
*/
    public boolean paintScreen(Graphics g) {
        int i, j;
        Graphics gbg;
        Plane pln;

        if(backGrp == null) {
            backGrp = owner.createImage(canvasWidth, canvasHeight);
        }
        gbg = backGrp.getGraphics();

        spriteList = new Integer[planes.size()];
        Enumeration enum = planes.keys();
        for(i = 0; enum.hasMoreElements(); i++)
            spriteList[i] = (Integer)(enum.nextElement());
// 由於Java1.1.x沒有sort()，所以自己排序
//      Arrays.sort(spriteList);
        Integer tmp;
        for(i = 0; i < spriteList.length - 1; i++)
            for(j = i + 1; j < spriteList.length; j++)
                if(spriteList[i].intValue() > spriteList[j].intValue()) {
                    tmp = spriteList[i];
                    spriteList[i] = spriteList[j];
                    spriteList[j] = tmp;
                }

        for(i = 0; i < spriteList.length; i++) {
            pln = (Plane)(planes.get(spriteList[i]));
            if(pln.view == false)
                continue;
            if(pln.planeMode == GRP_MODE) {
                gbg.drawImage((Image)(pln.grp.get(pln.animeNo)),
                    pln.posX, pln.posY, owner);
                if(pln.anime == true) {
                    for(j = 0; pln.animeList[j] != pln.animeNo; j++);
                    j = (j + 1) % pln.animeList.length;
                    pln.animeNo = pln.animeList[j];
                }
// 以左上為基準排列字串
            } else if(pln.planeMode == STR_MODE) {
                gbg.setFont(pln.font);
                gbg.setColor(pln.color);
                gbg.drawString(pln.str, pln.posX, pln.posY+pln.font.getSize());
// 以上方中央為基準排列字串
            } else if(pln.planeMode == CENTER_STR_MODE) {
                gbg.setFont(pln.font);
                gbg.setColor(pln.color);
                gbg.drawString(pln.str,
                    pln.posX - gbg.getFontMetrics().stringWidth(pln.str) / 2,
                    pln.posY + pln.font.getSize());
            } else if(pln.planeMode == DRW_MODE)
                pln.draw.drawing(gbg, pln);
        }
        gbg.dispose();
        g.drawImage(backGrp, owner.getInsets().left, owner.getInsets().top,
            owner);
        return true;
    }
}


/**
 * 用來存放單獨一個Sprite平面資訊的Plane類別<br>
 * 由於建立時並不需要特別進行初始化的動作，所以使用預設建構子。
 * @author mnagaku
 */
class Plane {
/** 是否顯示的旗標 */
    boolean view = false;
/** 是否為動畫的旗標 */
    boolean anime = false;
/** 平面的座標 */
    int posX = 0, posY = 0;
/** 若平面為動畫，存放現在顯示的是第幾張影像 */
    Integer animeNo = null;
/** 用來存放動畫順序用的陣列 */
    Integer animeList[] = null;
/** 平面的模式 */
    int planeMode = 0;
/** 關聯在平面上的影像編號 */
    Hashtable grp = new Hashtable();
/** 存放平面要顯示的字串 */
    String str = null;
/** 用來顯示字串的字形 */
    Font font = null;
/** 用來顯示字串的顏色 */
    Color color = null;
/** draw模式時，存放實際進行繪圖動作的Draw實體 */
    Draw draw = null;
}
