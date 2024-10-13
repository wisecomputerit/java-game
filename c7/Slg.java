// Slg.java
// written by mnagaku

import java.util.*;
import java.awt.event.*;
import java.awt.*;


/**
 * Slg
 */
public class Slg extends Game2D {

	public Slg() {
		CANVAS_SIZE_W = 300;
		CANVAS_SIZE_H = 400;
		SPEED = 100;
		KEY_SPEED = 40;
		KEY_DELAY = 3;
	}

/**
 * Game建構子的實體
*/
	public static void main(String args[]) {
		startGame("Slg");
	}


// 標記圖形的座標
	final int CAPITAL_POS[][] = {
		{38, 34},
		{145, 51},
		{261, 90},
		{173, 156},
		{48, 148},
		{38, 249},
		{32, 328},
		{120, 280},
		{246, 246},
		{220, 329},
	};

// 領主的數量
	static final int C_MAX = 10;

// 領主的鄰接狀態
	final int CAPITAL_LINE[][] = {
		{1, 0, 0, 0, 0, 0, 0, 0, 0},
		{1, 1, 1, 0, 0, 0, 0, 0},
		{1, 0, 0, 0, 0, 1, 0},
		{1, 1, 0, 1, 1, 0},
		{1, 0, 0, 0, 0},
		{1, 1, 0, 0},
		{1, 0, 0},
		{1, 1},
		{1},
	};

// 查詢領土是否相鄰
	boolean isCapitalConnect(int no1, int no2) {
		int line = no1 < no2 ? no1 : no2;
		int no = no1 > no2 ? no1 - line - 1 : no2 - line - 1;
		if(no1 == no2 || line < 0 || line >= CAPITAL_LINE.length
			|| no < 0 || no >= CAPITAL_LINE[line].length
			|| CAPITAL_LINE[line][no] == 0)
			return false;
		return true;
	}

	Capital pref[];
	Relation relation;
	King kings[];
	SlgMain slgMain;

// 表示遠征軍Sprite的開始號碼
	int idSeed = 50;

// 領土/遠征軍的參數
	boolean viewParam = false;

// 標記圖形的width是以反比例的雙曲線表示
	boolean relativeSize = false;

// 標記圖形的width與雙曲線彎的情形
	double p = 500.;


/**
 * SlgMain類別<br>
 * 遊戲本體的處理
 * @author mnagaku
 */
	public class SlgMain extends Game2DMain {

/** 圖形的副檔名 */
		static final String GRP_EXTENSION = ".gif";


		int mode = -1, warSrc = -1;

/** 建構子 */
		public SlgMain() {

			int i, j;

			slgMain = this;

			pref = new Capital[C_MAX];

			relation = new Relation(C_MAX);

// 背景
			sprite.addGrp(0, "map"+GRP_EXTENSION);
			sprite.waitLoad();
			sprite.setPlaneGrp(0, 0, 0);
// selector
			sprite.setPlaneDraw(1, new Selector());
// 標記圖形
			for(i = 0; i < C_MAX; i++) {
				sprite.setPlaneDraw(10 + i, pref[i] = new Capital(i));
				sprite.setPlanePos(10+i, CAPITAL_POS[i][0], CAPITAL_POS[i][1]);
			}
// dummy
			relation.init();
			kings = new King[C_MAX];
			for(i = 0; i < C_MAX; i++)
				kings[i] = new King1(i, pref[i]);

// BGM
			sp.addBgm(1, "09.au");
			sp.playBgm(1);
		}

