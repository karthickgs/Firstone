package String;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Addingspaces {
	
	public void addingspaces(String s,int []a) {
		for(int i:a) {
			String space = "//+s";
			Map<String,Integer> addlist = new TreeMap();
			addlist.put(s, s.indexOf(i) );
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
