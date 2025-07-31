package arraysList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsetOfIntegers {

	public List<List<Integer>> subset(int[]x ){
		
		List<Integer> arr = new ArrayList<Integer>();
		List<Integer> arr1 = new ArrayList<Integer>();
		for(int k:x) {
			arr.add(k);
		}
		int j;
		int length =arr.size() ;
		for(int i=0;i<length;i++) {
			for(j=0;j<length;j++) {
				arr1.add(arr.get(i));
				
			}
			length--;
		}
		return null;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