		public boolean mainLoop() {
			InputEventTiny ket;
			InputEventTiny met;

			switch(mode) {
//clear
				case -3:
					sprite.setPlaneString(100, "You are the King of kings");
					sprite.setPlanePos(100, 60, 200);
					sprite.setPlaneColor(100, 255, 255, 255);
					sprite.setPlaneString(101, "Hit any key");
					sprite.setPlanePos(101, 100, 250);
					sprite.setPlaneColor(101, 255, 255, 255);

					mouseQ.removeAllElements();

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = -1;
					break;
// game over
				case -2:
					sprite.setPlaneString(100, "You lose");
					sprite.setPlanePos(100, 110, 200);
					sprite.setPlaneColor(100, 255, 255, 255);
					sprite.setPlaneString(101, "Hit any key");
					sprite.setPlanePos(101, 100, 250);
					sprite.setPlaneColor(101, 255, 255, 255);

					mouseQ.removeAllElements();

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = -1;
					break;
// title
				case -1:
					sprite.setPlaneString(100, "Tiny Simulation");
					sprite.setPlanePos(100, 80, 200);
					sprite.setPlaneColor(100, 255, 255, 255);
					sprite.setPlaneString(101, "Hit any key");
					sprite.setPlanePos(101, 100, 250);
					sprite.setPlaneColor(101, 255, 255, 255);

					mouseQ.removeAllElements();

					while((ket = (InputEventTiny)(keyQ.dequeue())) != null)
						if(ket.getID() != KeyEvent.KEY_RELEASED)
							mode = 0;
					break;
// main start
				case 0:
					sprite.setPlaneView(100, false);
					sprite.setPlaneView(101, false);
					mode = 1;

					relation.init();

					int shift = (int)(Math.random() * C_MAX);

					kings = new King[C_MAX];
					kings[0] = new King0(0, pref[shift]);

					for(int i = 1; i < C_MAX; i++)
						kings[i] = new King1(i, pref[(i+shift) % C_MAX]);

					break;
// main
				case 1:

// 玩家的領土失掉就算輸
					if(!kings[0].isAlive) {
						for(int i = 0; i < C_MAX; i++)
							pref[i].military = 0;
						mode = -2;
						break;
					}
// 玩家的領土失掉就算贏
					if(kings[0].capitals.size() == C_MAX) {
						for(int i = 0; i < C_MAX; i++)
							pref[i].military = 0;
						mode = -3;
						break;
					}

// 以玩家最昌盛的領土做為基準決定標記圖形的大小
					if(relativeSize) {
						double ownTotalMax = 0.;
						for(int i = 0; i < kings[0].capitals.size(); i++)
							if(((Capital)(kings[0].capitals.elementAt(i))).total
								> ownTotalMax)
								ownTotalMax
									= ((Capital)(kings[0].capitals
									.elementAt(i))).total;
						if(ownTotalMax > 0.)
							p = ownTotalMax / 2.;
					}

// 按鍵輸入處理
					while((ket = (InputEventTiny)(keyQ.dequeue())) != null) {
						if(ket.getID() != KeyEvent.KEY_PRESSED)
							continue;
						switch(ket.getKeyCode()) {
							case KeyEvent.VK_RIGHT:
							case KeyEvent.VK_X:
								Selector.pos++;
								if(Selector.pos > 3)
									Selector.pos = 3;
								break;
							case KeyEvent.VK_LEFT:
							case KeyEvent.VK_Z:
								Selector.pos--;
								if(Selector.pos < 0)
									Selector.pos = 0;
								break;
							case KeyEvent.VK_ESCAPE:
								mode = -2;
								break;
						}
					}

// 游標操作的處理
					while((met = (InputEventTiny)(mouseQ.dequeue())) != null) {
						if(met.getID() == MouseEvent.MOUSE_PRESSED) {
							for(int i = 0; i < C_MAX; i++) {
								if(Math.abs(pref[i].x - met.getX()) < 30
									&& Math.abs(pref[i].y - met.getY()) < 30) {
									switch(Selector.pos) {
// [戰爭]拖曳到要開始侵佔的領土
										case 0:
											if(kings[0].isTerritory(pref[i])
												&& pref[i].military
												< pref[i].economy)
												warSrc = i;
											break;
// [軍備]
										case 1:
											if(kings[0].isTerritory(pref[i]))
												pref[i].incMilitary();
											break;
// [外交]
										case 2:
											if(!kings[0].isTerritory(pref[i]))
												incFriendry(kings[0],
													pref[i].owner);
											break;
// [投資]
										case 3:
											if(kings[0].isTerritory(pref[i]))
												pref[i].incEconomy();
											break;
										
									}
									break;
								}
							}
						}
// [戰爭]在要侵佔處的領土放開
						else if(met.getID() == MouseEvent.MOUSE_RELEASED
							&& warSrc >= 0) {
							for(int i = 0; i < C_MAX; i++) {
								if(!kings[0].isTerritory(pref[i])
									&& isCapitalConnect(warSrc, i)
									&& Math.abs(pref[i].x - met.getX()) < 30
									&& Math.abs(pref[i].y - met.getY()) < 30) {
// 軍力一半時組成遠征軍，會要花費二倍價值的費用
									new Expedition(pref[i],
										pref[warSrc].military / 2,
										kings[0], CAPITAL_POS[warSrc][0],
										CAPITAL_POS[warSrc][1]);
									pref[warSrc].military /= 2;
									pref[warSrc].economy-=pref[warSrc].military;
								}
							}
							warSrc = -1;
						}
					}

// 電腦扮演領主的行動
					for(int i = 1; i < C_MAX; i++)
						if(Math.random() < .5)
							kings[i].exec();
			}
			return true;
		}


