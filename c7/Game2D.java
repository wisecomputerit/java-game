// Game2D.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.lang.reflect.*;

/**
 * 2D game framework class Game2D<br>
 * �޲z2D�C���{���һݤ�Sprite�B�n���B��J��framework�C
 * extends�o�����O�}�o2D�C���C
 * �F�bMacOS9���e������MRJ2.2.5�H��MSVM�W���i�H�ϥΡA�o�̨ϥΰ����
 * ��@�p�ɾ��A��Java1.1.x������䴩�C
 * <br>
 * extends�o�����O��Game���O�A
 * �i�ΤU�C��HTML�ԭz�A�HApplet���Φ��I�s�C
 * �]Applet�w�gjar�Ʈɡ^
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

/** �C���W�١C�|�qGame2D���O���l���O�W�ٲ��� */
	static String GAME_NAME;

/** Game2DMain���O���l���O���W�� */
	static String GAME_MAIN_NAME;

/** ����ø�ϰϰ�e�� */
	int CANVAS_SIZE_W = 320;
/** ����ø�ϰϰ찪�� */
	int CANVAS_SIZE_H = 240;

/** �e����ø�B�D�j��B�z���t�סC���O�@�� */
	int SPEED = 100;

/** ��V�䪺���䭫�ƿ�J�t�סC���O�@�� */
	int KEY_SPEED = 50;
/** ��V�䪺���䭫�ƿ�J�@�}�l������ɶ��C���OGET_KEY_SPEED������ */
	int KEY_DELAY = 3;

/** �x�s������U�����A */
	boolean pressUp = false, pressDown = false,
		pressLeft = false, pressRight = false;
/** �x�s�ƹ��O�_�b�ϰ줺 */
	boolean mouseOnFrame = false;

/** �x�s�O�HApplet�Φ������٬OApplication�Φ� */
	boolean appletFlag = true;

/** Sprite���O�C�P�ϧά������ʧ@���浹Sprite���O */
	Sprite sprite;

/** SoundPalette���O�C�PBGM�BSE�������B�z���浹SoundPalette���O */
	SoundPalette sp;

/** Game2DMain���O�C�C�����骺�u�@�浹Game2DMain���O */
	Game2DMain gm;

/** ��L�ƥ�Queue�C��V�䪺��L���Ʀ��ġC */
	Queue keyQ;
/** �ƹ��ƥ�Queue�C */
	Queue mouseQ;

/** �s��Ψӹ�@��ø�B�D�j����MainLoop����C */
	MainLoop timerTask;
/** �s��Ψӹ�{��L���ƿ�J���KeyRepeater����C */
	KeyRepeater keyTimerTask;


/**
 * �غc�l
 * �۰ʷj�M�~��Game2DMain�����O�W�٫�s�JGAME_MAIN_NAME�C
 * �򥻤W�|�۰ʷj�M�A���ѩ�Java1.1.x�L�k�j�M�A�ҥH
 * GAME_MAIN_NAME = GAME_NAME + "$" + GAME_NAME + "Main";
 * �Ƃ���B
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
 * ��ܿ��~�Bĵ�i�C
 * @param info �n��ܪ��r��C�H�uError�v�}�l���r��A�|�j����{���C
 * @param e �o�ͨҥ~�ɡA�Y�N�ҥ~�]�Ƕi�ӡA�N�|��ܨҥ~����T�C
 * �S���ҥ~�h�ǤJnull�C
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
 * ���OApplet�ɡA��main()�I�s�غc�l���~�ߡC
 * @param game2dClassName �n�إߪ�Game2D�l���O���W��
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
 * ��Game2D���O�ҩI�s��Game2DMain���O�غc�l���~�ߡC
 * ��ڤW�O�ϥ�GAME_MAIN_NAME��
 * �ҳ]�w��Game2DMain�l���O���W�١A�I�s��غc�l�C
 * @return �إߥX��Game2DMain�l���O������
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
 * �ΨӶ}�l�������ε{���C
 * �bmain��k�ۧګغc��I�s�C
 * �|�إߥX�����A�b�̭����Applet�C
 */
	public void newGame2D() {
// Applet�}�l����
		appletFlag = false;
		init();

// �إ����ε{������
		Frame frame = new Frame(GAME_NAME);
		frame.pack();
		frame.setVisible(true);
		frame.setVisible(false);
		frame.pack();

// ������ܦb�ù�����
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width-CANVAS_SIZE_W)/2,
			(d.height-CANVAS_SIZE_H)/2);

// �]�w�����j�p
		int left, right, top, bottom;
		left = frame.getInsets().left;
		right = frame.getInsets().right;
		top = frame.getInsets().top;
		bottom = frame.getInsets().bottom;
		frame.setSize(CANVAS_SIZE_W + left + right,
			CANVAS_SIZE_H + top + bottom);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
// �p�G�����ܦ��ϥܫh�n������
			public void windowIconified(WindowEvent e) {stop();}
// �p�G�����q�ϥ��٭�h�h�}��BGM
			public void windowDeiconified(WindowEvent e) {start();}
		});
		frame.setResizable(false);

		frame.add(this);
// ��ܵ���
		frame.setVisible(true);
