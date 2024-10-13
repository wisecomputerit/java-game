// Game2D.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.lang.reflect.*;

/**
 * 2D game framework class Game2D<br>
 * ºŞ²z2D¹CÀ¸µ{¦¡©Ò»İ¤§Sprite¡BÁn­µ¡B¿é¤Jªºframework¡C
 * extends³o­ÓÃş§O¶}µo2D¹CÀ¸¡C
 * ¤F¦bMacOS9¤§«eªºÀô¹ÒMRJ2.2.5¥H¤ÎMSVM¤W³£¥i¥H¨Ï¥Î¡A³o¸Ì¨Ï¥Î°õ¦æºü
 * ¹ê§@­p®É¾¹¡AÅıJava1.1.xª©¯à°÷¤ä´©¡C
 * <br>
 * extends³o­ÓÃş§OªºGameÃş§O¡A
 * ¥i¥Î¤U¦CªºHTML±Ô­z¡A¥HAppletªº§Î¦¡©I¥s¡C
 * ¡]Applet¤w¸gjar¤Æ®É¡^
 * <br>
 * <pre>
 * &lt;html&gt;
 * &lt;head&gt;
 * &lt;title&gt;Game&lt;/title&gt;
 * &lt;/head&gt;
 * &lt;applet code=Game archive=Game.jar width=200 height=200&gt;
 * &lt;/applet&gt;
 * &lt;/html&gt;
 * </pre>
 * @author mnagaku
 */
