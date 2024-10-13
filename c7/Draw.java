// Draw.java
// written by mnagaku

import java.awt.*;

/**
 * 用來在Sprite上繪圖的介面。<br>
 * @author mnagaku
*/
interface Draw {

/**
 * 實作繪圖的動作。
 * 實作Sprite的繪圖資訊。
 * @param g 繪圖面
 * @param plane 平面
 * @return 繪圖成功時傳回true
*/
	public boolean drawing(Graphics g, Plane plane);
}
