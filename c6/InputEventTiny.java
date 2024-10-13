// InputEventTiny.java
// written by mnagaku

/**
 * InputEventTiny類別<br>
 * 存放鍵盤、滑鼠等狀態的類別。
 * @author mnagaku
 */
public class InputEventTiny {

/** 動作（代表滑鼠事件、鍵盤事件的ID值） */
	int id;

/** 發生滑鼠事件的X座標 */
	int x;

/** 發生滑鼠事件的Y座標 */
	int y;

/** 發生鍵盤事件的按鍵碼 */
	int keyCode;

/** 建構子
 * 滑鼠事件用
*/
	InputEventTiny(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.keyCode = 0;
	}


/** 建構子
 * 鍵盤事件用
*/
	InputEventTiny(int id, int keyCode) {
		this.id = id;
		this.x = 0;
		this.y = 0;
		this.keyCode = keyCode;
	}


/** 取得ID（動作） */
	public int getID() {
		return id;
	}


/** 取得X座標 */
	public int getX() {
		return x;
	}


/** 取得Y座標 */
	public int getY() {
		return y;
	}


/** 取得KeyCode */
	public int getKeyCode() {
		return keyCode;
	}
}

