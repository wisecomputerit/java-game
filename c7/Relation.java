// Relation.java
// written by mnagaku

import java.awt.*;

// ��D�ͱ������O
class Relation {

	int arr[][], gLine, bLine;

	Relation(int no) {
		arr = new int[no - 1][];
		for(int i = 0; i < no - 1; i++)
			arr[i] = new int[no - 1 - i];
	}

// �H�üƪ�l�ƻ�D�������Y
	void init() {
		int bad = 100;
		for(int i = 0; i < arr.length; i++)
			for(int j = 0; j < arr[i].length; j++) {
				arr[i][j] = (int)(Math.random() * 100.);
				if(i != 0 && arr[i][j] < bad) {
					bad = arr[i][j];
					gLine = i;
					bLine = j + 1;
				}
			}
	}

// ���o2�H�����Y
	int getRelation(int no1, int no2) {
		int line = no1 < no2 ? no1 : no2;
		int no = no1 > no2 ? no1 - line - 1 : no2 - line - 1;
		if(no1 == no2 || line < 0 || line >= arr.length
			|| no < 0 || no >= arr[line].length)
			return -1;
		return arr[line][no];
	}

// ����2�H���ͱ�
	int incRelation(int no1, int no2) {
		int line = no1 < no2 ? no1 : no2;
		int no = no1 > no2 ? no1 - 1 : no2 - 1;
		if(no1 == no2 || line < 0 || line >= arr.length
			|| no < 0 || no >= arr[line].length)
			return -1;
		arr[line][no]++;
		if(arr[line][no] > 100)
			arr[line][no] = 100;
		return arr[line][no];
	}

// ���C2�H���ͱ�
	int decRelation(int no1, int no2) {
		int line = no1 < no2 ? no1 : no2;
		int no = no1 > no2 ? no1 - 1 : no2 - 1;
		if(no1 == no2 || line < 0 || line >= arr.length
			|| no < 0 || no >= arr[line].length)
			return -1;
		arr[line][no]--;
		if(arr[line][no] < 0)
			arr[line][no] = 0;
		return arr[line][no];
	}

// src��dist�������Y�ﵽ���ܡA���ȶȬO2�H�����Y�ﵽ�A
// �Mdist���Y�n����D�����Y�]�|�ܦn�Adist�M���Y�a�]�ܱo�a�C
	int changeRelation(int src, int dist) {
		for(int i = 0; i < arr.length; i++) {
			if(i != src && i != dist && getRelation(dist, i) > 75)
				incRelation(src, i);
			else if(i != src && i != dist && getRelation(dist, i) < 25)
				decRelation(src, i);
		}
		return incRelation(src, dist);
	}

// �Ǧ^�ΨӶ���w��D���аO�ϧΪ��C��
	Color getColor(int no) {
		if(no == 0)
			return Color.red;
		else if(no == gLine)
			return new Color(Color.HSBtoRGB((float)(.5-getRelation(0,no)/200.),
				(float)1., (float)1.));
		else if(no == bLine)
			return new Color(Color.HSBtoRGB((float)(.5+getRelation(0,no)/200.),
				(float)1., (float)1.));

		int p = getRelation(0, no);
		int g = getRelation(no, gLine);
		int b = getRelation(no, bLine);

		double h, s;

		h = Math.atan2((g - b) * (Math.sqrt(100. * p - p * p)) / (50. * (g + b)),
			p / 50. - 1.) / (2. * Math.PI);
		if(h < 0.)
			h += 1.;

		s = Math.sqrt(Math.pow(p / 50. - 1., 2.)
			+ Math.pow((g - b) * (Math.sqrt(100. * p - p * p)) / (50. * (g + b)),
			2.));

		return new Color(Color.HSBtoRGB((float)h, (float)s, (float)1.));
	}
}

