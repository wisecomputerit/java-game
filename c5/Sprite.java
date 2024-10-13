// Sprite.java
// written by mnagaku

import java.util.*;
import java.awt.*;

/**
 * ¹ê§@Sprite¡]¶K¹Ï¡^¥\¯àªºSpriteÃþ§O<br>
 * ³o­ÓÃþ§O¥i±N¼v¹³µø¬°¹s¥ó¡A´£¨ÑÃ¸¹Ï¡B°Êµeµ¥¥\¯à¡C
 * Åª¤Jªº¼v¹³©ñ¸m¦b¼v¹³¦À¸Ì¡A¥i°t¦X¨Ï¥Îªº»Ý­n©ñ¦b¾A·íªº¦a¤è¡C
 * °£¤F¼v¹³¡A¤]¥i¥HÅã¥Ü¤å¦r¡C¤å¦r»P¼v¹³¡A
 * ³£À³©ñ¦bSprite¥­­±¤W¡A±N©Ò¦³¥­­±Å|¦b¤@°_¡A
 * ´N¬O¾ã­Óµe­±¡C
 * @author mnagaku
 */
public class Sprite {
/** Sprite¥­­±ªº¼Ò¦¡ */
	static final int NULL_MODE = 0, GRP_MODE = 1, STR_MODE = 2, DRW_MODE = 8,
		CENTER_STR_MODE = 6;
/** Ã¸¹Ï¹ï¶Hµe­±ªº¤j¤p */
	int canvasWidth, canvasHeight;
/** ¦s©ñÃ¸¹Ï¹ï¶HContainerªºÅÜ¼Æ */
	Container owner;
/** ½w½Ä°Ï */
	Hashtable grp;
/** Åª¨ú¼v¹³¦s©ñªº¼v¹³¦À */
	Image backGrp = null;
/** ¥Î¨ÓºÊµø¼v¹³ªºÅª¨úª¬ºA */
	MediaTracker tracker;
/** Sprite¥­­± */
	Hashtable planes;
/** Ã¸»s¥­­±ªº¶¶§Ç */
	Integer spriteList[];


/**
 * «Øºc¤l
 * «Ø¥ß¼v¹³¦À¡BSprite¥­­±¡A¨ÃÀx¦sµe­±¬ÛÃö¸ê°T¡C
 * @param canvasWidth Ã¸¹Ï¹ï¶Hµe­±ªº¼e«×
 * @param canvasHeight Ã¸¹Ï¹ï¶Hµe­±ªº°ª«×
 * @param owner Ã¸¹Ï¹ï¶HªºContainer
*/
	public Sprite(int canvasWidth, int canvasHeight, Container owner) {
		int i, j;
// «Ø¥ß¼v¹³¦À
		grp = new Hashtable();
// Sprite¥­­±
		planes = new Hashtable();
// °O¿ýÃ¸¹Ï¹ï¶Hµe­±
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.owner = owner;
// µe¹³ª¬ºAªººÞ²z©MÅª¨ú¡A¥ÑMediaTracker«Ø¥ß
		tracker = new MediaTracker(owner);
	}


/**
 * ¿ù»~¡BÄµ§iªºªí¥Ü
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
 * ±N¼v¹³Åª¤J¼v¹³¦À¡C
 * ¥i«ü©w­n±N¼v¹³Åª¤J¦À¤º´X¸¹¦ì¸m¡A¨Ã«ü©w¼v¹³ªº¦WºÙ¡C
 * Åª¤Jªº¼v¹³¡A¥i©ñ¨ìSprite¥­­±¤W¡C
 * @param no ¦s©ñÅª¤J¤§¼v¹³ªº¦ì¸m(¯Á¤Þ¡B0¡ã)
 * @param file ­nÅª¨ú¤§¼v¹³ªºÀÉ¦W
 * @return ­Y¥¿±`Åª¨ú§¹¦¨¡A·|¶Ç¦^true
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
 * µ¥«Ý¼v¹³ªºÅª¨ú
 * ÁöµM¤w¸g±N¼v¹³Åª¤J¼v¹³¦À¡A¦ýÁÙ¨S¦³¯u¥¿Åª¨ú§¹²¦ªº¼v¹³·|µLªk¨Ï¥Î¡A
 * ³o­Ó¤èªk¥i¥H¥Î¨Óµ¥«Ý©Ò¦³¦À¤ºªº¼v¹³Åª¨ú§¹²¦¡C
 * @return Åª¨ú¤¤µo¥Í¨Ò¥~®É·|¶Ç¦^false
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
 * ¬d¸ß¼v¹³ªºÅª¨úª¬ºA¡C
 * ÁöµM¤w¸g±N¼v¹³Åª¤J¼v¹³¦À¡A¦ýÁÙ¨S¦³¯u¥¿Åª¨ú§¹²¦ªº¼v¹³·|µLªk¨Ï¥Î¡A
 * ³o­Ó¤èªk¥i¥H¥Î¨Ó¬d¸ß¼v¹³¬O§_¤w¸g¥þ³¡Åª¨ú§¹¦¨¡C
 * ¥Î¦b·Q¦b­I´ºÅª¨ú¼v¹³®É¨Ï¥Î¡C
 * @return ¥¿±`Åª¨úµ²§ô¬°1¡BÅª¨ú¤¤¬°0¡Bµo¥Í¿ù»~«h¬°-1
*/
	public int isLoaded() {
		if(tracker.checkID(1) == false)
			return 0;
		if(tracker.isErrorID(1) == false)
			return 1;
		return -1;
	}


/**
 * ±N¼v¹³µn¿ý¦bSprite¥­­±¤W¡C
 * ±N¼v¹³¦À¤ºªº¼v¹³µn¿ý¦b¹ê»Ú¥Î¨ÓÅã¥Üªº¥­­±¤W¡C
 * ¬°¤FÅý¥­­±³B²z°Êµe¡A¬G¤¹³\¹ï¦P¤@­Ó¥­­±µn¿ý¦h­Ó¼v¹³¡C
 * µn¿ý¼v¹³«á¡A¥­­±·|¦Û°Ê³Q³]©w¬°¡u¼v¹³Åã¥Ü¡v¡C
 * @param planeNo ­nµn¿ýªº¥­­±¸¹½X(0¡ã)
 * @param animeNo ­Y­nµn¿ý°Êµe®É¡A«ü©w¼v¹³¬O²Ä´X±i¡CÀRµe«h«ü©w¬°0¡C
 * @param grpNo ­nµn¿ýªº¼v¹³¡C¦b¼v¹³¦À¤ºªº½s¸¹(0¡ã)
 * @return µn¿ý¦¨¥\®É¶Ç¦^true
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
 * ±N¼v¹³µn¿ý¦bSprite¥­­±¤W¡C
 * ±N¼v¹³¦À¤ºªº¼v¹³µn¿ý¦b¹ê»Ú¥Î¨ÓÅã¥Üªº¥­­±¤W¡C
 * µn¿ý¼v¹³«á¡A¥­­±·|¦Û°Ê³Q³]©w¬°¡u¼v¹³Åã¥Ü¡v¡C
 * ³o­Óª©¥»¥Î¦bµn¿ýÀRµe¼v¹³¨ì¥­­±¤W¡C
 * @param planeNo ­nµn¿ýªº¥­­±¸¹½X(0¡ã)
 * @param grpNo ­nµn¿ýªº¼v¹³¡C¦b¼v¹³¦À¤ºªº½s¸¹(0¡ã)
 * @return µn¿ý¦¨¥\®É¶Ç¦^true
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
 * ³]©wSprite¥­­±ªº®y¼Ð¡C
 * «ü©wSprite¥­­±¦bµe­±¤WªºÅã¥Ü¦ì¸m¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @param x x®y¼Ð
 * @param y y®y¼Ð
 * @return ¦¨¥\®É¶Ç¦^true
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
 * ¬Û¹ïSprite¥­­±ªº®y¼Ð¡C
 * ±NSprite¥­­±²{¦b¦bµe­±¤WªºÅã¥Ü¦ì¸m¥[¤W«ü©wªº²¾°Ê¶q¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @param x x¤è¦V²¾°Ê¶q
 * @param y y¤è¦V²¾°Ê¶q
 * @return ¦¨¥\®É¶Ç¦^true
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
 * ¶Ç¦^Sprite¥­­±ªºx®y¼Ð¡C
 * ¶Ç¦^Sprite¥­­±¦bµe­±¤Wªºx®y¼Ð¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @return x¤è¦VªºÅã¥Ü¦ì¸m
*/
	public int getPlanePosX(int planeNo) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return 0xffff;
		return pln.posX;
	}