abstract public class Game2D extends Applet {

/** ¹CÀ¸¦WºÙ¡C·|±qGame2DÃş§Oªº¤lÃş§O¦WºÙ²£¥Í */
	static String GAME_NAME;

/** Game2DMainÃş§O¤§¤lÃş§Oªº¦WºÙ */
	static String GAME_MAIN_NAME;

/** µøµ¡Ã¸¹Ï°Ï°ì¼e«× */
	int CANVAS_SIZE_W = 320;
/** µøµ¡Ã¸¹Ï°Ï°ì°ª«× */
	int CANVAS_SIZE_H = 240;

/** µe­±­«Ã¸¡B¥D°j°é³B²zªº³t«×¡C³æ¦ì¬O²@¬í */
	int SPEED = 100;

/** ¤è¦VÁäªº«öÁä­«½Æ¿é¤J³t«×¡C³æ¦ì¬O²@¬í */
	int KEY_SPEED = 50;
/** ¤è¦VÁäªº«öÁä­«½Æ¿é¤J¤@¶}©lªº©µ¿ğ®É¶¡¡C³æ¦ì¬OGET_KEY_SPEEDªº¦¸¼Æ */
	int KEY_DELAY = 3;

/** Àx¦s«öÁä«ö¤Uªºª¬ºA */
	boolean pressUp = false, pressDown = false,
		pressLeft = false, pressRight = false;
/** Àx¦s·Æ¹«¬O§_¦b°Ï°ì¤º */
	boolean mouseOnFrame = false;

/** Àx¦s¬O¥HApplet§Î¦¡°õ¦æÁÙ¬OApplication§Î¦¡ */
	boolean appletFlag = true;

/** SpriteÃş§O¡C»P¹Ï§Î¬ÛÃöªº°Ê§@³£¥æµ¹SpriteÃş§O */
	Sprite sprite;

/** SoundPaletteÃş§O¡C»PBGM¡BSE¬ÛÃöªº³B²z³£¥æµ¹SoundPaletteÃş§O */
	SoundPalette sp;

/** Game2DMainÃş§O¡C¹CÀ¸¥»Åéªº¤u§@¥æµ¹Game2DMainÃş§O */
	Game2DMain gm;

/** Áä½L¨Æ¥óªºQueue¡C¤è¦VÁäªºÁä½L­«½Æ¦³®Ä¡C */
	Queue keyQ;
/** ·Æ¹«¨Æ¥óªºQueue¡C */
	Queue mouseQ;

/** ¦s©ñ¥Î¨Ó¹ê§@­«Ã¸¡B¥D°j°é¾÷¨îªºMainLoopª«¥ó¡C */
	MainLoop timerTask;
/** ¦s©ñ¥Î¨Ó¹ê²{Áä½L­«½Æ¿é¤J¾÷¨îªºKeyRepeaterª«¥ó¡C */
	KeyRepeater keyTimerTask;


/**
 * «Øºc¤l
 * ¦Û°Ê·j´MÄ~©ÓGame2DMainªºÃş§O¦WºÙ«á¦s¤JGAME_MAIN_NAME¡C
 * °ò¥»¤W·|¦Û°Ê·j´M¡A¦ı¥Ñ©óJava1.1.xµLªk·j´M¡A©Ò¥H
 * GAME_MAIN_NAME = GAME_NAME + "$" + GAME_NAME + "Main";
 * ‚Æ‚·‚éB
 */
	public Game2D() {
		try {
			GAME_NAME = getClass().getName();
			Class[] mbrs = getClass().getClasses();
			if(mbrs.length == 0) {
				GAME_MAIN_NAME = GAME_NAME + "$" + GAME_NAME + "Main";
				infomation("Warning : I can not getClasses().", null);
			}
			for(int i = 0; i < mbrs.length; i++)
				if(mbrs[i].getSuperclass().getName()
					.compareTo("Game2D$Game2DMain") == 0)
					GAME_MAIN_NAME = mbrs[i].getName();
		} catch (Exception e) {
			infomation("Error : I can not finish Game2D constructor.", e);
		}
	}


/**
 * Åã¥Ü¿ù»~¡BÄµ§i¡C
 * @param info ­nÅã¥Üªº¦r¦ê¡C¥H¡uError¡v¶}©lªº¦r¦ê¡A·|±j¨îµ²§ôµ{¦¡¡C
 * @param e µo¥Í¨Ò¥~®É¡A­Y±N¨Ò¥~¤]¶Ç¶i¨Ó¡A´N·|Åã¥Ü¨Ò¥~ªº¸ê°T¡C
 * ¨S¦³¨Ò¥~«h¶Ç¤Jnull¡C
 */
	static void infomation(String info, Exception e) {
		System.out.println(info);
		System.out.println("java.version : "
			+ System.getProperty("java.version"));
		System.out.println("java.vendor : "
			+ System.getProperty("java.vendor"));
		if(e != null) {
			if(e.getClass().getName().compareTo(
				"java.lang.reflect.InvocationTargetException") == 0)
				((InvocationTargetException)e).getTargetException()
					.printStackTrace();
			else
				e.printStackTrace();
		}
		if(info.indexOf("Error") == 0)
			System.exit(0);
	}


/**
 * ¤£¬OApplet®É¡A¥Ñmain()©I¥s«Øºc¤lªº¥~´ß¡C
 * @param game2dClassName ­n«Ø¥ßªºGame2D¤lÃş§Oªº¦WºÙ
 */
	static void startGame(String game2dClassName) {
		GAME_NAME = game2dClassName;
		try {
			Game2D game2D = (Game2D)(Class
				.forName(game2dClassName).newInstance());
			game2D.newGame2D();
		} catch (Exception e) {
			infomation("Error : I can not create Game2D or newGame2D().", e);
		}
	}


/**
 * ¥ÑGame2DÃş§O©Ò©I¥sªºGame2DMainÃş§O«Øºc¤lªº¥~´ß¡C
 * ¹ê»Ú¤W¬O¨Ï¥ÎGAME_MAIN_NAME¸Ì
 * ©Ò³]©wªºGame2DMain¤lÃş§Oªº¦WºÙ¡A©I¥s¨ä«Øºc¤l¡C
 * @return «Ø¥ß¥XªºGame2DMain¤lÃş§Oªºª«¥ó
 */
	Game2DMain newGame2DMain() {
		try {
			Class argClass[] = {getClass()};
			Constructor g2dmCon
				= Class.forName(GAME_MAIN_NAME).getConstructor(argClass);
			Object initArgs[] = {this};
			return (Game2DMain)(g2dmCon.newInstance(initArgs));
		} catch(Exception e) {
			infomation("Error : I can not create Game2DMain.", e);
		}
		return null;
	}


/**
 * ¥Î¨Ó¶}©l°õ¦æÀ³¥Îµ{¦¡¡C
 * ¦bmain¤èªk¦Û§Ú«Øºc«á©I¥s¡C
 * ·|«Ø¥ß¥Xµøµ¡¡A¦b¸Ì­±Åã¥ÜApplet¡C
 */
	public void newGame2D() {
// Applet¶}©l°õ¦æ
		appletFlag = false;
		init();

// «Ø¥ßÀ³¥Îµ{¦¡µøµ¡
		Frame frame = new Frame(GAME_NAME);
		frame.pack();
		frame.setVisible(true);
		frame.setVisible(false);
		frame.pack();

// µøµ¡Åã¥Ü¦b¿Ã¹õ¤¤¥¡
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width-CANVAS_SIZE_W)/2,
			(d.height-CANVAS_SIZE_H)/2);

