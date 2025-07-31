package arraysList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemovalofElement {
	
	public void removeele(int[] nums, int val) {
		
		List<int[]> elem = Arrays.asList(nums);
		Integer[] boxedNums = Arrays.stream(nums)         // Create an IntStream
                .boxed()             // Box each int to an Integer
                .toArray(Integer[]::new);
		
		for(Integer i:boxedNums) {
			if(elem.contains(val)) {
				elem.remove(val);
			}
		}
		
		for(int[] j:elem) {
			System.out.println(j);
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RemovalofElement re = new RemovalofElement();
		int[] a = {1,5,8,8,9,9,4,7,6};
		int b = 8;
		re.removeele(a, b);
	}

}