/**
 * ¶Ç¦^Sprite¥­­±ªºy®y¼Ð¡C
 * ¶Ç¦^Sprite¥­­±¦bµe­±¤Wªºy®y¼Ð¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @return y¤è¦VªºÅã¥Ü¦ì¸m
*/
	public int getPlanePosY(int planeNo) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return 0xffff;
		return pln.posY;
	}


/**
 * ³]©wSprite¥­­±¬°°Êµe®æ¦¡¡C
 * ³]©w¥­­±¬O§_¨Ï¥Î°Êµe¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @param mode °Êµe¼Ò¦¡³]©wtrue¡BÀR¤Æ¼Ò¦¡³]©wfalse
 * @return ³]©w¦¨¥\®É¶Ç¦^true
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
// Java1.1.x¨S¦³sort()¡A©Ò¥H¤£¯à¦Û¤v±Æ§Ç
//			Arrays.sort(pln.animeList);
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
 * ¦bSprite¥­­±¤W³]©w¦r¦ê¡C
 * ¨Ã±N¥­­±³]©w¬°¡u¦r¦êÅã¥Ü¥Î¡v¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @param str ­nÅã¥Ü¦b³o­Ó¦r¦ê¤Wªº¦r¦ê
 * @return ³]©w¦¨¥\®É¶Ç¦^true
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
 * ¦bSprite¥­­±¤W³]©w¦V¤¤¹ï»ôªº¦r¦ê¡C
 * ¨Ã±N¥­­±³]©w¬°¡u¦r¦êÅã¥Ü¥Î¡v¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @param str ­nÅã¥Ü¦b³o­Ó¦r¦ê¤Wªº¦r¦ê
 * @return ³]©w¦¨¥\®É¶Ç¦^true
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
 * ³]©wSprite¥­­±ªºFontÄÝ©Ê¡C
 * ³]©w¥­­±¤WÅã¥Ü¤å¦r¨Ï¥Îªº¦r§Î¡C
 * ¨Ì¾Ú¶Ç¤Jªº¸ê°T«Ø¥ßFontÃþ§O¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @param name ¦r«¬¦WºÙ
 * @param style ¼Ë¦¡
 * @param size ¤j¤p
 * @return ³]©w¦¨¥\®É¶Ç¦^true
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
 * ³]©wSprite¥­­±ªºÃC¦âÄÝ©Ê¡C
 * ©w¸q¥­­±ªºRGB­È¡C¥Î¦bÅã¥Ü¤å¦r®É¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @param r ¬õ(0¡ã255)
 * @param g ºñ(0¡ã255)
 * @param b ÂÅ(0¡ã255)
 * @return ³]©w¦¨¥\®É¶Ç¦^true
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
 * ³]©wSprite¥­­±ªºÃ¸¹Ïµ{§Ç¡C
 * ¨Ã±N¥­­±³]©w¬°¡uÃ¸¹Ïµ{§ÇÅã¥Ü¥Î¡v¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @param draw ªí¥Ü³o­Ó¥­­±Ã¸¹Ïµ{§ÇªºDrawÃþ§Oªº¹êÅé
 * @return ³]©w¦¨¥\®É¶Ç¦^true
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
 * ¶}/ÃöSprite¥­­±ªºÅã¥Ü¡C
 * Åã¥Ü©ÎÁôÂÃ¥­­±ªºÅã¥Üª¬ºA¡C
 * @param planeNo ­n³]©wªº¥­­±½s¸¹(0¡ã)
 * @param view Åã¥Ü¬°true¡BÁôÂÃ¬°false
 * @return ³]©w¦¨¥\®É¶Ç¦^true
*/
	public boolean setPlaneView(int planeNo, boolean view) {
		Plane pln;
		if((pln = (Plane)(planes.get(new Integer(planeNo)))) == null)
			return false;
		pln.view = view;
		return true;
	}


