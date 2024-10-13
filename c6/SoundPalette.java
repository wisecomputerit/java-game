// SoundPalette.java
// written by mnagaku

import java.util.*;
import java.applet.*;

/**
 * 用來管理聲音的SoundPalette類別。<br>
 * 先登錄(讀入)BGM與SE，
 * 在需要的時候可以隨時播放只能播放AudioClip所支援的格式。
 * @author mnagaku
 */
public class SoundPalette {

/** 用來存放讀入之BGM的pool */
	Hashtable bgms;

/** 用來存放讀入之SE的pool */
	Hashtable ses;

/** JVM的版本 */
	int javaVersion;

/** 播放中的BGM */
	int nowBgm;

/** 播放中的SE */
	AudioClip nowSe = null;

/** 程式執行狀態是否為Applet */
	boolean isApplet;

/** 呼叫SoundPalette的Applet */
	Applet owner;


/**
 * 建構子
 * 建構子產生pool。呼叫無引數的建構子時認定程式不是Applet
*/
	public SoundPalette() {
		this(null);
	}


/**
 * 建構子
 * 建構子產生pool。並判斷程式是否為Applet
 * @param owner 管理SoundPalette的Applet
*/
	public SoundPalette(Applet owner) {
		this.owner = null;
		this.isApplet = false;
		if(owner instanceof Applet) {
			this.owner = owner;
			this.isApplet = true;
		}

		bgms = new Hashtable();
		ses = new Hashtable();

		String javaVersionStr = System.getProperty("java.version");
		if(javaVersionStr.compareTo("1.1.0") < 0)
			javaVersion = 10;
		else if(javaVersionStr.compareTo("1.2.0") < 0)
			javaVersion = 11;
		else
			javaVersion = 12;
	}

/**
 * 將資料讀入pool。
 * 指定要將什麼名稱的資料讀入pool的哪個位置裡。
 * @param no 用來存放讀入之資料的pool位置(索引)
 * @param file 要讀入的資料檔名
 * @param pool 對象的pool
 * @return 正常讀取時傳回true
*/
	boolean loadData(int no, String file, Hashtable pool) {
		AudioClip ac = null;
		try {
			switch(javaVersion) {
				case 11:
					if(isApplet)
						ac = owner.getAudioClip(getClass().getResource(file));
					else
						ac = newAudioClip4Sun(file);
					break;
				case 12:
					ac = Applet.newAudioClip(getClass().getResource(file));
					break;
			}
		} catch(Exception e) {
			System.out.println("Warning : SoundPalette is unplayable.");
			System.out.println("java.version : "
				+ System.getProperty("java.version"));
			System.out.println("java.vendor : "
				+ System.getProperty("java.vendor"));
			e.printStackTrace();
		}
		if(ac == null)
			return false;
		pool.put(new Integer(no), ac);
		return true;
	}

/**
 * 使用sun的內部類別讀入聲音資料
 * 用於想在Java1.1.x版的JVM，以非applet的遊戲處理聲音時。
 * @param file 要讀入的資料檔名
 * @return 讀入的AudioClip
*/
	AudioClip newAudioClip4Sun(String file) {
		AudioClip ret;
		try {
			Class.forName("sun.applet.AppletAudioClip");
			ret = new sun.applet.AppletAudioClip(getClass().getResource(file));
		} catch(Exception e) {
			ret = null;
		}
		return ret;
	}


/**
 * 將BGM讀入BGM pool
 * 指定要將什麼名稱的BGM資料讀入pool的哪個位置裡。
 * @param no 用來存放讀入之BGM的pool位置(索引)
 * @param file 要讀入的BGM檔名
 * @return 正常讀取時傳回true
*/
	public boolean addBgm(int no, String file) {
		return loadData(no, file, bgms);
	}


/**
 * 將SE讀入BGM pool
 * 指定要將什麼名稱的SE資料讀入pool的哪個位置裡。
 * @param no 用來存放讀入之SE的pool位置(索引)
 * @param file 要讀入的SE檔名
 * @return 正常讀取時傳回true
*/
	public boolean addSe(int no, String file) {
		return loadData(no, file, ses);
	}


/** 從pool取得ac */
	AudioClip getAc(int no, Hashtable pool) {
		AudioClip ac = null;
		ac = (AudioClip)pool.get(new Integer(no));
		return ac;
	}


/** 暫停聲音 */
	public boolean pause() {
		AudioClip ac = getAc(nowBgm, bgms);
		if(ac == null)
			return false;
		ac.stop();
		return true;
	}


/** 重新播放聲音 */
	public boolean restart() {
		AudioClip ac = getAc(nowBgm, bgms);
		if(ac == null)
			return false;
		ac.loop();
		return true;
	}


/** 播放BGM */
	public boolean playBgm(int no) {
		AudioClip ac = getAc(nowBgm, bgms);
		if(ac != null)
		ac.stop();
		nowBgm = no;
		ac = getAc(nowBgm, bgms);
		if(ac == null)
			return false;
		ac.loop();
		return true;
	}


/** 播放SE */
	public boolean playSe(int no) {
		if(nowSe != null)
			nowSe.stop();
		nowSe = getAc(no, ses);
		if(nowSe == null)
			return false;
		nowSe.play();
		return true;
	}
}

