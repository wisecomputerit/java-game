// GameObjects.java
// written by mnagaku

import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.lang.reflect.*;



/**
 * GameObjects√˛ßO<br>
 * πC¿∏µn≥ı™∫¶U∫ÿ™∫™´≈È™∫≥B≤z
 * @author mnagaku
 */
class GameObjects extends Hashtable {

	Sprite sprite;
	Queue keyQ;

	int count, overCount;
	int ownPlnNo, firstPlnNo;
/** §¿º∆ */
	int score = 0;


/** ´ÿ∫c§l */
	GameObjects(int no, Sprite sprite, Queue keyQ) {
		firstPlnNo = count = no;
		ownPlnNo = firstPlnNo + 100;
		overCount = ownPlnNo + 1;
		this.sprite = sprite;
		this.keyQ = keyQ;
	}


/** §¿º∆≤M¨∞0 */
	boolean clearScore() {
		score = 0;
		return true;
	}


/** ∂«¶^§¿º∆ */
	int getScore() {
		return score;
	}


/** ¶€æ˜¨O§£¨°µ€ */
	boolean isAliveOwn() {
		if(get(new Integer(ownPlnNo)) == null)
			return false;
		else
			return true;
	}


/** ∂«¶^¥›ælæ˜º∆∂q */
	int getLast() {
		return ((Own)get(new Integer(ownPlnNo))).getLast();
	}


/** ≤{¶b∫ﬁ≤z§§™´≈È≤M∞£±º */
	boolean clearGOS() {
		clearScore();
		GameObject go;
		Enumeration enum = elements();
		while(enum.hasMoreElements()) {
			go = ((GameObject)(enum.nextElement()));
			delGO(go.plnNo);
		}
		count = firstPlnNo;
		return true;
	}


/** µnø˝¶€æ˜´e§Ë™∫®§¶‚ */
	int addGOsetOverOwn(String className) {
		addGOsetPln(className, sprite.getPlanePosX(ownPlnNo),
			sprite.getPlanePosY(ownPlnNo), ownPlnNo, overCount);
		overCount++;
		return overCount - 1;
	}


/** µnø˝¶€æ˜ */
	int addGOOwn(String className, int x, int y, int plnNo) {
		ownPlnNo = plnNo;
		overCount = ownPlnNo + 1;
		addGOsetPln(className, x, y, ownPlnNo, ownPlnNo);
		return plnNo;
	}


/** µnø˝¶€æ˜´·§Ë™∫®§¶‚ */
	int addGO(String className, int x, int y, int targetPlnNo) {
		int ret = count;
		addGOsetPln(className, x, y, targetPlnNo, count);
		count++;
		if(count >= ownPlnNo)
			count = firstPlnNo;
		return ret;
	}


/** ´¸©w•≠≠±Ωs∏π§§™´≈È™∫µnø˝ */
	int addGOsetPln(String className, int x, int y, int targetPlnNo,
		int plnNo) {
		try {
			Class argClass[] = {Integer.TYPE, Sprite.class, Queue.class,
				Integer.TYPE, Integer.TYPE, GameObjects.class,
				Integer.TYPE};
			Constructor goCon
				= Class.forName(className).getConstructor(argClass);
			Integer no = new Integer(plnNo);
			Object initArgs[] = {no, sprite, keyQ,
				new Integer(x), new Integer(y), this,
				new Integer(targetPlnNo)};
			put(no, goCon.newInstance(initArgs));
		} catch(Exception e) {e.printStackTrace();}
		return plnNo;
	}


/** ´¸©w•≠≠±Ωs∏π§§™´≈È™∫ßR∞£ */
	void delGO(int plnNo) {
		sprite.delPlane(plnNo);
		remove(new Integer(plnNo));
	}


/** ≤æ∞ ≥Qµnø˝™∫•˛≥°™∫™´≈È */
	void moveAll() {
		Enumeration enum = elements();
		while(enum.hasMoreElements())
			((GameObject)(enum.nextElement())).move();
	}


/** ßP©w°uºƒæ˜°vªP°u¶€æ˜°˛¶€æ˜µoÆg™∫§lºu°vΩƒ¨ */
	void hitCheckOwnBow() {
		GameObject go;
		GameObject ownGo = null;
		Enumeration enum = elements();
		Enumeration enum2;
		while(enum.hasMoreElements()) {
			go = ((GameObject)(enum.nextElement()));
			if(go.attribute == GameObject.OWN_BOW) {
				ownGo = go;
				enum2 = elements();
				while(enum2.hasMoreElements()) {
					go = ((GameObject)(enum2.nextElement()));
					if(go.attribute != GameObject.ENEMY)
						continue;
					if(Math.abs(sprite.getPlanePosX(ownGo.plnNo)
						+ ownGo.hitX + ownGo.hitW / 2
						- sprite.getPlanePosX(go.plnNo)-go.hitX-go.hitW/2)
						< (ownGo.hitW + go.hitW) / 2
						&& Math.abs(sprite.getPlanePosY(ownGo.plnNo)
						+ ownGo.hitY + ownGo.hitH / 2
						-sprite.getPlanePosY(go.plnNo)-go.hitY-go.hitH/2)
						< (ownGo.hitH + go.hitH) / 2) {

						ownGo.hitPoint--;
						if(ownGo.hitPoint == 0)
							delGO(ownGo.plnNo);

						go.hitPoint--;
						go.hitFlag = true;
						if(go.hitPoint == 0) {
							score += go.scorePoint;
							go.attribute = GameObject.NO_HIT;
						}
						break;
					}
				}
			}
		}
	}


/** ¿À¨d°u¶€æ˜°vªP°uºƒæ˜°˛ºƒæ˜µoÆg™∫§lºu°vΩƒ¨ */
	void hitCheckOwn() {
		GameObject go;
		GameObject ownGo = null;
		Enumeration enum = elements();
		while(enum.hasMoreElements()) {
			go = ((GameObject)(enum.nextElement()));
			if(go.attribute == GameObject.OWN) {
				ownGo = go;
				break;
			}
		}
		if(ownGo == null)
			return;
		enum = elements();
		while(enum.hasMoreElements()) {
			go = ((GameObject)(enum.nextElement()));
			if(go.attribute != GameObject.ENEMY
				&& go.attribute != GameObject.ENEMY_BOW
				&& go.attribute != GameObject.ALL_HIT)
				continue;
			if(Math.abs(sprite.getPlanePosX(ownGo.plnNo)
				+ ownGo.hitX + ownGo.hitW / 2
				- sprite.getPlanePosX(go.plnNo) - go.hitX - go.hitW / 2)
				< (ownGo.hitW + go.hitW) / 2
				&& Math.abs(sprite.getPlanePosY(ownGo.plnNo)
				+ ownGo.hitY + ownGo.hitH / 2
				- sprite.getPlanePosY(go.plnNo) - go.hitY - go.hitH / 2)
				< (ownGo.hitH + go.hitH) / 2) {

				go.hitPoint--;
				go.hitFlag = true;
				if(go.hitPoint == 0) {
					score += go.scorePoint;
					go.attribute = GameObject.NO_HIT;
					if(go.attribute == GameObject.ENEMY_BOW)
						delGO(go.plnNo);
				}

				ownGo.attribute = GameObject.NO_HIT;
				break;
			}
		}
	}
}