/**
 * §R°£Sprite¥­­±©Ò¦s©ñªº¸ê°T¡C
 * §R°£¤w¸g¤£»Ý­nªºSprite¥­­±¡A¥H¨Ñ«áÄò­«½Æ¨Ï¥Î¡C
 * @param planeNo ­n²M°£ªº¥­­±½s¸¹(0¡ã)
 * @return ¦¨¥\§R°£®É¶Ç¦^true
*/
	public boolean delPlane(int planeNo) {
		Integer pno = new Integer(planeNo);
		planes.remove(pno);
		return true;
	}


/**
 * ²M°£©Ò¦³¥­­±©Ò¦s©ñªº¸ê°T¡C
 * ²M°£©Ò¦³Sprite¥­­±¡A«ì´_­ì©lªºª¬ºA¡C
 * @return ¦¨¥\§R°£®É¶Ç¦^true
*/
	public boolean delPlaneAll() {
		planes.clear();
		return true;
	}


/**
 * ®Ú¾Ú²{¦bªº¤º®e¡A¦b¿Ã¹õ¤WÃ¸¹Ï¡C
 * ®Ú¾ÚSpriteÃþ§O¸Ì©Ò¦s©ñªº¸ê°T¡A¶i¦æÃ¸¹Ïªº°Ê§@¡C
 * @param g Ã¸¹Ï¥Ø¼ÐªºGraphicsª«¥ó
 * @return ¦¨¥\Ã¸¹Ï®É¶Ç¦^true
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
// Java1.1.x¨S¦³sort()¡A©Ò¥H¤£¯à¦Û¤v±Æ§Ç
//		Arrays.sort(spriteList);
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
//
			} else if(pln.planeMode == STR_MODE) {
				gbg.setFont(pln.font);
				gbg.setColor(pln.color);
				gbg.drawString(pln.str, pln.posX, pln.posY+pln.font.getSize());
// ã•Ó??Šî?‚Å•¶?—ñ•\Ž¦
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
 * ¥Î¨Ó¦s©ñ³æ¿W¤@­ÓSprite¥­­±¸ê°TªºPlaneÃþ§O¡C<br>
 * ¥Ñ©ó«Ø¥ß®É¨Ã¤£»Ý­n¯S§O¶i¦æªì©l¤Æªº°Ê§@¡A©Ò¥H¨Ï¥Î¹w³]«Øºc¤l¡C
 * @author mnagaku
 */
