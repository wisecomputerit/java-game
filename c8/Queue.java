// Queue.java
// written by mnagaku

import java.util.*;

/**
 * Queue類別<br>
 * 從Vector類別擴充出來的類別。
 * 為了在MacOS9之前的環境MRJ2.2.5以及MSVM上都可以使用，
 * 這裡只用了Vector類別Java1.1.x版所支援的舊方法。
 * 因為建立時不需要特別的初始化，所以使用預設建構子。
 * @author mnagaku
 */
public class Queue extends Vector {

/**
 * 存入Queue
 * @param element 要存進去的元素
 * @return 存入的元素
*/
	public Object enqueue(Object element) {
		addElement(element);
		return element;
	}


/**
 * 從Queue取出
 * @return 取出的元素。Queue裡沒有元素則會取得null
*/
	public Object dequeue() {
		Object ret;
		if(isEmpty())
			return null;
		ret = elementAt(0);
		removeElementAt(0);
		return ret;
	}
}

