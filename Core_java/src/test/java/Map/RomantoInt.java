package Map;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RomantoInt {
	
	private static Map<String,Integer> Romanc = new LinkedHashMap<String,Integer>();
	int k=0;
	

	
	public static int Intconversion(String Roman) {
		
		Romanc.put("M", 1000);
		Romanc.put("D", 500);
		Romanc.put("C", 100);
		Romanc.put("L", 50);
		Romanc.put("X", 10);
		Romanc.put("V", 5);
		Romanc.put("I", 1);
		
		for(String j:Romanc.keySet()) {
			
			
		}
		
		char[] RomanChar = Roman.toCharArray();
		for(Map.Entry<String,Integer>  entry:Romanc.entrySet()) {
			
			String getKey=entry.getKey();
			for(char i:RomanChar) {
				
				if(getKey.equals(i)) {
					
				}
			}
		}
		
		return 1;
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
