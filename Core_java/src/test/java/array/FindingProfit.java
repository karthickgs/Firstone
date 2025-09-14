package array;

public class FindingProfit {

	public int profit(int booking[]) {
		
		int profit =0;
		int max=0;
		for(int i=0;i<booking.length;i++) {
			for(int j=0;j<booking.length;j++) {
				
				
				profit = Math.abs(booking[i]- booking[j]);
				
				if(profit>max) {
					
					max = profit;
				}
			}
			
			
		}
		return max;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FindingProfit fp = new FindingProfit();
		int[] profit = {1,24,57,8,9,1};
		System.out.println(fp.profit(profit));
		
		
	}

}
