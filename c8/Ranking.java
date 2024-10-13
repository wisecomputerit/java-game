// Ranking.java
// written by mnagaku

/**
 * Ranking���O<br>
 * �O�s�Ʀ��T
 * @author mnagaku
 */
class Ranking {
	String names[];
	int points[], max, sortParam = 1;


/**
 * �غc�l
 * �O�s�Ʀ��T���ǳ�
 * @param max �O�s��X�쬰��ƧǸ�T
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
 * �إ߱Ʀ�]���r��
 * @return �Ʀ�]���r��
 */
	String makeString() {
		String ret = "RANKING ";
		for(int i = 0; i < max; i++)
			ret += names[i] + " " + points[i] + " ";
		return ret;
	}


/**
 * �[�J�Ʀ�
 * @param name �W��
 * @param point �o��
 * @return �|�Q�Ʀb�ĴX�W? ���Q�C�J�Ʀ�]�̫h��max
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
 * �Ʀ��T���ƧǬ�����
 */
	void big2small() {
		sortParam = 1;
		sort();
	}


/**
 * �Ʀ��T���ƧǬ��@��
 */
	void small2big() {
		sortParam = -1;
		sort();
	}


/**
 * �Ƨ�
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