/** •N™Ì™´≈È™∫GameObject√˛ßO */
abstract class GameObject {
	Sprite sprite;
	Queue keyQ;
	GameObjects gos;
/** ™´≈È∫ÿ√˛§@ƒ˝ */
	static final int OWN = 0, OWN_BOW = 1, ENEMY = 2, ENEMY_BOW = 3,
		NO_HIT = 4, ALL_HIT = 8;
/** ≈„•‹™´≈È®œ•Œ™∫•≠≠±Ωs∏π */
	int plnNo;
/** ™´≈È™∫∫ÿ√˛ */
	int attribute;
/** ™´≈È™∫≠@§[§O */
	int hitPoint = 1;
/** ™´≈È≥QØ}√aÆ…•i±o®Ï™∫§¿º∆ */
	int scorePoint = 0;
/** ™´≈È¨Oß_µo•Õ§FΩƒ¨ */
	boolean hitFlag = false;
/** ß@¨∞™´≈È¶Ê∂i•ÿº–™∫®‰•¶™´≈È©“¶b™∫•≠≠±Ωs∏π */
	int targetPlnNo;
/** ™´≈È™∫Ωƒ¨ßP©w∞œ∞Ï(ØxßŒ) */
	int hitX, hitY, hitW, hitH;


/** ´ÿ∫c§l */
	public GameObject(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		this.plnNo = plnNo;
		this.sprite = sprite;
		this.keyQ = keyQ;
		this.gos = gos;
		this.targetPlnNo = targetPlnNo;
	}


/** πÍß@™´≈È™∫¶Ê∞  */
	abstract public void move();


/** ™´≈È¶€µM¶Vµe≠±§U§Ë≤æ∞ ™∫¶Ê∞  */
	void scrollMove() {
		sprite.setPlaneMov(plnNo, 0, 3);
	}
}



