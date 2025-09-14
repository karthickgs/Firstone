package String;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class formaContainer {
	private int [] height;

	public void addcontain(int[] height) {
//		int [] a = height;
//		int temp, b,c,d;
//		for(int i=0;i<=a.length;i++) {
//			if(a[i]<a[i+1]) {
//				temp = a[i];
//				a[i]=a[i+1];
//				a[i+1] = temp;
//			}
//			b = a[i];
//			c= a[i+1];
//			d= a[i+2];
//		}
//		for(int i=0;i<height.length;i++) {
//			
//			}
		List<Integer> a = new ArrayList<>();
		this.height = height;
		int mul=0;
		for(int i=1;i<=height.length;i++) {
			mul = i*height[i-1];
			a.add(mul);
			
		}
		System.out.println(a);
}		
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		formaContainer fo = new formaContainer();
		int arr[] = {1,45,2,7,22,11};
		fo.addcontain(arr);
	}

}
