package String;

import java.util.ArrayList;
import java.util.List;

public class Duplicateremoval {

	public void dupremoval(String a) {

		char [] b = a.toCharArray();
		List<Character> li = new ArrayList<Character>();
		for(int i=0;i<b.length;i++) {
			if(!li.contains(a.charAt(i))) {
				li.add(a.charAt(i));
			}
			else if(li.contains(a.charAt(i))) {
				li.remove(a.charAt(i));
			}
		} System.out.println(li);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Duplicateremoval  dp = new Duplicateremoval();
		dp.dupremoval("String Good Strin Bad");
	}

}
