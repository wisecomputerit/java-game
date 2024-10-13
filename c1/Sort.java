// Sort.java
// written by mnagaku

class Sort {

	public static void main(String args[]) {
		int i, j, tmp;
		int[] data = new int[args.length];
		for(i = 0; i < data.length; i++)
			data[i] = Integer.valueOf(args[i]).intValue();
		for(i = 0; i < data.length - 1; i++) {
			for(j = i; j < data.length; j++)
				if(data[i] > data[j]) {
					tmp = data[i];
					data[i] = data[j];
					data[j] = tmp;
				}
			System.out.println(data[i]);
		}
		System.out.println(data[i]);
	}
}