		void incFriendry(King src, King dist) {
			if(!src.isAlive || !dist.isAlive)
				return;

			double add = 0.;
			for(int i = 0; i < src.capitals.size(); i++) {
				add += ((Capital)(src.capitals.elementAt(i))).economy * .05;
				((Capital)(src.capitals.elementAt(i))).economy *= .95;
			}

			for(int i = 0; i < dist.capitals.size(); i++)
				((Capital)(dist.capitals.elementAt(i))).economy
					+= add / (dist.capitals.size() * 2);

			relation.changeRelation(src.id, dist.id);
		}
	}



// 敵對的電腦領主
	class King1 extends King {

		King1(int id, Capital capital) {
			super(id, capital);
		}

		void think() {
// 軍備
			for(int i = 0; i < capitals.size(); i++) {
				Capital tc = (Capital)(capitals.elementAt(i));
				if(tc.military < tc.economy / 3) {
					tc.incMilitary();
					return;
				}
			}
// 經濟
			for(int i = 0; i < capitals.size(); i++) {
				Capital tc = (Capital)(capitals.elementAt(i));
				for(int j = 0; j < C_MAX; j++)
					if(!isTerritory(pref[j])
						&& isCapitalConnect(tc.id, j)
						&& tc.economy < pref[j].economy / 2) {
						tc.incEconomy();
						return;
					}
			}
// 戰爭
			for(int i = 0; i < capitals.size(); i++) {
				Capital tc = (Capital)(capitals.elementAt(i));
				for(int j = 0; j < C_MAX; j++)
					if(!isTerritory(pref[j])
						&& isCapitalConnect(tc.id, j)
						&& tc.military - relation.getRelation(id,
						pref[j].owner.id)
						> pref[j].military * 2) {
						new Expedition(pref[j], tc.military / 2,
							this, tc.x, tc.y);
						tc.military /= 2;
						tc.economy -= tc.military;
						return;
					}
			}
// 外交
			for(int i = 0; i < capitals.size(); i++) {
				Capital tc = (Capital)(capitals.elementAt(i));
				for(int j = 0; j < C_MAX; j++)
					if(!isTerritory(pref[j])) {
						if(isCapitalConnect(tc.id, j)
							&& relation.getRelation(tc.id, j) < 50
							&& tc.military < pref[j].military) {
							slgMain.incFriendry(this, pref[j].owner);
							return;
						}
					}
			}
		}
	}



// 玩家操縱的領主
	class King0 extends King {

		King0(int id, Capital capital) {
			super(id, capital);
		}

		void think() {}
	}



// 領主的雛型
	abstract class King {
		boolean isAlive = true;
		int id;
		Vector capitals = new Vector();
		Color flagColor;
		final Color flags[] = {Color.red, Color.green, Color.blue,
			Color.cyan, Color.magenta, Color.yellow, Color.orange, Color.pink,
			Color.black, Color.gray};

		King(int id, Capital capital) {
			this.id = id;
			capital.init(this);
			capitals.addElement(capital);
			flagColor = flags[id];
		}

		abstract void think();

		void exec() {
			if(isAlive)
				think();
		}

		void lostTerritory(Capital capital) {
			capitals.removeElement(capital);
			if(capitals.isEmpty())
				isAlive = false;
		}

		void addTerritory(Capital capital) {
			capitals.addElement(capital);
		}

		boolean isTerritory(Capital capital) {
			return capitals.contains(capital);
		}
	}



// 遠征軍
	class Expedition implements Draw {
		King owner;
		Capital target;
		double military;
		int x, y;
		int width;
		int vx, vy;
		int id;
		int moveCount = 0;
		Color color;

		Expedition(Capital target, double military, King owner,
			int sx, int sy) {
			this.target = target;
			this.military = military;
			this.owner = owner;
			x = sx;
			y = sy;
			idSeed++;
			id = idSeed;
			sprite.setPlaneDraw(id, this);
			vx = (target.x - x) / 10;
			vy = (target.y - y) / 10;
		}