/** ≈„•‹™Ì≤{¶€ºu™∫™´≈È™∫OwnBow√˛ßO */
class OwnBow extends GameObject {

	public OwnBow(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		attribute = OWN_BOW;
		sprite.setPlaneDraw(plnNo, new DrawRect(2,16,Color.green));
		x += 15;
		sprite.setPlanePos(plnNo, x, y);
		hitX = hitY = 0;
		hitW = 2;
		hitH = 16;
	}

	public void move() {
		sprite.setPlaneMov(plnNo, 0, -8);
		if(sprite.getPlanePosY(plnNo) < -16)
			gos.delGO(plnNo);
	}
}



/** ≈„•‹™Ì≤{ºƒºu™∫™´≈È™∫EnemyBow√˛ßO */
class EnemyBow extends OwnBow {

	public EnemyBow(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		attribute = ENEMY_BOW;
		sprite.setPlaneDraw(plnNo, new DrawRect(2,16,Color.yellow));
	}

	public void move() {
		sprite.setPlaneMov(plnNo, 0, 12);
		if(sprite.getPlanePosY(plnNo) > 400)
			gos.delGO(plnNo);
	}
}



/** •Œ®”™Ì≤{©ÒÆg™¨≠∏¥≤§ßºƒºu™∫EnemyRingBow√˛ßO */
class EnemyRingBow extends EnemyBow {
	double vx, vy, fx, fy;

	public EnemyRingBow(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		sprite.setPlaneDraw(plnNo, new DrawRect(6,6,Color.yellow));
		hitX = hitY = 0;
		hitW = hitH = 6;
		fx = x;
		fy = y;
// targetPlnNo®“•~ß@¨∞´¸©w®§´◊®œ•Œ
		vx = Math.cos(0.0174533 * targetPlnNo) * 5.;
		vy = Math.sin(0.0174533 * targetPlnNo) * 5.;
	}

	public void move() {
		fx += vx;
		fy += vy;
		sprite.setPlanePos(plnNo, (int)fx, (int)fy);
		if(sprite.getPlanePosY(plnNo) > 400
			|| sprite.getPlanePosY(plnNo) < -6
			|| sprite.getPlanePosX(plnNo) < -6
			|| sprite.getPlanePosX(plnNo) > 300)
			gos.delGO(plnNo);
	}
}



/** Ø‡∞˜∞l≥v¶€æ˜´e∂i™∫§lºuEnemyMissile√˛ßO */
class EnemyMissile extends EnemyBow {
	int vx = 0, vy = 0;

	public EnemyMissile(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		sprite.setPlaneDraw(plnNo, new DrawRect(6,6,Color.yellow));
		hitX = hitY = 0;
		hitW = hitH = 6;
	}

	public void move() {
// ®Ã∑”¶€§vªP•ÿº–™∫¶Ï∏m√ˆ´Y¶”≠◊•ø≥t´◊
		if(sprite.getPlanePosY(targetPlnNo) > sprite.getPlanePosY(plnNo))
			vy++;
		else
			vy--;
		if(sprite.getPlanePosX(targetPlnNo) > sprite.getPlanePosX(plnNo))
			vx++;
		else
			vx--;
// ±q≤{¶b™∫¶Ï∏m≤æ∞ ≥t´◊™∫≥Ê¶Ï∂q
		sprite.setPlaneMov(plnNo, vx, vy);

		if(sprite.getPlanePosY(plnNo) > 400
			|| sprite.getPlanePosX(plnNo) < 0
			|| sprite.getPlanePosX(plnNo) > 294)
			gos.delGO(plnNo);
	}
}



