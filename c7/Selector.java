// Selector.java
// written by mnagaku

import java.awt.*;

// ®Ø®Ø
class Selector implements Draw {

	static int pos = 0;
	static final int line = 5;

	public boolean drawing(Graphics g, Plane pln) {
		g.setColor(Color.red);
		for(int i = 0; i < line; i++)
			g.drawRect(75 * pos + i, 361 + i, 75 - i * 2, 39 - i * 2);
		return true;
	}
}