// �T�w�����j�p
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
 * �}�l����Applet�C
 * �ǳƹC��������C
 * �}�l�i�歫ø�B�D�j��B�ƥ�B�z���ʧ@�C
 */
	public void init() {
// ���o�P�]�w�ƥ�
		enableEvents(AWTEvent.MOUSE_EVENT_MASK |
			AWTEvent.MOUSE_MOTION_EVENT_MASK |
			AWTEvent.KEY_EVENT_MASK);
// Sprite�B�n���B��J�����޲z�P�ͦ�
		keyQ = new Queue();
		mouseQ = new Queue();
		if(appletFlag)
			sp = new SoundPalette(this);
		else
			sp = new SoundPalette();
		sprite = new Sprite(CANVAS_SIZE_W, CANVAS_SIZE_H, this);
// �C�������骺�إ�
		gm = newGame2DMain();
// ��ø�B�D�j�骺�إ�
		timerTask = new MainLoop();
		timerTask.start();
// �ƥ�B�z���إ�
		keyTimerTask = new KeyRepeater();
		keyTimerTask.start();
// ���o�J�I
		requestFocus();
	}


/** �^��Applet�ɡA����BGM */
	public void start() {
		sp.restart();
		timerTask.threadSuspended = false;
		keyTimerTask.threadSuspended = false;
	}


/** Applet����ɡA����� */
	public void stop() {
		sp.pause();
		timerTask.threadSuspended = true;
		keyTimerTask.threadSuspended = true;
	}


/** Applet�����ɡA��������� */
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
 * �B�z�ƹ��ƥ�]�b�����W�B���U����^�C
 * ���U���s�P��}���s�ɡA�ƥ󳣷|�s�JQueue�C
 * @param e �ƹ��ƥ�
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
 * �B�z�ƹ��ƥ�]���ʡ^�C
 * ��ƹ��y�Ч��ܮɡA�N�ƥ�s�JQueue�C
 * @param e �ƹ��ƥ�
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
 * �B�z��L�ƥ�C
 * �N��L�ƥ�s�JQueue�C
 * ��V�䳡���ðt�XKeyRepeater����B�z�ץ��п�J�C
 * @param e �L�[�C�x?�g
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
 * ø�ϡC
 * �n�DSprite���O�i��ø�ϡC
 * ���Frepaint��k�n�D����ø�H�~�A
 * �]�N�O�q�̤p�ƫ�_�쪬�B�q�Q�л\�����p���s�^��@�Τ��������ɭԡA
 * �h�j����o�J�I�A������L�ƥ�C
 * ���ҥH�ݭn�j����o�J�I�A
 * �O�]��Applet�S���Q�ƹ��I�L�N�L�k������L�ƥ�C
 * @param g ø�Ϲ�H��Graphics�պA
 */
	public void paint(Graphics g) {
		sprite.paintScreen(g);
		requestFocus();
	}


/**
 * ��s�e���C
 * repaint��k�|�I�s�o�Ӥ�k�A�n�DSprite���O�i��ø�ϡC
 * �b�o�̤����o�J�I�C
 * @param g ø�Ϲ�H��Graphics�պA
 */
	public void update(Graphics g) {
		sprite.paintScreen(g);
	}


/**
 * �Ψӹ�{���䭫�ƿ�J�����O�C<br>
 * ��@��V�䪺���䭫�ƿ�J����C
 * ���䭫�ƿ�J���ɶ����j�B�}�l������ɶ��A
 * �̷�Game2D���O��KEY_SEED�BKEY_DELAY�]�w�ȡC
 * @author mnagaku
 */
	class KeyRepeater extends Thread {
/** �]�w��true�ɡA�h�Ȯɰ�����䭫�ƿ�J���B�z */
		boolean threadSuspended = false;
/** �]�w��true�ɡA�h������䭫�ƿ�J���B�z */
		boolean threadStoped = false;

/**
 * �p�ɾ����_�B�z������C
 * �C�j�@�w�ɶ��o����L���ƿ�J�ƥ�C
 */
		public void run() {
			long processTime, pressDownCount = 0, pressUpCount = 0,
				pressRightCount = 0, pressLeftCount = 0;
			while(!threadStoped) {
				try {
					processTime = System.currentTimeMillis();
// �p�G�����U���亡������ɡA��JQueue
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
// KEY_SPEED�����ݮɶ�
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
 * �Ψӹ�{��ø�B�D�j�骺���O�C<br>
 * �Hrun��k�����j��H�T�w�ɶ����j�A��{�w�ɪ��p�ɾ��C
 * @author mnagaku
 */
	class MainLoop extends Thread {
/** �]�w��true�ɡA�h�Ȯɰ���D�j�骺�B�z */
		boolean threadSuspended = false;
/** �]�w��true�ɡA�h����D�j�骺�B�z */
		boolean threadStoped = false;

/**
 * �p�ɾ����_������C
 * �|�C�j�@�w�ɶ��I�sGame2DMain���O
 * �]���l���O�^��mainLoop��k�A
 * �{�D�j�骺����A
 * �ù�Sprite�e�X��ø�ШD�C
 */
		public void run() {
			long processTime = 0;
			while(!threadStoped) {
				try {
					processTime = System.currentTimeMillis();
// �i��@��mainLoop�B�z
					gm.mainLoop();
// ���Ӹ�T��ø
					repaint();
// SPEED���ݮɶ�
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
 * Game2DMain���O<br>
 * �C�����骺�u�@
 * @author mnagaku
 */
	abstract public class Game2DMain {

/**
 * �غc�l
 * �C�����骺�u�@����l��
*/
//		Game2DMain() {}


/**
 * �D�j��@������B�z�C
 * �g�J�C�өT�w�ɶ��N�����i�檺�ʧ@�C
 * �e������ø�|�b�o�Ӥ�k������۰ʶi��C
 */
		abstract public boolean mainLoop();
	}
}
