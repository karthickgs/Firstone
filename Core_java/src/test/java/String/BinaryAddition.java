package String;

public class BinaryAddition {
	public void addbinary(String s1,String s2) {
		
		int bin1 = Integer.parseInt(s1);
		int bin2 = Integer.parseInt(s2);
		int current1=0;
		int current2 =0;
		int carry =0;
		int remain=0;
		for(int i=0;i<s1.length();i++) {
			
			current1 = bin1%2;
			current2 = bin2%2;
			carry = (current1+current2)/2;
			remain = (current1+current2)%2;
			
			
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
