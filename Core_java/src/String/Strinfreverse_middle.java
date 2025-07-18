package String;

import java.lang.classfile.components.ClassPrinter.ListNode;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Strinfreverse_middle{

	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		
		Arrays.copyOf(nums1, nums1.length+nums2.length);
		System.arraycopy(nums1, 0, nums2, nums1.length, nums2.length);
		System.out.println(Arrays.toString(nums1));
		return 0;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Strinfreverse_middle str = new Strinfreverse_middle();
		int [] a = {1,2,3,4};
		int [] b = {5,6,2,1};
		str.findMedianSortedArrays(a, b);
	}

}