/** Enemy1√˛ßO */
class Enemy1 extends GameObject {
	int count = 0;

	public Enemy1(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		attribute = ENEMY;
		sprite.setPlaneGrp(plnNo, 0, 1);
		sprite.setPlanePos(plnNo, x, y);
		hitX = hitY = 8;
		hitW = hitH = 16;
		scorePoint = 10;
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed();
		else {
			sprite.setPlaneMov(plnNo, 0, 8);
			if(sprite.getPlanePosY(plnNo) > 400)
				gos.delGO(plnNo);
		}
	}

	public void beDestroyed() {
		scrollMove();
		if(count == 0)
			gos.addGO("Explosion",sprite.getPlanePosX(plnNo),
				sprite.getPlanePosY(plnNo), plnNo);
		else if(count > 20 || sprite.getPlanePosY(plnNo) > 400)
			gos.delGO(plnNo);
		count++;
		if(count%2==0)
			sprite.setPlaneView(plnNo, false);
		else
			sprite.setPlaneView(plnNo, true);
	}
}



/** Enemy2√˛ßO */
class Enemy2 extends Enemy1 {
	int direction;

	public Enemy2(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		sprite.setPlaneGrp(plnNo, 0, 2);
		scorePoint = 20;

		if(x > 134)
			direction = -2;
		else
			direction = 2;
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed();
		else {
			sprite.setPlaneMov(plnNo, direction, 8);
			if((int)(Math.random() * 10) == 0)
				gos.addGO("EnemyBow", sprite.getPlanePosX(plnNo),
					sprite.getPlanePosY(plnNo) + 16, targetPlnNo);
			if(sprite.getPlanePosY(plnNo) > 400)
				gos.delGO(plnNo);
		}
	}
}



/** Enemy3√˛ßO */
class Enemy3 extends Enemy2 {
	public Enemy3(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		sprite.setPlaneGrp(plnNo, 0, 3);
		scorePoint = 30;

		if(x > 134)
			direction = -1;
		else
			direction = 1;
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed();
		else {
			if(sprite.getPlanePosX(plnNo)<sprite.getPlanePosX(targetPlnNo))
				direction++;
			else
				direction--;
			sprite.setPlaneMov(plnNo, direction, 8);
			if((int)(Math.random() * 10) == 0)
				gos.addGO("EnemyBow", sprite.getPlanePosX(plnNo),
					sprite.getPlanePosY(plnNo) + 16, targetPlnNo);
			if(sprite.getPlanePosY(plnNo) > 400)
				gos.delGO(plnNo);
		}
	}
}



/** Enemy4√˛ßO */
class Enemy4 extends Enemy1 {
	public Enemy4(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
	}

	public void move() {
		int direction = -2;
		if(attribute != ENEMY)
			beDestroyed();
		else {
			if(sprite.getPlanePosX(targetPlnNo)
				- sprite.getPlanePosX(plnNo) > 0)
				direction = 2;
			sprite.setPlaneMov(plnNo, direction, 8);
			if(sprite.getPlanePosY(plnNo) > 400)
				gos.delGO(plnNo);
		}
	}
}



/** Enemy5√˛ßO */
class Enemy5 extends Enemy2 {
	double a, p, q, ix, iy, direction;

