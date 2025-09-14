package arraysList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bannednumbers {
	
	public void banned(int n, int maxsum, int [] banned) {
		int sum = 0;

		List<Integer> a = new ArrayList<Integer>();
		List<Integer> b = new ArrayList<Integer>();
		for(int i:banned) {
			a.add(i);
		}
		for(int j=1;j<=n;j++) {
			int count=0;
			
			 if (!a.contains(j)) {
				 b.add(j);
		           }
	            }
			System.out.println(b.toString());
			
//	          System.out.println(Arrays.toString(arr1)); 
//	            if (sum + j <= maxsum) {
//	                sum += j;
//	                count++;
//	            } else {
//	                break; // If the sum exceeds maxSum, stop adding numbers
//	            }
		}
		
//		return sum;
//	
//	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Bannednumbers bn = new Bannednumbers();
		int [] arr = {1,5,6};
		bn.banned(7, 6, arr);
	}

}
