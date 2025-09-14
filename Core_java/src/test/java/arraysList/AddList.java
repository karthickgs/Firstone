package arraysList;

import java.util.Arrays;

public class AddList {
	
	public float add(int []nums1,int []nums2) {
	int [] mergedArr=Arrays.copyOf(nums1, nums1.length+nums2.length);
	System.arraycopy(nums2, 0, mergedArr, nums1.length, nums2.length);
	System.out.println(Arrays.toString(mergedArr));
	int sie=mergedArr.length;
	float mid=0f;
	if(sie%2==0) {
	 mid = (mergedArr[0]+mergedArr.length-1)/2;
	 System.out.println(mid);
	}
	return mid;
}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AddList addL = new AddList();
		int [] a = {1,2,3,4};
		int [] b = {2,23,4,6};
		addL.add(a, b);

	}

}