		public boolean drawing(Graphics g, Plane pln) {

// 如果另外的部隊進行佔領，結束戰鬥合流
			if(target.owner == owner) {
				target.military += military;
				sprite.delPlane(id);
				return true;
			}

// 如果遠征軍的軍力失去時，則遠征軍消滅
			if(military < 1) {
				sprite.delPlane(id);
				return true;
			}

// 如果目的領土的軍力失掉時，則佔領領土
			if(target.military < 1) {
				target.owner.lostTerritory(target);
				owner.addTerritory(target);
				target.owner = owner;
				target.military += military;
				sprite.delPlane(id);
				return true;
			}

// 如果接敵時就戰爭
			if(Math.pow(Math.abs(target.x - x), 2)
				+ Math.pow(Math.abs(target.y - y), 2)
				< target.width + width) {
				double targetKill = Math.pow(target.military, 2)
					* Math.random() / 2000;
				double kill = Math.pow(military, 2) * Math.random() / 2000;
				military -= targetKill;
				target.military -= kill;
				target.economy -= targetKill + kill;
				relation.decRelation(owner.id, target.owner.id);
// 如果不是就進軍
			} else {
				if(moveCount > 10) {
					x = target.x;
					y = target.y;
				} else {
					x += vx;
					y += vy;
					moveCount++;
				}
			}

			color = relation.getColor(owner.id);

			width = (int)((60. * military * 2.) / (military * 2. + p));

			g.setColor(color);
			g.fillRect(x - width / 2, y - width / 2, width, width);

			g.setColor(owner.flagColor);
			g.drawRect(x - width / 2, y - width / 2, width, width);
// 顯示遠征軍的戰力
			if(viewParam) {
				g.setColor(Color.black);
				g.drawString(Integer.toString((int)military), x, y);
			}
			return true;
		}
	}



// 領土
	class Capital implements Draw {

		King owner;
		int id;
		double military, militaInc, militaIncBase;
		double economy, econoInc, econoIncBase;
		double total;
		int width = 0, arc;
		int x, y;
		Color color;


		Capital(int id) {
			this.id = id;
		}


		void setOwner(King owner) {
			this.owner = owner;
		}


		void init(King owner) {
			setOwner(owner);
			military = 25. + Math.random() * 25.;
			economy = 50. + Math.random() * 50.;
			total = military + economy;
		}


		void incMilitary() {
			double add = 0.;
			for(int i = 0; i < owner.capitals.size(); i++) {
				add += ((Capital)(owner.capitals.elementAt(i))).economy * .05;
				((Capital)(owner.capitals.elementAt(i))).economy *= .95;
			}
			militaIncBase += add / 2;
		}


		void incEconomy() {
			double add = 0.;
			for(int i = 0; i < owner.capitals.size(); i++) {
				add += ((Capital)(owner.capitals.elementAt(i))).economy * .05;
				((Capital)(owner.capitals.elementAt(i))).economy *= .95;
			}
			econoIncBase += add / 2;
		}


		public boolean drawing(Graphics g, Plane pln) {

			color = relation.getColor(owner.id);

			if(economy - 2. * military < 0.)
				militaInc = (economy - 2. * military) * .001;
			military += militaIncBase + militaInc;
			militaIncBase *= .7;
			military *= .99;
// 貿易效果
			econoInc = 0.;
			for(int j = 0; j < C_MAX; j++) {
				if(isCapitalConnect(id, j)
					&& !owner.isTerritory(pref[j])
					&& relation.getRelation(id, j) > 50)
					econoInc += (relation.getRelation(id, j) - 50)
						* pref[j].economy;
				else if(isCapitalConnect(id, j)
					&& owner.isTerritory(pref[j]))
					econoInc += pref[j].economy;
			}
			econoInc /= 10000;

			economy += econoIncBase + econoInc;
			econoIncBase *= .7;

			if(military < 0.) {
				economy += military;
				military = militaIncBase = 0.;
			}

			if(economy < 0.)
				military = economy = 0.;

			g.setColor(color);

//雙曲線使用 f(x)=60x/(x+p)
			total = military + economy;
			width = (int)((60. * total) / (total + p));
			arc = (int)(width * economy / (2. * military + economy));

			x = pln.posX;
			y = pln.posY;

			g.fillRoundRect(x - width / 2, y - width / 2,
				width, width, arc, arc);

			g.setColor(owner.flagColor);
			g.drawRoundRect(x - width / 2, y - width / 2,
				width, width, arc, arc);
// 顯示領土的軍事力、經濟力
			if(viewParam) {
				g.setColor(Color.black);
				g.drawString(Integer.toString((int)military), x, y);
				g.drawString(Integer.toString((int)economy), x, y + 10);
			}

			return true;
		}
	}
}

