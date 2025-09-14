package array;

public class AddOne {

	
	public int addoneintonum(int[]num) {
		
		int sum =0;
		int addnum =1;
			
			for(int i=0;i<num.length;i++) {
				
				sum = sum*10 +num[i];
				
				}
			
			sum+=addnum;
			return sum;
			}
		
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AddOne one = new AddOne();
		int [] c = {1,4,9};
		int res=one.addoneintonum(c);
		System.out.println(res);
	}

}
