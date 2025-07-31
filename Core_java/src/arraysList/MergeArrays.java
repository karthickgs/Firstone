package arraysList;

import java.util.Arrays;
import java.util.List;

public class MergeArrays {

	public static void main(String[] args) {
		int [] x = {2,5,6,12,4};
		int [] y = {10,2,9,34,2};
		System.arraycopy(x, 0, y, 2, 2);
		for(int i:y) {
			System.out.println(i);
		}
	}

}
