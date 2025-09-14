package array;

import java.util.List;

public class ConsecutiveMaxIntegers {
	public void addmax(int []a) {

	int maxCount = 1;
	int currentCount = 1;
	int maxNumber = a[0];

	for(int i = 1;i<a.length;i++)
	{
		if (a[i] == a[i - 1]) {
			currentCount++;
		} else {
			currentCount = 1; // reset for a new number
		}

		if (currentCount > maxCount) {
			maxCount = currentCount;
			maxNumber = a[i];
		}
	}

	System.out.println("The maximum repetition is "+maxCount+" and the number is "+maxNumber);
}

public static void main(String[] args) {
    ConsecutiveMaxIntegers cmi = new ConsecutiveMaxIntegers();
    int[] x = {1, 0, 0, 0, 1, 1, 1, 1, 0};
    cmi.addmax(x);
}
}
