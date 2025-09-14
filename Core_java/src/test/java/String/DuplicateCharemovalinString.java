package String;

import java.util.ArrayList;
import java.util.List;

public class DuplicateCharemovalinString {

	public void dupremoval(String a) {

		char [] b = a.toCharArray();
		List<Character> li = new ArrayList<Character>();
		for(int i=0;i<b.length;i++) {
			if(!li.contains(a.charAt(i))) {
				li.add(a.charAt(i));
			}
			else if(li.contains(a.charAt(i))) {
				li.remove(li.indexOf(a.charAt(i)));
				
			}
		} 
		System.out.println(li.toString());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DuplicateCharemovalinString  dp = new DuplicateCharemovalinString();
		dp.dupremoval("String Good Strin Bad");
	}

}
