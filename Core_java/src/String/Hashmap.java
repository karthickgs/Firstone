package String;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Hashmap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HashMap<String,String> hashmap  = new HashMap<String,String>();
		hashmap.put("India","Delhi");
		hashmap.put("Russia","Moscow");
		hashmap.put("Italy","Rome");
		hashmap.put("France","paris");
		hashmap.put("paksistan","Islamabad");
		HashMap<String,String> hashmap2  = new HashMap<String,String>();
		hashmap2.put("India","Mumbai");
		hashmap2.put("Russia","Dagisthan");
		hashmap2.put("Italy","Colessium");
		hashmap2.put("France","Austria");
		hashmap2.put("paksistan","Punjab");
		System.out.println(hashmap.size());
		hashmap2.forEach((key, value)->hashmap.merge(key,value,(v1,v2)->v1.equalsIgnoreCase(v2)?v1:v1+","+v2));
		System.out.println(hashmap);
		
		TreeMap<String,Integer> tmap = new TreeMap<String,Integer>();
		tmap.put("Bit", 8);
		tmap.put("Integer", 32);
		tmap.put("Char", 16);
		tmap.put("Long", 64);
		TreeMap<String,Integer> tmap1 = new TreeMap<String,Integer>();
		tmap1.put("Long", 12);
		System.out.println(tmap);
		tmap1.forEach((key,value)->tmap.merge(key, value, Integer::sum));
		System.out.println(tmap);
		
		
		
	}

}
