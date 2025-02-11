// GameObjects.java
// written by mnagaku

import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.lang.reflect.*;



/**
 * GameObjects類別<br>
 * 遊戲登場的各種的物體的處理
 * @author mnagaku
 */
class GameObjects extends Hashtable {

	Sprite sprite;
	Queue keyQ;

	int count, overCount;
	int ownPlnNo, firstPlnNo;
/** 分數 */
	int score = 0;


/** 建構子 */
	GameObjects(int no, Sprite sprite, Queue keyQ) {
		firstPlnNo = count = no;
		ownPlnNo = firstPlnNo + 100;
		overCount = ownPlnNo + 1;
		this.sprite = sprite;
		this.keyQ = keyQ;
	}


/** 分數清為0 */
	boolean clearScore() {
		score = 0;
		return true;
	}


/** 傳回分數 */
	int getScore() {
		return score;
	}


/** 自機是不活著 */
	boolean isAliveOwn() {
		if(get(new Integer(ownPlnNo)) == null)
			return false;
		else
			return true;
	}


/** 傳回殘餘機數量 */
	int getLast() {
		return ((Own)get(new Integer(ownPlnNo))).getLast();
	}


/** 現在管理中物體清除掉 */
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


/** 登錄自機前方的角色 */
	int addGOsetOverOwn(String className) {
		addGOsetPln(className, sprite.getPlanePosX(ownPlnNo),
			sprite.getPlanePosY(ownPlnNo), ownPlnNo, overCount);
		overCount++;
		return overCount - 1;
	}


/** 登錄自機 */
	int addGOOwn(String className, int x, int y, int plnNo) {
		ownPlnNo = plnNo;
		overCount = ownPlnNo + 1;
		addGOsetPln(className, x, y, ownPlnNo, ownPlnNo);
		return plnNo;
	}


/** 登錄自機後方的角色 */
	int addGO(String className, int x, int y, int targetPlnNo) {
		int ret = count;
		addGOsetPln(className, x, y, targetPlnNo, count);
		count++;
		if(count >= ownPlnNo)
			count = firstPlnNo;
		return ret;
	}


/** 指定平面編號中物體的登錄 */
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


/** 指定平面編號中物體的刪除 */
	void delGO(int plnNo) {
		sprite.delPlane(plnNo);
		remove(new Integer(plnNo));
	}


/** 移動被登錄的全部的物體 */
	void moveAll() {
		Enumeration enum = elements();
		while(enum.hasMoreElements())
			((GameObject)(enum.nextElement())).move();
	}


/** 判定「敵機」與「自機／自機發射的子彈」衝突 */
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


/** 檢查「自機」與「敵機／敵機發射的子彈」衝突 */
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



/** 代表物體的GameObject類別 */
abstract class GameObject {
	Sprite sprite;
	Queue keyQ;
	GameObjects gos;
/** 物體種類一覽 */
	static final int OWN = 0, OWN_BOW = 1, ENEMY = 2, ENEMY_BOW = 3,
		NO_HIT = 4, ALL_HIT = 8;
/** 顯示物體使用的平面編號 */
	int plnNo;
/** 物體的種類 */
	int attribute;
/** 物體的耐久力 */
	int hitPoint = 1;
/** 物體被破壞時可得到的分數 */
	int scorePoint = 0;
/** 物體是否發生了衝突 */
	boolean hitFlag = false;
/** 作為物體行進目標的其它物體所在的平面編號 */
	int targetPlnNo;
/** 物體的衝突判定區域(矩形) */
	int hitX, hitY, hitW, hitH;


/** 建構子 */
	public GameObject(int plnNo, Sprite sprite, Queue keyQ, int x, int y,
		GameObjects gos, int targetPlnNo) {
		this.plnNo = plnNo;
		this.sprite = sprite;
		this.keyQ = keyQ;
		this.gos = gos;
		this.targetPlnNo = targetPlnNo;
	}


/** 實作物體的行動 */
	abstract public void move();


/** 物體自然向畫面下方移動的行動 */
	void scrollMove() {
		sprite.setPlaneMov(plnNo, 0, 3);
	}
}



/** 顯示表現自彈的物體的OwnBow類別 */
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



/** 顯示表現敵彈的物體的EnemyBow類別 */
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



/** 用來表現放射狀飛散之敵彈的EnemyRingBow類別 */
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
// targetPlnNo例外作為指定角度使用
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



/** 能夠追逐自機前進的子彈EnemyMissile類別 */
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
// 依照自己與目標的位置關係而修正速度
		if(sprite.getPlanePosY(targetPlnNo) > sprite.getPlanePosY(plnNo))
			vy++;
		else
			vy--;
		if(sprite.getPlanePosX(targetPlnNo) > sprite.getPlanePosX(plnNo))
			vx++;
		else
			vx--;
// 從現在的位置移動速度的單位量
		sprite.setPlaneMov(plnNo, vx, vy);

		if(sprite.getPlanePosY(plnNo) > 400
			|| sprite.getPlanePosX(plnNo) < 0
			|| sprite.getPlanePosX(plnNo) > 294)
			gos.delGO(plnNo);
	}
}



/** Enemy1類別 */
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



/** Enemy2類別 */
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



/** Enemy3類別 */
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



/** Enemy4類別 */
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



/** Enemy5類別 */
class Enemy5 extends Enemy2 {
	double a, p, q, ix, iy, direction;

	public Enemy5(int plnNo, Sprite sprite, Queue keyQ, int x1, int y1,
		GameObjects gos, int targetPlnNo) {
		super(plnNo, sprite, keyQ, x1, y1, gos, targetPlnNo);
// 取得目標的座標
		q = sprite.getPlanePosY(targetPlnNo);
		p = sprite.getPlanePosX(targetPlnNo);
// �O����(p,q)�����_����(x1,y1)������ y = a(x - p)^2 + q ������
// x1 == p, y1 == q �������������������������������C��
		if(y1 == q)
			q++;
		if(x1 == p)
			p++;
// 求a
		a = (y1 - q) / Math.pow(x1 - p, 2);
// x����������������?����
// p�鸚1�����������l1��?�A0.1�����������A
// p�鸚1�����������l300��?�A15����������������������
		direction = Math.abs(p - x1) * 149. / 2990. + 15./299.;
		if(x1 - p > 0)
			direction = -direction;
// ?�����u������
		ix = x1;
		iy = y1;
	}

	public void move() {
		if(attribute != ENEMY)
			beDestroyed();
		else {
// x��?����������������������������
			ix += direction;
// �V���│���W�������Ay����������?���Ay���W������������
// iy = a * Math.pow(ix - p) + q; ����������?����
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



/** 1����?�{�X���\�������������\�愚idleEnemy1類別 */
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



/** 2����?�{�X���\�������������\�愚idleEnemy2類別 */
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



/** 3����?�{�X���\�������������\�愚idleEnemy3類別 */
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



/** 1�����{�X���\�������������\�媲igEnemy1類別 */
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



/** �u�^���{�X���\�������������\�媲igEnemy2類別 */
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



/** �J�G?���{�X���\�������������\�媲igEnemy3類別 */
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



/** BigEnemy3�������\�������������\�幌nemyTongue類別 */
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



/** ���@���\�������������\�慈wn類別 */
class Own extends GameObject {
	int count = 0, explosion = -1;
/** �c�@? */
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

// ���@��?����������??
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



/** ?�����\�������������\�幌xplosion類別 */
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



/** ?�����`�����噲rawExplosion類別 */
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
