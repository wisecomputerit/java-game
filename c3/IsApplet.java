// IsApplet.java
// written by mnagaku 

import java.applet.*;

public class IsApplet {

	public static void main(String args[]) {
		String obj1 = new String("test");
		System.out.println(isApplet(obj1));
		Applet obj2 = new Applet();
		System.out.println(isApplet(obj2));
	}

	public static boolean isApplet(Object obj) {
		return obj instanceof Applet;
	}
}

