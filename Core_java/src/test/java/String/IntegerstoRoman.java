package String;

import java.util.*;
import java.util.Map.Entry;

public class IntegerstoRoman {

	public void toRoman(int integer) {

		Map<Integer, Character> map = new TreeMap<>();
		map.put(1000, 'M');
		map.put(500, 'D');
		map.put(100, 'C');
		map.put(50, 'L');
		map.put(10, 'X');
		map.put(5, 'V');
		map.put(1, 'I');

	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
