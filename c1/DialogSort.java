// DialogSort.java
// written by mnagaku

import java.io.*;
import java.util.*;

class DialogSort {

    public static void main(String args[]) throws IOException {
        int i, j, tmp;
        int[] dataArray;
        String str;
        Vector data = new Vector();
        BufferedReader br
            = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.println("�{�b��J���ƭ�");
            if(data.isEmpty())
                System.out.println("�S����J");
            else {
                for(i = 0; i < data.size(); i++) {
                    if(i % 4 == 3)
                        System.out.println("["+i+"]"+data.elementAt(i));
                    else
                        System.out.print("["+i+"]"+data.elementAt(i)+", ");
                }
                if(i % 4 != 0)
                    System.out.println("");
            }
            System.out.println("a:�s�W�ƭ�");
            System.out.println("d:�R���ƭ�");
            System.out.println("s:�ƧǼƭ�");

            str = br.readLine();
            if(str.compareTo("d") == 0) {
                System.out.println("�п�J�Q�n�R�����ƭȽs��");
                str = br.readLine();
                tmp = Integer.parseInt(str);
                data.removeElementAt(tmp);
            }
            else if(str.compareTo("a") == 0) {
                System.out.println("�п�J�Q�n�s�W���ƭ�");
                str = br.readLine();
                tmp = Integer.parseInt(str);
                data.addElement(new Integer(tmp));
            }
            else
                break;
        }

        if(data.size() == 0) {
            System.out.println("�S���ƭ�");
            return;
        }

        System.out.println("�Ƨǵ��G");

        dataArray = new int[data.size()];
        for(i = 0; i < data.size(); i++)
            dataArray[i] = ((Integer)data.elementAt(i)).intValue();
        for(i = 0; i < dataArray.length - 1; i++) {
            for(j = i; j < dataArray.length; j++)
                if(dataArray[i] > dataArray[j]) {
                    tmp = dataArray[i];
                    dataArray[i] = dataArray[j];
                    dataArray[j] = tmp;
                }
            System.out.println(dataArray[i]);
        }
        System.out.println(dataArray[i]);
    }
}

