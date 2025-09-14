package array;

public class ReversetheArrayinGivenSize {
	
	public static int[] reversethearray(int []a,int seg) {
	
		int b[] = new int[a.length];
		for(int i=0;i<a.length;i+=seg) {
		
			int left =i;
			int right = Math.min(i+seg-1, a.length-1);
			int kd = left;
			while(left<=right) {
				
				b[kd++]=a[right--];
			}
			
		}
		return b;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int b[] = {1,2,3,4,5,1,2,4};
		int c[]=reversethearray(b, 4);
		for(int j:c) {
			System.out.println(j);
		}
	}

}