	public Enemy5(int plnNo, Sprite sprite, Queue keyQ, int x1, int y1,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x1, y1, gos, targetPlnNo);
// ®˙±o•ÿº–™∫Æyº–
		q = sprite.getPlanePosY(targetPlnNo);
		p = sprite.getPlanePosX(targetPlnNo);
// ãOê’Ç(p,q)Çí∏ì_Ç∆Çµ(x1,y1)Çí ÇÈ y = a(x - p)^2 + q Ç∆Ç∑ÇÈ
// x1 == p, y1 == q ÇæÇ∆ï˙ï®ê¸Ç…Ç»ÇÁÇ»Ç≠Çƒç¢ÇÈÇÃÇ≈èCê≥
		if(y1 == q)
			q++;
		if(x1 == p)
			p++;
// ®Da
		a = (y1 - q) / Math.pow(x1 - p, 2);
// xÇïœâªÇ≥ÇπÇÈó Ç?ÇﬂÇÈ
// pÇ∆x1ÇÃç∑Ç™ç≈è¨íl1ÇÃ?ÅA0.1Ç∏Ç¬ïœâªÇµÅA
// pÇ∆x1ÇÃç∑Ç™ç≈ëÂíl300ÇÃ?ÅA15Ç∏Ç¬ïœâªÇ∑ÇÈÇÊÇ§Ç…Ç∑ÇÈ
		direction = Math.abs(p - x1) * 149. / 2990. + 15./299.;
		if(x1 - p > 0)
			direction = -direction;
// ?ä˙à íuÇê›íË
		ix = x1;
		iy = y1;
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed();
		else {
// xÇ?ÇﬂÇÁÇÍÇΩó Ç∏Ç¬ïœâªÇ≥ÇπÇƒÇ¢Ç≠
			ix += direction;
// êVÇµÇ¢xç¿ïWÇå≥Ç…ÅAyÇÃïœâªó Ç?ÇﬂÅAyç¿ïWÇ…â¡Ç¶ÇƒÇ¢Ç≠
// iy = a * Math.pow(ix - p) + q; Ç∆ìØÇ∂ó Ç™?Ç‹ÇÈ
			iy += 2. * a * (ix - p) * direction;

			sprite.setPlanePos(plnNo, (int)ix, (int)iy);

			if(sprite.getPlanePosY(plnNo) > 400
				|| sprite.getPlanePosY(plnNo) < -32
				|| sprite.getPlanePosX(plnNo) < -32
				|| sprite.getPlanePosX(plnNo) > 300)
				gos.delGO(plnNo);
		}
	}
}



/** 1ñ ÇÃ?É{ÉXÇï\åªÇ∑ÇÈï®ëÃÇï\Ç∑MidleEnemy1√˛ßO */
class MidleEnemy1 extends Enemy1 {
	int direction = 0;

	public MidleEnemy1(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		sprite.setPlaneGrp(plnNo, 0, 4);
		hitX = hitY = 10;
		hitW = hitH = 44;
		hitPoint = 16;
		scorePoint = 100;
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed(2, 2);
		else {
			advent(64, 50);

			if((int)(Math.random() * 10) == 0) {
				gos.addGO("EnemyBow", sprite.getPlanePosX(plnNo),
					sprite.getPlanePosY(plnNo) + 32, targetPlnNo);
				gos.addGO("EnemyBow", sprite.getPlanePosX(plnNo) + 32,
					sprite.getPlanePosY(plnNo) + 32, targetPlnNo);
			}
		}
	}

	public void advent(int width, int downCount) {
		sprite.setPlaneView(plnNo, !hitFlag);
		if(hitFlag)
			hitFlag = false;
		else if(sprite.getPlanePosY(plnNo) < downCount)
			sprite.setPlaneMov(plnNo, 0, 4);
		else if(direction == 0)
			direction = -4;
		else if(sprite.getPlanePosX(plnNo) <= 0)
			direction = 4;
		else if(sprite.getPlanePosX(plnNo) >= 300 - width)
			direction = -4;
		sprite.setPlaneMov(plnNo, direction, 0);
	}

	public void beDestroyed(int width, int height) {
		scrollMove();
		if(count < width * height) {
			gos.addGO("Explosion",sprite.getPlanePosX(plnNo)+count%width*32,
				sprite.getPlanePosY(plnNo) + count/width*32, plnNo);
		} else if(count > 20 + width * height
			|| sprite.getPlanePosY(plnNo) > 400) {
			gos.delGO(plnNo);
		}
		count++;
		if(count%2==0)
			sprite.setPlaneView(plnNo, false);
		else
			sprite.setPlaneView(plnNo, true);
	}
}



