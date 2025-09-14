package String;

import java.util.ArrayList;
import java.util.List;

public class PrintingDupliateCharinString {
	
	public List<Character> printduplicates(String str) {
	List<Character> lis = new ArrayList<Character>();
	List<Character> list = new ArrayList<Character>();
	char[] ch_arr = str.toCharArray();
	for(char k:ch_arr) {
		list.add(k);
	}
	for(int i=0;i<str.length();i++) {
		
		list.remove(str.indexOf(str.charAt(i)));
		if(list.contains(str.charAt(i))) {
			if(!lis.contains(str.charAt(i))){
				lis.add(str.charAt(i));
			}
		}
		list.add(str.charAt(i));
	}
	 return lis;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PrintingDupliateCharinString mm = new PrintingDupliateCharinString();
		List<Character> res=mm.printduplicates("KarthicK Sudhakar");
		System.out.println(res.toString());
	}

}
