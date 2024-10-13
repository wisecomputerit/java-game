// SoundPalette.java
// written by mnagaku

import java.util.*;
import java.applet.*;

/**
 * �ΨӺ޲z�n����SoundPalette���O�C<br>
 * ���n��(Ū�J)BGM�PSE�A
 * �b�ݭn���ɭԥi�H�H�ɼ���u�༽��AudioClip�Ҥ䴩���榡�C
 * @author mnagaku
 */
public class SoundPalette {

/** �ΨӦs��Ū�J��BGM��pool */
	Hashtable bgms;

/** �ΨӦs��Ū�J��SE��pool */
	Hashtable ses;

/** JVM������ */
	int javaVersion;

/** ���񤤪�BGM */
	int nowBgm;

/** ���񤤪�SE */
	AudioClip nowSe = null;

/** �{�����檬�A�O�_��Applet */
	boolean isApplet;

/** �I�sSoundPalette��Applet */
	Applet owner;


/**
 * �غc�l
 * �غc�l����pool�C�I�s�L�޼ƪ��غc�l�ɻ{�w�{�����OApplet
*/
	public SoundPalette() {
		this(null);
	}


/**
 * �غc�l
 * �غc�l����pool�C�çP�_�{���O�_��Applet
 * @param owner �޲zSoundPalette��Applet
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
 * �N���Ū�Jpool�C
 * ���w�n�N����W�٪����Ū�Jpool�����Ӧ�m�̡C
 * @param no �ΨӦs��Ū�J����ƪ�pool��m(����)
 * @param file �nŪ�J������ɦW
 * @param pool ��H��pool
 * @return ���`Ū���ɶǦ^true
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
 * �ϥ�sun���������OŪ�J�n�����
 * �Ω�Q�bJava1.1.x����JVM�A�H�Dapplet���C���B�z�n���ɡC
 * @param file �nŪ�J������ɦW
 * @return Ū�J��AudioClip
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
 * �NBGMŪ�JBGM pool
 * ���w�n�N����W�٪�BGM���Ū�Jpool�����Ӧ�m�̡C
 * @param no �ΨӦs��Ū�J��BGM��pool��m(����)
 * @param file �nŪ�J��BGM�ɦW
 * @return ���`Ū���ɶǦ^true
*/
	public boolean addBgm(int no, String file) {
		return loadData(no, file, bgms);
	}


/**
 * �NSEŪ�JBGM pool
 * ���w�n�N����W�٪�SE���Ū�Jpool�����Ӧ�m�̡C
 * @param no �ΨӦs��Ū�J��SE��pool��m(����)
 * @param file �nŪ�J��SE�ɦW
 * @return ���`Ū���ɶǦ^true
*/
	public boolean addSe(int no, String file) {
		return loadData(no, file, ses);
	}


/** �qpool���oac */
	AudioClip getAc(int no, Hashtable pool) {
		AudioClip ac = null;
		ac = (AudioClip)pool.get(new Integer(no));
		return ac;
	}


/** �Ȱ��n�� */
	public boolean pause() {
		AudioClip ac = getAc(nowBgm, bgms);
		if(ac == null)
			return false;
		ac.stop();
		return true;
	}


/** ���s�����n�� */
	public boolean restart() {
		AudioClip ac = getAc(nowBgm, bgms);
		if(ac == null)
			return false;
		ac.loop();
		return true;
	}


/** ����BGM */
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


/** ����SE */
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