/** 2ñ ÇÃ?É{ÉXÇï\åªÇ∑ÇÈï®ëÃÇï\Ç∑MidleEnemy2√˛ßO */
class MidleEnemy2 extends MidleEnemy1 {
	public MidleEnemy2(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed(2, 2);
		else {
			advent(64, 50);

			if((int)(Math.random() * 10) == 0) {
				gos.addGO("EnemyBow", sprite.getPlanePosX(plnNo),
					sprite.getPlanePosY(plnNo) + 32, targetPlnNo);
				gos.addGO("EnemyBow", sprite.getPlanePosX(plnNo) + 32,
					sprite.getPlanePosY(plnNo) + 32, targetPlnNo);
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 32,
					sprite.getPlanePosY(plnNo) + 32, 45);
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 32,
					sprite.getPlanePosY(plnNo) + 32, 90);
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 32,
					sprite.getPlanePosY(plnNo) + 32, 135);
			}
		}
	}
}



/** 3ñ ÇÃ?É{ÉXÇï\åªÇ∑ÇÈï®ëÃÇï\Ç∑MidleEnemy3√˛ßO */
class MidleEnemy3 extends MidleEnemy1 {
	public MidleEnemy3(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed(2, 2);
		else {
			advent(64, 50);

			if((int)(Math.random() * 15) == 0) {
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 32,
					sprite.getPlanePosY(plnNo) + 32, 45);
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 32,
					sprite.getPlanePosY(plnNo) + 32, 90);
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 32,
					sprite.getPlanePosY(plnNo) + 32, 135);
				gos.addGO("EnemyMissile", sprite.getPlanePosX(plnNo) + 13,
					sprite.getPlanePosY(plnNo) + 32, targetPlnNo);
				gos.addGO("EnemyMissile", sprite.getPlanePosX(plnNo) + 45,
					sprite.getPlanePosY(plnNo) + 32, targetPlnNo);
			}
		}
	}
}



/** 1ñ ÇÃÉ{ÉXÇï\åªÇ∑ÇÈï®ëÃÇï\Ç∑BigEnemy1√˛ßO */
class BigEnemy1 extends MidleEnemy1 {
	public BigEnemy1(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		sprite.setPlaneGrp(plnNo, 0, 5);
		hitX = hitY = 20;
		hitW = hitH = 88;
		hitPoint = 32;
		scorePoint = 500;
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed(4, 4);
		else {
			advent(128, 50);

			if((int)(Math.random() * 10) == 0) {
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 64,
					sprite.getPlanePosY(plnNo) + 64, 0);
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 64,
					sprite.getPlanePosY(plnNo) + 64, 45);
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 64,
					sprite.getPlanePosY(plnNo) + 64, 90);
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 64,
					sprite.getPlanePosY(plnNo) + 64, 135);
				gos.addGO("EnemyRingBow", sprite.getPlanePosX(plnNo) + 64,
					sprite.getPlanePosY(plnNo) + 64, 180);
			}
		}
	}
}



/** ÉuÉ^ÇÃÉ{ÉXÇï\åªÇ∑ÇÈï®ëÃÇï\Ç∑BigEnemy2√˛ßO */
class BigEnemy2 extends MidleEnemy1 {
	public BigEnemy2(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		sprite.setPlaneGrp(plnNo, 0, 11);
		hitX = hitY = 8;
		hitW = 112;
		hitH = 70;
		hitPoint = 48;
		scorePoint = 1000;
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed(4, 3);
		else {
			advent(128, 50);

			if((int)(Math.random() * 10) == 0) {
				gos.addGO("EnemyMissile", sprite.getPlanePosX(plnNo) + 55,
					sprite.getPlanePosY(plnNo) + 56, targetPlnNo);
				gos.addGO("EnemyMissile", sprite.getPlanePosX(plnNo) + 65,
					sprite.getPlanePosY(plnNo) + 56, targetPlnNo);
			}
		}
	}
}



/** ÉJÉG?ÇÃÉ{ÉXÇï\åªÇ∑ÇÈï®ëÃÇï\Ç∑BigEnemy3√˛ßO */
class BigEnemy3 extends BigEnemy2 {
	int tongues[] = null;
	int tongueCount = 0;
	int tongueTargetX;
	int tongueTargetY;
	static final int tonguesLength = 10;

