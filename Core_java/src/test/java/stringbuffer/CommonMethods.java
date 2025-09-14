package stringbuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonMethods {
	
	StringBuilder sb = new StringBuilder("qre");
	
	public void insertionoperation() {
		char[] chr= new char[]{'a','b','c'};
		sb.insert(2, true);
		System.out.println(sb.toString());
		sb.insert(0, "karthick", 5, 6); // 0-index , 5-Starting char,6-Ending char
		System.out.println(sb.toString());
		sb.insert(0, chr, 1, 2);
		System.out.println(sb.toString());
	}
	public void increment() {
		StringBuilder s = new StringBuilder("karthick");
		int i=0,r=0;
		s.setCharAt(++i, s.charAt(r+2));
		System.out.println(s.toString());
		
	}
	public void removeSpacewithZeroes(String s) {
		StringBuilder sb = new StringBuilder(s);
		List<Integer> alldots = new ArrayList<>();
		int inx = s.indexOf('.');
		while(inx>=0) {
			alldots.add(inx);
			s.indexOf('.',inx+1);
		}
		for(int i=0;i<s.length();i++) {
			if(s.charAt(i)=='.') {
				sb.insert(alldots.indexOf(i), '0');
			}
		}
		System.out.println(sb.toString());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonMethods cm = new CommonMethods();
//		cm.insertionoperation();
//		String s = "kar.thi.cks.udh.akar";
//		cm.removeSpacewithZeroes(s);
		cm.increment();
	}

}
