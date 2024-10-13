// Queue.java
// written by mnagaku

import java.util.*;

/**
 * Queue���O<br>
 * �qVector���O�X�R�X�Ӫ����O�C
 * ���F�bMacOS9���e������MRJ2.2.5�H��MSVM�W���i�H�ϥΡA
 * �o�̥u�ΤFVector���OJava1.1.x���Ҥ䴩���¤�k�C
 * �]���إ߮ɤ��ݭn�S�O����l�ơA�ҥH�ϥιw�]�غc�l�C
 * @author mnagaku
 */
public class Queue extends Vector {

/**
 * �s�JQueue
 * @param element �n�s�i�h������
 * @return �s�J������
*/
	public Object enqueue(Object element) {
		addElement(element);
		return element;
	}


/**
 * �qQueue���X
 * @return ���X�������CQueue�̨S�������h�|���onull
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