	public BigEnemy3(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		sprite.setPlaneGrp(plnNo, 0, 16);
		hitPoint = 64;
		scorePoint = 2000;
	}

	public void move() {
		if(attribute != ENEMY) {
			if(count == 0)
				for(int j = 0; j < tonguesLength; j++)
					gos.delGO(tongues[j]);
			beDestroyed(4, 3);
		} else {
			advent(128, 50);

			if(tongues == null) {
				tongues = new int[tonguesLength];
				for(int j = 0; j < tonguesLength; j++)
					tongues[j] = gos.addGO("EnemyTongue",
						sprite.getPlanePosX(plnNo) + 48,
						j * 12 + sprite.getPlanePosY(plnNo) + 80, plnNo);
			}
			if(tongueCount / 40 % 2 == 0) {
				for(int i = 0; i < tonguesLength; i++)
					sprite.setPlanePos(tongues[i],
						(int)(Math.sin(i * 12. + tongueCount) * 27.)
						+ sprite.getPlanePosX(plnNo) + 48,
						i * 12 + sprite.getPlanePosY(plnNo) + 80);
			} else {
				int j = tongueCount % 40;
				if(j == 10) {
					tongueTargetX = sprite.getPlanePosX(targetPlnNo) + 16;
					tongueTargetY = sprite.getPlanePosY(targetPlnNo) + 16;
				}
				if(j / 10 < 1)
					for(int i = 0; i < tonguesLength; i++)
						sprite.setPlanePos(tongues[i],
							(int)(Math.sin(i * 12. + tongueCount)
							* 27. * (10. - j) / 10. )
							+ sprite.getPlanePosX(plnNo) + 48,
							(int)((i * 12. / 10. * (10. - j))
							+ sprite.getPlanePosY(plnNo) + 80));
				else if(j / 10 < 2)
					for(int i = 0; i < tonguesLength; i++)
						sprite.setPlanePos(tongues[i],
							(int)((tongueTargetX
							- sprite.getPlanePosX(plnNo) - 48)
							/ (tonguesLength - 1) * i / 9. * (j % 10)
							+ sprite.getPlanePosX(plnNo) + 48),
							(int)((tongueTargetY
							- sprite.getPlanePosY(plnNo) - 80)
							/ (tonguesLength - 1) * i / 9 * (j % 10)
							+ sprite.getPlanePosY(plnNo) + 80));
				else if(j / 10 < 3)
					for(int i = 0; i < tonguesLength; i++)
						sprite.setPlanePos(tongues[i],
							(int)((tongueTargetX
							- sprite.getPlanePosX(plnNo) - 48)
							/ (tonguesLength - 1) * i / 9. * (10 - j % 10)
							+ sprite.getPlanePosX(plnNo) + 48),
							(int)((tongueTargetY
							- sprite.getPlanePosY(plnNo) - 80)
							/ (tonguesLength - 1) * i / 9 * (10 - j % 10)
							+ sprite.getPlanePosY(plnNo) + 80));
				else
					for(int i = 0; i < tonguesLength; i++)
						sprite.setPlanePos(tongues[i],
							(int)(Math.sin(i * 12. + tongueCount)
							* 27. * (j % 10) / 10.)
							+ sprite.getPlanePosX(plnNo) + 48,
							(int)((i * 12. / 10. * (j % 10))
							+ sprite.getPlanePosY(plnNo) + 80));
			}
			tongueCount++;
		}
	}
}



/** BigEnemy3ÇÃê„Çï\åªÇ∑ÇÈï®ëÃÇï\Ç∑EnemyTongue√˛ßO */
class EnemyTongue extends Enemy1 {
	public EnemyTongue(int plnNo, Sprite sprite, Queue keyQ,
		int x, int y, GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		sprite.setPlaneGrp(plnNo, 0, 17);
		hitX = hitY = 4;
		hitW = hitH = 8;
		hitPoint = 10000;
	}

	public void move() {}
}