// ³]©wµøµ¡¤j¤p
		int left, right, top, bottom;
		left = frame.getInsets().left;
		right = frame.getInsets().right;
		top = frame.getInsets().top;
		bottom = frame.getInsets().bottom;
		frame.setSize(CANVAS_SIZE_W + left + right,
			CANVAS_SIZE_H + top + bottom);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
// ¦pªGµøµ¡ÅÜ¦¨¹Ï¥Ü«hÁn­µ°±¤î
			public void windowIconified(WindowEvent e) {stop();}
// ¦pªGµøµ¡±q¹Ï¥ÜÁÙ­ì«h«h¶}±ÒBGM
			public void windowDeiconified(WindowEvent e) {start();}
		});
		frame.setResizable(false);

		frame.add(this);
// Åã¥Üµøµ¡
		frame.setVisible(true);
// ½T©wµøµ¡¤j¤p
		if(left != frame.getInsets().left
			|| right != frame.getInsets().right
			|| top != frame.getInsets().top
			|| bottom != frame.getInsets().bottom) {
			left = frame.getInsets().left;
			right = frame.getInsets().right;
			top = frame.getInsets().top;
			bottom = frame.getInsets().bottom;
			frame.setSize(CANVAS_SIZE_W + left + right,
				CANVAS_SIZE_H + top + bottom);
		}
	}


/**
 * ¶}©l°õ¦æApplet¡C
 * ·Ç³Æ¹CÀ¸ªº¥»Åé¡C
 * ¶}©l¶i¦æ­«Ã¸¡B¥D°j°é¡B¨Æ¥ó³B²zµ¥°Ê§@¡C
 */
	public void init() {
// ¨ú±o»P³]©w¨Æ¥ó
		enableEvents(AWTEvent.MOUSE_EVENT_MASK |
			AWTEvent.MOUSE_MOTION_EVENT_MASK |
			AWTEvent.KEY_EVENT_MASK);
// Sprite¡BÁn­µ¡B¿é¤Jµ¥ªººŞ²z»P¥Í¦¨
		keyQ = new Queue();
		mouseQ = new Queue();
		if(appletFlag)
			sp = new SoundPalette(this);
		else
			sp = new SoundPalette();
		sprite = new Sprite(CANVAS_SIZE_W, CANVAS_SIZE_H, this);
// ¹CÀ¸ªº¥»Åéªº«Ø¥ß
		gm = newGame2DMain();
// ­«Ã¸¡B¥D°j°éªº«Ø¥ß
		timerTask = new MainLoop();
		timerTask.start();
// ¨Æ¥ó³B²zªº«Ø¥ß
		keyTimerTask = new KeyRepeater();
		keyTimerTask.start();
// ¨ú±oµJÂI
		requestFocus();
	}