class Plane {
/** ¬O§_Åã¥ÜªººX¼Ð */
	boolean view = false;
/** ¬O§_¬°°ÊµeªººX¼Ð */
	boolean anime = false;
/** ¥­­±ªº®y¼Ð */
	int posX = 0, posY = 0;
/** ­Y¥­­±¬°°Êµe¡A¦s©ñ²{¦bÅã¥Üªº¬O²Ä´X±i¼v¹³ */
	Integer animeNo = null;
/** ¥Î¨Ó¦s©ñ°Êµe¶¶§Ç¥Îªº°}¦C */
	Integer animeList[] = null;
/** ¥­­±ªº¼Ò¦¡ */
	int planeMode = 0;
/** ÃöÁp¦b¥­­±¤Wªº¼v¹³½s¸¹ */
	Hashtable grp = new Hashtable();
/** ¦s©ñ¥­­±­nÅã¥Üªº¦r¦ê */
	String str = null;
/** ¥Î¨ÓÅã¥Ü¦r¦êªº¦r§Î */
	Font font = null;
/** ¥Î¨ÓÅã¥Ü¦r¦êªºÃC¦â */
	Color color = null;
/** draw¼Ò¦¡®É¡A¦s©ñ¹ê»Ú¶i¦æÃ¸¹Ï°Ê§@ªºDraw¹êÅé */
	Draw draw = null;
}

