// Ranking.java
// written by mnagaku

/**
 * Ranking類別<br>
 * 保存排行資訊
 * @author mnagaku
 */
class Ranking {
	String names[];
	int points[], max, sortParam = 1;


/**
 * 建構子
 * 保存排行資訊的準備
 * @param max 保存到幾位為止的排序資訊
 */
	Ranking(int max) {
		this.max = max;
		names = new String[max];
		points = new int[max];
		for(int i = 0; i < max; i++) {
			names[i] = "player" + (i + 1);
			points[i] = (i + 1) * 10;
		}
	}


/**
 * 建立排行榜的字串
 * @return 排行榜的字串
 */
	String makeString() {
		String ret = "RANKING ";
		for(int i = 0; i < max; i++)
			ret += names[i] + " " + points[i] + " ";
		return ret;
	}


/**
 * 加入排行
 * @param name 名稱
 * @param point 得分
 * @return 會被排在第幾名? 未被列入排行榜者則為max
 */
	int addRanking(String name, int point) {
		int i, j;
		for(i = 0; i < max && points[i] * sortParam >= point * sortParam; i++);
		if(i >= max)
			return i;
		for(j = max - 1; j > i; j--) {
			points[j] = points[j - 1];
			names[j] = names[j - 1];
		}
		points[i] = point;
		names[i] = name;
		return i;
	}


/**
 * 排行資訊的排序為降冪
 */
	void big2small() {
		sortParam = 1;
		sort();
	}


/**
 * 排行資訊的排序為昇冪
 */
	void small2big() {
		sortParam = -1;
		sort();
	}


/**
 * 排序
 */
	void sort() {
		if(max < 2)
			return;
		int i, j, tmpPoint;
		String tmpName;
		for(i = 0; i < max - 1; i++)
			for(j = i + 1; j < max; j++)
				if(points[i] * sortParam < points[j] * sortParam) {
					tmpPoint = points[i];
					points[i] = points[j];
					points[j] = tmpPoint;
					tmpName = names[i];
					names[i] = names[j];
					names[j] = tmpName;
				}
	}
}