/** ¦^¨ìApplet®É¡A¼½©ñBGM */
	public void start() {
		sp.restart();
		timerTask.threadSuspended = false;
		keyTimerTask.threadSuspended = false;
	}


/** Applet°±¤î®É¡A°±¤î¼½­µ */
	public void stop() {
		sp.pause();
		timerTask.threadSuspended = true;
		keyTimerTask.threadSuspended = true;
	}


/** Appletµ²§ô®É¡Aµ²§ô°õ¦æºü */
	public void destroy() {
		timerTask.threadStoped = true;
		keyTimerTask.threadStoped = true;
		try {
			timerTask.join();
			keyTimerTask.join();
		} catch (Exception e) {
			infomation("Error : I can not finish destroy().", e);
		}
	}


/**
 * ³B²z·Æ¹«¨Æ¥ó¡]¦bµøµ¡¤W¡B«ö¤U«öÁä¡^¡C
 * «ö¤U«ö¶s»P©ñ¶}«ö¶s®É¡A¨Æ¥ó³£·|¦s¤JQueue¡C
 * @param e ·Æ¹«¨Æ¥ó
 */
	public void processMouseEvent(MouseEvent e) {
		switch(e.getID()) {
			case MouseEvent.MOUSE_ENTERED:
				mouseOnFrame = true;
				break;
			case MouseEvent.MOUSE_EXITED:
				mouseOnFrame = false;
				break;
			case MouseEvent.MOUSE_PRESSED:
			case MouseEvent.MOUSE_RELEASED:
				if(mouseOnFrame)
					mouseQ.enqueue(new InputEventTiny(e.getID(),
						 e.getX() - getInsets().left,
						 e.getY() - getInsets().top));
				break;
		}
	}


/**
 * ³B²z·Æ¹«¨Æ¥ó¡]²¾°Ê¡^¡C
 * ·í·Æ¹«®y¼Ğ§ïÅÜ®É¡A±N¨Æ¥ó¦s¤JQueue¡C
 * @param e ·Æ¹«¨Æ¥ó
 */
	public void processMouseMotionEvent(MouseEvent e) {
		if(!mouseOnFrame || e.getID() != MouseEvent.MOUSE_MOVED
			|| e.getX() < getInsets().left
			|| e.getX() > CANVAS_SIZE_W + getInsets().left
			|| e.getY() < getInsets().top
			|| e.getY() > CANVAS_SIZE_H + getInsets().top)
			return;
		mouseQ.enqueue(new InputEventTiny(e.getID(),
			e.getX() - getInsets().left, e.getY() - getInsets().top));
	}


/**
 * ³B²zÁä½L¨Æ¥ó¡C
 * ±NÁä½L¨Æ¥ó¦s¤JQueue¡C
 * ¤è¦VÁä³¡¥÷¨Ã°t¦XKeyRepeaterª«¥ó³B²z®×¥ó­«ÂĞ¿é¤J¡C
 * @param e ƒL[ƒCƒx?ƒg
 */
	public void processKeyEvent(KeyEvent e) {
		switch(e.getID()) {
			case KeyEvent.KEY_PRESSED:
				switch(e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						if(!pressDown)
							keyQ.enqueue(new InputEventTiny(e.getID(),
								e.getKeyCode()));
						pressDown = true;
						break;
					case KeyEvent.VK_UP:
						if(!pressUp)
							keyQ.enqueue(new InputEventTiny(e.getID(),
								e.getKeyCode()));
						pressUp = true;
						break;
					case KeyEvent.VK_RIGHT:
						if(!pressRight)
							keyQ.enqueue(new InputEventTiny(e.getID(),
								e.getKeyCode()));
						pressRight = true;
						break;
					case KeyEvent.VK_LEFT:
						if(!pressLeft)
							keyQ.enqueue(new InputEventTiny(e.getID(),
								e.getKeyCode()));
						pressLeft = true;
						break;
					default:
						keyQ.enqueue(new InputEventTiny(e.getID(),
							e.getKeyCode()));
						break;
				}
				break;
			case KeyEvent.KEY_RELEASED:
				switch(e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						pressDown = false;
						break;
					case KeyEvent.VK_UP:
						pressUp = false;
						break;
					case KeyEvent.VK_RIGHT:
						pressRight = false;
						break;
					case KeyEvent.VK_LEFT:
						pressLeft = false;
						break;
				}
				keyQ.enqueue(new InputEventTiny(e.getID(), e.getKeyCode()));
				break;
		}
	}


