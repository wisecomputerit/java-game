// GameAudioClip.java
// written by mnagaku 

import java.applet.*;
import java.net.*;

public class GameAudioClip implements AudioClip {

	static Applet owner = null;

	static String version = null;

	AudioClip ac;

	public GameAudioClip(String file) {
		getAudioClip(file);
	}

	public GameAudioClip(String file, Applet owner) {
		setOwner(owner);
		getAudioClip(file);
	}

	void getAudioClip(String file) {
		if(version == null)
			version = System.getProperty("java.version");

		URL url = getClass().getResource(file);

		if(version.compareTo("1.2.0") >= 0)
			ac = Applet.newAudioClip(url);
		else if(owner != null)
			ac = owner.getAudioClip(url);
		else
			ac = new sun.applet.AppletAudioClip(url);
	}

	static void setOwner(Applet realOwner) {
		if(realOwner instanceof Applet)
			owner = realOwner;
	}

	public void loop() {
		if(ac == null)
			return;
		ac.loop();
	}

	public void play() {
		if(ac == null)
			return;
		ac.play();
	}

	public void stop() {
		if(ac == null)
			return;
		ac.stop();
	}
}

