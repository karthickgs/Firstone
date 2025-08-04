package array;

public class FindingProfit {

	public void profit(int booking[]) {
		
		int profit =0;
		int max=0;
		for(int i=0;i<booking.length;i++) {
			for(int j=0;j<booking.length;j++) {
				
				int temp = profit;
				profit = booking[i]- booking[j];
				if(profit>temp) {
					
					max = profit;
				}
				
			}

			
		}
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FindingProfit fp = new FindingProfit();
		int[] profit = {1,24,57,8,9,1};
		fp.profit(profit);
		
		
	}

}
