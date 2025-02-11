// Game2D.java
// written by mnagaku

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.lang.reflect.*;

/**
 * 2D game framework class Game2D<br>
 * 管理2D遊戲程式所需之Sprite、聲音、輸入的framework。
 * extends這個類別開發2D遊戲。
 * 了在MacOS9之前的環境MRJ2.2.5以及MSVM上都可以使用，這裡使用執行緒
 * 實作計時器，讓Java1.1.x版能夠支援。
 * <br>
 * extends這個類別的Game類別，
 * 可用下列的HTML敘述，以Applet的形式呼叫。
 * （Applet已經jar化時）
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

/** 遊戲名稱。會從Game2D類別的子類別名稱產生 */
	static String GAME_NAME;

/** Game2DMain類別之子類別的名稱 */
	static String GAME_MAIN_NAME;

/** 視窗繪圖區域寬度 */
	int CANVAS_SIZE_W = 320;
/** 視窗繪圖區域高度 */
	int CANVAS_SIZE_H = 240;

/** 畫面重繪、主迴圈處理的速度。單位是毫秒 */
	int SPEED = 100;

/** 方向鍵的按鍵重複輸入速度。單位是毫秒 */
	int KEY_SPEED = 50;
/** 方向鍵的按鍵重複輸入一開始的延遲時間。單位是GET_KEY_SPEED的次數 */
	int KEY_DELAY = 3;

/** 儲存按鍵按下的狀態 */
	boolean pressUp = false, pressDown = false,
		pressLeft = false, pressRight = false;
/** 儲存滑鼠是否在區域內 */
	boolean mouseOnFrame = false;

/** 儲存是以Applet形式執行還是Application形式 */
	boolean appletFlag = true;

/** Sprite類別。與圖形相關的動作都交給Sprite類別 */
	Sprite sprite;

/** SoundPalette類別。與BGM、SE相關的處理都交給SoundPalette類別 */
	SoundPalette sp;

/** Game2DMain類別。遊戲本體的工作交給Game2DMain類別 */
	Game2DMain gm;

/** 鍵盤事件的Queue。方向鍵的鍵盤重複有效。 */
	Queue keyQ;
/** 滑鼠事件的Queue。 */
	Queue mouseQ;

/** 存放用來實作重繪、主迴圈機制的MainLoop物件。 */
	MainLoop timerTask;
/** 存放用來實現鍵盤重複輸入機制的KeyRepeater物件。 */
	KeyRepeater keyTimerTask;


/**
 * 建構子
 * 自動搜尋繼承Game2DMain的類別名稱後存入GAME_MAIN_NAME。
 * 基本上會自動搜尋，但由於Java1.1.x無法搜尋，所以
 * GAME_MAIN_NAME = GAME_NAME + "$" + GAME_NAME + "Main";
 * �������B
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
 * 顯示錯誤、警告。
 * @param info 要顯示的字串。以「Error」開始的字串，會強制結束程式。
 * @param e 發生例外時，若將例外也傳進來，就會顯示例外的資訊。
 * 沒有例外則傳入null。
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
 * 不是Applet時，由main()呼叫建構子的外殼。
 * @param game2dClassName 要建立的Game2D子類別的名稱
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
 * 由Game2D類別所呼叫的Game2DMain類別建構子的外殼。
 * 實際上是使用GAME_MAIN_NAME裡
 * 所設定的Game2DMain子類別的名稱，呼叫其建構子。
 * @return 建立出的Game2DMain子類別的物件
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
 * 用來開始執行應用程式。
 * 在main方法自我建構後呼叫。
 * 會建立出視窗，在裡面顯示Applet。
 */
	public void newGame2D() {
// Applet開始執行
		appletFlag = false;
		init();

// 建立應用程式視窗
		Frame frame = new Frame(GAME_NAME);
		frame.pack();
		frame.setVisible(true);
		frame.setVisible(false);
		frame.pack();

// 視窗顯示在螢幕中央
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width-CANVAS_SIZE_W)/2,
			(d.height-CANVAS_SIZE_H)/2);

// 設定視窗大小
		int left, right, top, bottom;
		left = frame.getInsets().left;
		right = frame.getInsets().right;
		top = frame.getInsets().top;
		bottom = frame.getInsets().bottom;
		frame.setSize(CANVAS_SIZE_W + left + right,
			CANVAS_SIZE_H + top + bottom);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
// 如果視窗變成圖示則聲音停止
			public void windowIconified(WindowEvent e) {stop();}
// 如果視窗從圖示還原則則開啟BGM
			public void windowDeiconified(WindowEvent e) {start();}
		});
		frame.setResizable(false);

		frame.add(this);
// 顯示視窗
		frame.setVisible(true);