/**
 * Ã¸¹Ï¡C
 * ­n¨DSpriteÃş§O¶i¦æÃ¸¹Ï¡C
 * °£¤Frepaint¤èªk­n¨Dªº­«Ã¸¥H¥~¡A
 * ¤]´N¬O±q³Ì¤p¤Æ«ì´_­ìª¬¡B±q³QÂĞ»\ªº±¡ªp­«·s¦^¨ì§@¥Î¤¤µøµ¡µ¥®É­Ô¡A
 * «h±j¨î¨ú±oµJÂI¡A±µ¨üÁä½L¨Æ¥ó¡C
 * ¤§©Ò¥H»İ­n±j¨î¨ú±oµJÂI¡A
 * ¬O¦]¬°Applet¨S¦³³Q·Æ¹«ÂI¹L´NµLªk¦¬¨ìÁä½L¨Æ¥ó¡C
 * @param g Ã¸¹Ï¹ï¶HªºGraphics²ÕºA
 */
	public void paint(Graphics g) {
		sprite.paintScreen(g);
		requestFocus();
	}


/**
 * §ó·sµe­±¡C
 * repaint¤èªk·|©I¥s³o­Ó¤èªk¡A­n¨DSpriteÃş§O¶i¦æÃ¸¹Ï¡C
 * ¦b³o¸Ì¤£¨ú±oµJÂI¡C
 * @param g Ã¸¹Ï¹ï¶HªºGraphics²ÕºA
 */
	public void update(Graphics g) {
		sprite.paintScreen(g);
	}


/**
 * ¥Î¨Ó¹ê²{«öÁä­«½Æ¿é¤JªºÃş§O¡C<br>
 * ¹ê§@¤è¦VÁäªº«öÁä­«½Æ¿é¤J¾÷¨î¡C
 * «öÁä­«½Æ¿é¤Jªº®É¶¡¶¡¹j¡B¶}©lªº©µ¿ğ®É¶¡¡A
 * ¨Ì·ÓGame2DÃş§OªºKEY_SEED¡BKEY_DELAY³]©w­È¡C
 * @author mnagaku
 */
	class KeyRepeater extends Thread {
/** ³]©w¬°true®É¡A«h¼È®É°±¤î«öÁä­«½Æ¿é¤Jªº³B²z */
		boolean threadSuspended = false;
/** ³]©w¬°true®É¡A«h°±¤î«öÁä­«½Æ¿é¤Jªº³B²z */
		boolean threadStoped = false;

/**
 * ­p®É¾¹¤¤Â_³B²zªº¥»Åé¡C
 * ¨C¹j¤@©w®É¶¡µo¥ÍÁä½L­«½Æ¿é¤J¨Æ¥ó¡C
 */
		public void run() {
			long processTime, pressDownCount = 0, pressUpCount = 0,
				pressRightCount = 0, pressLeftCount = 0;
			while(!threadStoped) {
				try {
					processTime = System.currentTimeMillis();
// ¦pªG¦³«ö¤U«öÁäº¡¨¬±ø¥ó®É¡A©ñ¤JQueue
					if(pressDown) {
					 	if(pressDownCount > KEY_DELAY)
							keyQ.enqueue(new InputEventTiny(
								KeyEvent.KEY_PRESSED, KeyEvent.VK_DOWN));
						else
							pressDownCount++;
					}
					else
						pressDownCount = 0;
					if(pressUp) {
					 	if(pressUpCount > KEY_DELAY)
							keyQ.enqueue(new InputEventTiny(
								KeyEvent.KEY_PRESSED, KeyEvent.VK_UP));
						else
							pressUpCount++;
					}
					else
						pressUpCount = 0;
					if(pressRight) {
					 	if(pressRightCount > KEY_DELAY)
							keyQ.enqueue(new InputEventTiny(
								KeyEvent.KEY_PRESSED, KeyEvent.VK_RIGHT));
						else
							pressRightCount++;
					}
					else
						pressRightCount = 0;
					if(pressLeft) {
					 	if(pressLeftCount > KEY_DELAY)
							keyQ.enqueue(new InputEventTiny(
								KeyEvent.KEY_PRESSED, KeyEvent.VK_LEFT));
						else
							pressLeftCount++;
					}
					else
						pressLeftCount = 0;
// KEY_SPEEDªºµ¥«İ®É¶¡
					processTime = System.currentTimeMillis() - processTime;
					if(KEY_SPEED - processTime < 0)
						infomation("Warning : Processing delay in KeyRepeater.",
							null);
					else
						sleep(KEY_SPEED - processTime);

					while(threadSuspended && !threadStoped)
						yield();

				} catch (Exception e) {
					infomation("Error : Problem occurred in KeyRepeater.", e);
				}
			}
		}
	}


