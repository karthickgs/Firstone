package array;

public class ThirdLargestNumber {

	public int[] numthree(int []a) {
		int length = a.length;
		int largest=0;
		int second_largest =0;
		int third_largest =0;
		for(int i=0;i<length;i++) {
		if(a[i]>largest) {
			third_largest =second_largest; // 21 
			second_largest =largest;  // 11
			largest = a[i]; // 11
			
		}
		else if(a[i]>third_largest&&second_largest>third_largest) {
			third_largest =second_largest;
			second_largest = a[i];
			}
			
		else if(a[i]>third_largest&&second_largest>third_largest&&largest>second_largest) {
			third_largest = a[i];
		}
		
		return int[3] a = new int[] {largest,second_largest,third_largest};
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int [] x = {2,5,6,1};
		SecondLargestElement sle = new SecondLargestElement();
		int s=sle.twopasssearch(x);
		System.out.println(s);
	}

}