// 確定視窗大小
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
 * 開始執行Applet。
 * 準備遊戲的本體。
 * 開始進行重繪、主迴圈、事件處理等動作。
 */
	public void init() {
// 取得與設定事件
		enableEvents(AWTEvent.MOUSE_EVENT_MASK |
			AWTEvent.MOUSE_MOTION_EVENT_MASK |
			AWTEvent.KEY_EVENT_MASK);
// Sprite、聲音、輸入等的管理與生成
		keyQ = new Queue();
		mouseQ = new Queue();
		if(appletFlag)
			sp = new SoundPalette(this);
		else
			sp = new SoundPalette();
		sprite = new Sprite(CANVAS_SIZE_W, CANVAS_SIZE_H, this);
// 遊戲的本體的建立
		gm = newGame2DMain();
// 重繪、主迴圈的建立
		timerTask = new MainLoop();
		timerTask.start();
// 事件處理的建立
		keyTimerTask = new KeyRepeater();
		keyTimerTask.start();
// 取得焦點
		requestFocus();
	}


/** 回到Applet時，播放BGM */
	public void start() {
		sp.restart();
		timerTask.threadSuspended = false;
		keyTimerTask.threadSuspended = false;
	}


/** Applet停止時，停止播音 */
	public void stop() {
		sp.pause();
		timerTask.threadSuspended = true;
		keyTimerTask.threadSuspended = true;
	}


/** Applet結束時，結束執行緒 */
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
 * 處理滑鼠事件（在視窗上、按下按鍵）。
 * 按下按鈕與放開按鈕時，事件都會存入Queue。
 * @param e 滑鼠事件
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
 * 處理滑鼠事件（移動）。
 * 當滑鼠座標改變時，將事件存入Queue。
 * @param e 滑鼠事件
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
 * 處理鍵盤事件。
 * 將鍵盤事件存入Queue。
 * 方向鍵部份並配合KeyRepeater物件處理案件重覆輸入。
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
 * 繪圖。
 * 要求Sprite類別進行繪圖。
 * 除了repaint方法要求的重繪以外，
 * 也就是從最小化恢復原狀、從被覆蓋的情況重新回到作用中視窗等時候，
 * 則強制取得焦點，接受鍵盤事件。
 * 之所以需要強制取得焦點，
 * 是因為Applet沒有被滑鼠點過就無法收到鍵盤事件。
 * @param g 繪圖對象的Graphics組態
 */
	public void paint(Graphics g) {
		sprite.paintScreen(g);
		requestFocus();
	}


/**
 * 更新畫面。
 * repaint方法會呼叫這個方法，要求Sprite類別進行繪圖。
 * 在這裡不取得焦點。
 * @param g 繪圖對象的Graphics組態
 */
	public void update(Graphics g) {
		sprite.paintScreen(g);
	}


/**
 * 用來實現按鍵重複輸入的類別。<br>
 * 實作方向鍵的按鍵重複輸入機制。
 * 按鍵重複輸入的時間間隔、開始的延遲時間，
 * 依照Game2D類別的KEY_SEED、KEY_DELAY設定值。
 * @author mnagaku
 */
	class KeyRepeater extends Thread {
/** 設定為true時，則暫時停止按鍵重複輸入的處理 */
		boolean threadSuspended = false;
/** 設定為true時，則停止按鍵重複輸入的處理 */
		boolean threadStoped = false;

/**
 * 計時器中斷處理的本體。
 * 每隔一定時間發生鍵盤重複輸入事件。
 */
		public void run() {
			long processTime, pressDownCount = 0, pressUpCount = 0,
				pressRightCount = 0, pressLeftCount = 0;
			while(!threadStoped) {
				try {
					processTime = System.currentTimeMillis();
// 如果有按下按鍵滿足條件時，放入Queue
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
// KEY_SPEED的等待時間
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
 * 用來實現重繪、主迴圈的類別。<br>
 * 以run方法內的迴圈以固定時間巡迴，實現定時的計時器。
 * @author mnagaku
 */
	class MainLoop extends Thread {
/** 設定為true時，則暫時停止主迴圈的處理 */
		boolean threadSuspended = false;
/** 設定為true時，則停止主迴圈的處理 */
		boolean threadStoped = false;

/**
 * 計時器中斷的本體。
 * 會每隔一定時間呼叫Game2DMain類別
 * （的子類別）的mainLoop方法，
 * 現主迴圈的機制，
 * 並對Sprite送出重繪請求。
 */
		public void run() {
			long processTime = 0;
			while(!threadStoped) {
				try {
					processTime = System.currentTimeMillis();
// 進行一次mainLoop處理
					gm.mainLoop();
// 按照資訊重繪
					repaint();
// SPEED等待時間
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
 * Game2DMain類別<br>
 * 遊戲本體的工作
 * @author mnagaku
 */
	abstract public class Game2DMain {

/**
 * 建構子
 * 遊戲本體的工作之初始化
*/
//		Game2DMain() {}


/**
 * 主迴圈一圈份的處理。
 * 寫入每個固定時間就必須進行的動作。
 * 畫面的重繪會在這個方法結束後自動進行。
 */
		abstract public boolean mainLoop();
	}
}
