package array;

public class SecondLargestElement {
	
	public int twopasssearch(int [] a) {
		
		
			int largest=0;
			int second_largest =0;
			for(int i=0;i<=a.length-1;i++) {
			if(a[i]>largest) {
				largest = a[i];
			}
		}
			for(int j=0;j<a.length-1;j++) {
				if(largest>a[j] && a[j]>second_largest) {
					second_largest = a[j];
				}
			}
		return second_largest;
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int [] x = {2,13,8,10,37};
		SecondLargestElement sle = new SecondLargestElement();
		int s=sle.twopasssearch(x);
		System.out.println(s);
		

	}

}