/**
 * ¥Î¨Ó¹ê²{­«Ã¸¡B¥D°j°éªºÃş§O¡C<br>
 * ¥Hrun¤èªk¤ºªº°j°é¥H©T©w®É¶¡¨µ°j¡A¹ê²{©w®Éªº­p®É¾¹¡C
 * @author mnagaku
 */
	class MainLoop extends Thread {
/** ³]©w¬°true®É¡A«h¼È®É°±¤î¥D°j°éªº³B²z */
		boolean threadSuspended = false;
/** ³]©w¬°true®É¡A«h°±¤î¥D°j°éªº³B²z */
		boolean threadStoped = false;

/**
 * ­p®É¾¹¤¤Â_ªº¥»Åé¡C
 * ·|¨C¹j¤@©w®É¶¡©I¥sGame2DMainÃş§O
 * ¡]ªº¤lÃş§O¡^ªºmainLoop¤èªk¡A
 * ²{¥D°j°éªº¾÷¨î¡A
 * ¨Ã¹ïSprite°e¥X­«Ã¸½Ğ¨D¡C
 */
		public void run() {
			long processTime = 0;
			while(!threadStoped) {
				try {
					processTime = System.currentTimeMillis();
// ¶i¦æ¤@¦¸mainLoop³B²z
					gm.mainLoop();
// «ö·Ó¸ê°T­«Ã¸
					repaint();
// SPEEDµ¥«İ®É¶¡
					processTime = System.currentTimeMillis() - processTime;
					if(SPEED - processTime < 0)
						infomation("Warning : Processing delay in MainLoop.",
							null);
					else
						sleep(SPEED - processTime);

					while(threadSuspended && !threadStoped)
						yield();

				} catch (Exception e) {
					infomation("Error : Problem occurred in MainLoop.", e);
					System.exit(0);
				}
			}
		}
	}


/**
 * Game2DMainÃş§O<br>
 * ¹CÀ¸¥»Åéªº¤u§@
 * @author mnagaku
 */
	abstract public class Game2DMain {

/**
 * «Øºc¤l
 * ¹CÀ¸¥»Åéªº¤u§@¤§ªì©l¤Æ
*/
//		Game2DMain() {}


/**
 * ¥D°j°é¤@°é¥÷ªº³B²z¡C
 * ¼g¤J¨C­Ó©T©w®É¶¡´N¥²¶·¶i¦æªº°Ê§@¡C
 * µe­±ªº­«Ã¸·|¦b³o­Ó¤èªkµ²§ô«á¦Û°Ê¶i¦æ¡C
 */
		abstract public boolean mainLoop();
	}
}