/** é©ã@Çï\åªÇ∑ÇÈï®ëÃÇï\Ç∑Own√˛ßO */
class Own extends GameObject {
	int count = 0, explosion = -1;
/** écã@? */
	int last = 3;

	public Own(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		attribute = NO_HIT;
		sprite.setPlaneGrp(plnNo, 0, 6);
		sprite.setPlanePos(plnNo, x, y);
		hitX = hitY = 10;
		hitW = hitH = 12;
	}

	public void move() {
		if(attribute != OWN) {
			if(explosion == -1) {
				moveInputs();
				sprite.setPlaneMov(plnNo, 0, -8);
				if(sprite.getPlanePosY(plnNo) < 300) {
					attribute = OWN;
					explosion = 0;
					sprite.setPlaneView(plnNo, true);
					count = 0;
					return;
				}
			} else {
				keyQ.removeAllElements();
				scrollMove();
				if(count == 0)
					gos.addGOsetOverOwn("Explosion");
				else if(count > 20 || sprite.getPlanePosY(plnNo) > 400) {
					explosion = -1;
					sprite.setPlanePos(plnNo, sprite.getPlanePosX(plnNo),
						400);
					last--;
					if(last == 0)
						gos.delGO(plnNo);
					return;
				}
			}
			count++;
			if(count%2==0)
				sprite.setPlaneView(plnNo, false);
			else
				sprite.setPlaneView(plnNo, true);
			return;
		}
		moveInputs();
	}

// é©ã@ÇÃ?çÏÇÕÇ±Ç±Ç≈??
	void moveInputs() {
		InputEventTiny ket;
		boolean u, d, l, r, s;
		u = d = l = r = s = true;

		while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
			if(ket.getID() != KeyEvent.KEY_PRESSED)
				continue;
			switch(ket.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					if(d && sprite.getPlanePosY(plnNo) < 400 - 32 - 8) {
						sprite.setPlaneMov(plnNo, 0, 8);
						d = false;
					}
					break;
				case KeyEvent.VK_UP:
					if(u && sprite.getPlanePosY(plnNo) > 8) {
						sprite.setPlaneMov(plnNo, 0, -8);
						u = false;
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(r && sprite.getPlanePosX(plnNo) < 300 - 32 - 8) {
						sprite.setPlaneMov(plnNo, 8, 0);
						r = false;
					}
					break;
				case KeyEvent.VK_LEFT:
					if(l && sprite.getPlanePosX(plnNo) > 8) {
						sprite.setPlaneMov(plnNo, -8, 0);
						l = false;
					}
					break;
				case KeyEvent.VK_SPACE:
					if(s) {
						gos.addGO("OwnBow", sprite.getPlanePosX(plnNo),
							sprite.getPlanePosY(plnNo) - 16, 0);
						s = false;
					}
					break;
			}
		}
	}

	int getLast() {
		return last;
	}
}



/** ?î≠Çï\åªÇ∑ÇÈï®ëÃÇï\Ç∑Explosion√˛ßO */
class Explosion extends GameObject {
	int count = 0;

	public Explosion(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x, y, gos, targetPlnNo);
		attribute = NO_HIT;
		sprite.setPlaneDraw(plnNo, new DrawExplosion());
		sprite.setPlanePos(plnNo, x, y);
		count = 0;
	}

	public void move() {
		scrollMove();
		if(count > 20 || sprite.getPlanePosY(plnNo) > 400)
			gos.delGO(plnNo);
		count++;
	}
}



/** ?î≠Çï`âÊÇ∑ÇÈDrawExplosion√˛ßO */
class DrawExplosion implements Draw {
	int count, x, y;

	DrawExplosion() {
		count = 0;
		x = y = 5;
	}

	public boolean drawing(Graphics g, Plane pln) {
		switch(count) {
			case 4:
				x = y = 27;
				break;
			case 8:
				x = 5;
				y = 27;
				break;
			case 12:
				x = 27;
				y = 5;
				break;
			case 16:
				x = y = 16;
				break;
		}
		g.setColor(Color.yellow);
		g.fillOval(pln.posX + x - count%4*4, pln.posY + y - count%4*4,
			count%4*8, count%4*8);
		count++;
		return true;
	}
}
