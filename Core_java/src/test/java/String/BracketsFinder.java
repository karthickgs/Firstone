package String;

import java.util.ArrayList;
import java.util.List;

public class BracketsFinder {

	public int parangenerate(String paran) {
		
		List<Character> par = new ArrayList<Character>();
		int countop=0,countcl=0,count=0;
		char open = '(';
		int min=0,finali=0;
		char close = ')';
		for(int i=0;i<paran.length();i++) {
			
			if(paran.charAt(i)==open&&paran.charAt(i+1)==close) {
				count++;
			}
			
			else if((paran.charAt(i)==open)) {
				countop ++;
				par.add(paran.charAt(i));
				
			}
			
			
			else if(paran.charAt(i)==close) {
				countcl++;
				par.add(paran.charAt(i));
				min = Math.min(countop, countcl);
				
				
			}
		}
		
		return count+min;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BracketsFinder bf = new BracketsFinder();
		int result=bf.parangenerate("()()()(");
		System.out.println(result);
	}

}
