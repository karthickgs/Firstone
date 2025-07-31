package arraysList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bannednumbers {
	
	public int banned(int n, int maxsum, int [] banned) {
		int sum = 0;
		List<Integer> a = new ArrayList<Integer>();
		for(int i:banned) {
			a.add(i);
		}
		for(int j=1;j<=n;j++) {
			int count=0;
			 if (a.contains(j)) {
	                continue;
	            }
			 int[] arr1 = new int[5];
	           for(int k:arr1) {
	        	   arr1[k] = j;
	           }
	          System.out.println(Arrays.toString(arr1)); 
//	            if (sum + j <= maxsum) {
//	                sum += j;
//	                count++;
//	            } else {
//	                break; // If the sum exceeds maxSum, stop adding numbers
//	            }
		}
		
		return sum;
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Bannednumbers bn = new Bannednumbers();
		int [] arr = {1,5,6};
		System.out.println(bn.banned(7, 6, arr));
	}

}
