// Sprite.java
// written by mnagaku

import java.util.*;
import java.awt.*;

/**
 * ��@Sprite�]�K�ϡ^�\�઺Sprite���O<br>
 * �o�����O�i�N�v�������s��A����ø�ϡB�ʵe���\��C
 * Ū�J���v����m�b�v�����̡A�i�t�X�ϥΪ��ݭn��b�A���a��C
 * ���F�v���A�]�i�H��ܤ�r�C
 * ��r�P�v���A������bSprite�����W�A
 * �N�Ҧ������|�b�@�_�A�N�O��ӵe��
 * @author mnagaku
 */
public class Sprite {
/** Sprite�������Ҧ� */
    static final int NULL_MODE = 0, GRP_MODE = 1, STR_MODE = 2, DRW_MODE = 8,
        CENTER_STR_MODE = 6;
/** ø�Ϲ�H�e�����j�p */
    int canvasWidth, canvasHeight;
/** �s��ø�Ϲ�HContainer���ܼ� */
    Container owner;
/** Ū���v���s�񪺼v���� */
    Hashtable grp;
/** �w�İ� */
    Image backGrp = null;
/** �ΨӺʵ��v����Ū�����A */
    MediaTracker tracker;
/** Sprite���� */
    Hashtable planes;
/** ø�s���������� */
    Integer spriteList[];


/**
 * �غc�l�C
 * �إ߼v�����BSprite�����A���x�s�e��������T�C
 * @param canvasWidth ø�Ϲ�H�e�����e��
 * @param canvasHeight ø�Ϲ�H�e��������
 * @param owner ø�Ϲ�H��Container
*/
    public Sprite(int canvasWidth, int canvasHeight, Container owner) {
        int i, j;
// �إ߼v����
        grp = new Hashtable();
// �إ�Sprite����
        planes = new Hashtable();
// �x�sø�ϥ�������T
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.owner = owner;
// �إߥΨӺ޲z�v��Ū�����A��MediaTracker
        tracker = new MediaTracker(owner);
    }


/**
 * ��ܿ��~�Bĵ�i�C
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
 * �N�v��Ū�J�v�����C
 * �i���w�n�N�v��Ū�J�����X����m�A�ë��w�v�����W�١C
 * Ū�J���v���A�i���Sprite�����W�C
 * @param no �s��Ū�J���v������m(���ޡB0��)
 * @param file �nŪ�����v�����ɩ�
 * @return �Y���`Ū�������A�|�Ǧ^true
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
 * ���ݼv����Ū���C
 * ���M�w�g�N�v��Ū�J�v�����A���٨S���u��Ū���������v���|�L�k�ϥΡA
 * �o�Ӥ�k�i�H�Ψӵ��ݩҦ��������v��Ū�������C
 * @return Ū�����o�ͨҥ~�ɷ|�Ǧ^false
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
 * �d�߼v����Ū�����A�C
 * ���M�w�g�N�v��Ū�J�v�����A���٨S���u��Ū���������v���|�L�k�ϥΡA
 * �o�Ӥ�k�i�H�ΨӬd�߼v���O�_�w�g����Ū�������C
 * �Φb�Q�b�I��Ū���v���ɨϥΡC
 * @return ���`Ū��������1�BŪ������0�B�o�Ϳ��~�h��-1
*/
    public int isLoaded() {
        if(tracker.checkID(1) == false)
            return 0;
        if(tracker.isErrorID(1) == false)
            return 1;
        return -1;
    }


/**
 * �N�v���n���bSprite�����W�C
 * �N�v���������v���n���b��ڥΨ���ܪ������W�C
 * ���F�������B�z�ʵe�A�G���\��P�@�ӥ����n���h�Ӽv���C
 * �n���v����A�����|�۰ʳQ�]�w���u�v����ܡv�C
 * @param planeNo �n�n�����������X(0��)
 * @param animeNo �Y�n�n���ʵe�ɡA���w�v���O�ĴX�i�C�R�e�h���w��0�C
 * @param grpNo �n�n�����v���C�b�v���������s��(0��)
 * @return �n�����\�ɶǦ^true
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
 * �N�v���n���bSprite�����W�C
 * �N�v���������v���n���b��ڥΨ���ܪ������W�C
 * �n���v����A�����|�۰ʳQ�]�w���u�v����ܡv�C
 * �o�Ӫ����Φb�n���R�e�v���쥭���W�C
 * @param planeNo �n�n�����������X(0��)
 * @param grpNo �n�n�����v���C�b�v���������s��(0��)
 * @return �n�����\�ɶǦ^true
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
 * �]�wSprite�������y�СC
 * ���wSprite�����b�e���W����ܦ�m�C
 * @param planeNo �n�]�w�������s��(0��)
 * @param x x�y��
 * @param y y�y��
 * @return ���\�ɶǦ^true
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
 * �۹�Sprite�������y�СC
 * �NSprite�����{�b�b�e���W����ܦ�m�[�W���w�����ʶq�C
 * @param planeNo �n�]�w�������s��(0��)
 * @param x x��V���ʶq
 * @param y y��V���ʶq
 * @return ���\�ɶǦ^true
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
 * �Ǧ^Sprite������x�y�СC
 * �Ǧ^Sprite�����b�e���W��x�y�СC
 * @param planeNo �n�]�w�������s��(0��)
 * @return x��V����ܦ�m
*/
    public int getPlanePosX(int planeNo) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return 0xffff;
        return pln.posX;
    }


