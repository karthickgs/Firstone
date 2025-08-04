package String;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Addingspaces {
	
	public String addingspaces(String s,int a[]) {
		
		int len = s.length();
		
		String result="";
		for(int i=0;i<len;i++) {
			result += s.charAt(i);
			for(int j:a) {
			if(result.indexOf(s.charAt(i))==j-1) {
				result = result+" ";
			}
			}
	}
		return result;
	}
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Addingspaces spc = new Addingspaces();
		String name = "Karthick";
		int []a = {2,6};
		String res=spc.addingspaces(name, a);
		System.out.println(res);
	}

}
