// TestCurrentTimeMillis.java
// written by mnagaku

import java.applet.*;
import java.awt.*;
import java.util.*;

// 浪代Timer弘applet
public class TestCurrentTimeMillis extends Applet {
// 代刚计
	static final int TESTING_SECONDS = 5;
// 砞﹚盽计∕﹚Ω计
	int TESTING_SPLIT = 100;

	int cpuSpec, insideCount, outsideCount,
		waitCountMax = 0, waitCountMin, incTimeMax = 0, incTimeMin;
	double waitCountAverage, incTimeAverage,
		waitCountStandardDeviation, incTimeStandardDeviation;

	Vector splitDatas = new Vector();

	public void init() {
		int i, j;
		long oldTime, nowTime, newTime, rawData[];
// 代刚cpuSpec
		for(oldTime = nowTime = System.currentTimeMillis(); oldTime == nowTime;
			nowTime = System.currentTimeMillis());
		for(i = 0, newTime = nowTime;
			newTime == nowTime; i++, newTime = System.currentTimeMillis());
		waitCountMin = incTimeMin = i;

		cpuSpec = i / (int)(newTime - nowTime);
// 衡眔代刚Ω计
		insideCount = i / TESTING_SPLIT;
		outsideCount = TESTING_SECONDS * 1000 * TESTING_SPLIT
			/ (int)(newTime - nowTime);
// 眔代刚
		rawData = new long[outsideCount];
		for(i = 0; i < outsideCount; i++) {
			for(j = 0; j < insideCount; j++)
				nowTime = System.currentTimeMillis();
			rawData[i] = nowTime;
		}
// 秆猂代刚
		waitCountAverage = incTimeAverage = 0;
		for(i = 1, j = 0; i < outsideCount; i++)
			if(rawData[i - 1] != rawData[i]) {
				splitDatas.addElement(
					new SplitData(i - j, (int)(rawData[i] - rawData[i - 1])));
				if(j != 0) {
					waitCountAverage += i - j;
					if(waitCountMax < i - j)
						waitCountMax = i - j;
					else if(waitCountMin > i - j)
						waitCountMin = i - j;
				}
				incTimeAverage += (int)(rawData[i] - rawData[i - 1]);
				if(incTimeMax < (int)(rawData[i] - rawData[i - 1]))
					incTimeMax = (int)(rawData[i] - rawData[i - 1]);
				else if(incTimeMin > (int)(rawData[i] - rawData[i - 1]))
					incTimeMin = (int)(rawData[i] - rawData[i - 1]);
				j = i;
			}
// 璸计秈キА
		waitCountAverage /= splitDatas.size() - 1;
// だ秆キА
		incTimeAverage /= splitDatas.size();
// 夹非熬畉
		waitCountStandardDeviation = 0;
		incTimeStandardDeviation = Math.pow(
			((SplitData)(splitDatas.elementAt(0))).incTime
			- incTimeAverage, 2);
		for(i = 1; i < splitDatas.size(); i++) {
			waitCountStandardDeviation += Math.pow(
				((SplitData)(splitDatas.elementAt(i))).waitCount
				- waitCountAverage, 2);
			incTimeStandardDeviation += Math.pow(
				((SplitData)(splitDatas.elementAt(i))).incTime
				- incTimeAverage, 2);
		}
		waitCountStandardDeviation = Math.sqrt(
			waitCountStandardDeviation / (splitDatas.size() - 1));
		incTimeStandardDeviation = Math.sqrt(
			incTimeStandardDeviation / splitDatas.size());
	}

	public void paint(Graphics g) {
		int y = 0;

		g.drawString("cpuSpec : " + cpuSpec, 0, y += 10);
		g.drawString("cpuPower / timerResolution : " + insideCount, 0, y += 10);
		g.drawString("rawData count : " + outsideCount, 0, y += 10);
		g.drawString("splitDatas count : " + splitDatas.size(), 0, y += 10);
		g.drawString("waitCountAverage : " + waitCountAverage, 0, y += 10);
		g.drawString("waitCountMax : " + waitCountMax, 0, y += 10);
		g.drawString("waitCountMin : " + waitCountMin, 0, y += 10);
		g.drawString("waitCountStandardDeviation : "
			+ waitCountStandardDeviation, 0, y += 10);
		g.drawString("incTimeAverage : " + incTimeAverage, 0, y += 10);
		g.drawString("incTimeMax : " + incTimeMax, 0, y += 10);
		g.drawString("incTimeMin : " + incTimeMin, 0, y += 10);
		g.drawString("incTimeStandardDeviation : "
			+ incTimeStandardDeviation, 0, y += 10);

		Dimension d = getSize();

		g.drawString("splitDatas", 0, y += 10);
		for(int i = 0; i < splitDatas.size() && y < d.height; i++)
			g.drawString("no " + i + " : waitCount = "
				+ ((SplitData)(splitDatas.elementAt(i))).waitCount
				+" : incTime = "+((SplitData)(splitDatas.elementAt(i))).incTime,
				0, y += 10);
	}
}

class SplitData {
	int waitCount, incTime;

	SplitData(int waitCount, int incTime) {
		this.waitCount = waitCount;
		this.incTime = incTime;
	}
}