/**
 * �Ǧ^Sprite������y�y�СC
 * �Ǧ^Sprite�����b�e���W��y�y�СC
 * @param planeNo �n�]�w�������s��(0��)
 * @return y��V����ܦ�m
*/
    public int getPlanePosY(int planeNo) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return 0xffff;
        return pln.posY;
    }


/**
 * �]�wSprite�������ʵe�榡�C
 * �]�w�����O�_�ϥΰʵe�C
 * @param planeNo �n�]�w�������s��(0��)
 * @param mode �ʵe�Ҧ��]�wtrue�B�R�ƼҦ��]�wfalse
 * @return �]�w���\�ɶǦ^true
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
// �]��Java1.1.x�S��sort()�A�ҥH�ۤv�Ƨ�
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
 * �bSprite�����W�]�w�r��C
 * �ñN�����]�w���u�r����ܥΡv�C
 * @param planeNo �n�]�w�������s��(0��)
 * @param str �n��ܦb�o�Ӧr��W���r��
 * @return �]�w���\�ɶǦ^true
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
 * �bSprite�����W�]�w�V��������r��C
 * �ñN�����]�w���u�r����ܥΡv�C
 * @param planeNo �n�]�w�������s��(0��)
 * @param str �n��ܦb�o�Ӧr��W���r��
 * @return �]�w���\�ɶǦ^true
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
 * �]�wSprite������Font�ݩʡC
 * �]�w�����W��ܤ�r�ϥΪ��r�ΡC
 * �̾ڶǤJ����T�إ�Font���O�C
 * @param planeNo �n�]�w�������s��(0��)
 * @param name �r���W��
 * @param style �˦�
 * @param size �j�p
 * @return �]�w���\�ɶǦ^true
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
 * �]�wSprite�������C���ݩʡC
 * �w�q������RGB�ȡC�Φb��ܤ�r�ɡC
 * @param planeNo �n�]�w�������s��(0��)
 * @param r ��(0��255)
 * @param g ��(0��255)
 * @param b ��(0��255)
 * @return �]�w���\�ɶǦ^true
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
 * �]�wSprite������ø�ϵ{�ǡC
 * �ñN�����]�w���uø�ϵ{����ܥΡv�C
 * @param planeNo �n�]�w�������s��(0��)
 * @param draw ��ܳo�ӥ�����ø�ϵ{�Ǫ�Draw���O����
 * @return �]�w���\�ɶǦ^true
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
 * �}/��Sprite��������ܡC
 * ��ܩ����å�������ܪ��A�C
 * @param planeNo �n�]�w�������s��(0��)
 * @param view ��ܬ�true�B���ì�false
 * @return �]�w���\�ɶǦ^true
*/
    public boolean setPlaneView(int planeNo, boolean view) {
        Plane pln;
        if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
            return false;
        pln.view = view;
        return true;
    }


/**
 * �R��Sprite�����Ҧs�񪺸�T�C
 * �R���w�g���ݭn��Sprite�����A�H�ѫ��򭫽ƨϥΡC
 * @param planeNo �n�M���������s��(0��)
 * @return ���\�R���ɶǦ^true
*/
    public boolean delPlane(int planeNo) {
        Integer pno = new Integer(planeNo);
        planes.remove(pno);
        return true;
    }


/**
 * �M���Ҧ������Ҧs�񪺸�T�C
 * �M���Ҧ�Sprite�����A��_��l�����A�C
 * @return ���\�M���ɶǦ^true
*/
    public boolean delPlaneAll() {
        planes.clear();
        return true;
    }


/**
 * �ھڲ{�b�����e�A�b�ù��Wø�ϡC
 * �ھ�Sprite���O�̩Ҧs�񪺸�T�A�i��ø�Ϫ��ʧ@�C
 * @param g ø�ϥؼЪ�Graphics����
 * @return ���\ø�ϮɶǦ^true
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
// �ѩ�Java1.1.x�S��sort()�A�ҥH�ۤv�Ƨ�
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
// �H���W����ǱƦC�r��
            } else if(pln.planeMode == STR_MODE) {
                gbg.setFont(pln.font);
                gbg.setColor(pln.color);
                gbg.drawString(pln.str, pln.posX, pln.posY+pln.font.getSize());
// �H�W�褤������ǱƦC�r��
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
 * �ΨӦs���W�@��Sprite������T��Plane���O<br>
 * �ѩ�إ߮ɨä��ݭn�S�O�i���l�ƪ��ʧ@�A�ҥH�ϥιw�]�غc�l�C
 * @author mnagaku
 */
class Plane {
/** �O�_��ܪ��X�� */
    boolean view = false;
/** �O�_���ʵe���X�� */
    boolean anime = false;
/** �������y�� */
    int posX = 0, posY = 0;
/** �Y�������ʵe�A�s��{�b��ܪ��O�ĴX�i�v�� */
    Integer animeNo = null;
/** �ΨӦs��ʵe���ǥΪ��}�C */
    Integer animeList[] = null;
/** �������Ҧ� */
    int planeMode = 0;
/** ���p�b�����W���v���s�� */
    Hashtable grp = new Hashtable();
/** �s�񥭭��n��ܪ��r�� */
    String str = null;
/** �Ψ���ܦr�ꪺ�r�� */
    Font font = null;
/** �Ψ���ܦr�ꪺ�C�� */
    Color color = null;
/** draw�Ҧ��ɡA�s���ڶi��ø�ϰʧ@��Draw���� */
    Draw draw = null;
}
