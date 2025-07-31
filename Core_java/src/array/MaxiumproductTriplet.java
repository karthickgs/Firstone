package array;

import java.util.ArrayList;
import java.util.List;

public class MaxiumproductTriplet {

	public int triplet(int []a) {
		
		int largest =-1;
		int second_largest =-1;
		int third_largest =-1;
		int sum =0;
		List<Integer> threedigits = new ArrayList<>();
		for(int i:a) {
			
			if(i>largest) {
				largest=i;
				
				
			}
		}
			for(int j:a) {
				if(j>second_largest&&j<largest) {
					second_largest=j;
					
				}
			}
			
			for(int k:a) {
		
			if(k>third_largest&&k<second_largest&&second_largest>third_largest) {
				third_largest=k;
		
			}
			
		}
			
		sum = largest*second_largest*third_largest;
		System.out.println(largest+" "+second_largest+" "+third_largest);
		return sum;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MaxiumproductTriplet m = new MaxiumproductTriplet();
		int[] b = { 2, 4, 10, 1, 5, 7 };
		int multiply = m.triplet(b);
		System.out.println(multiply);
		

	}

}
