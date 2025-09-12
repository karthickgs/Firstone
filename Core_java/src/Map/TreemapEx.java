package allMap;

import java.util.Map;
import java.util.TreeMap;

public class TreemapEx {

	public void naturalorder(char[] a) {

		Map<Character, Integer> map1 = new TreeMap<Character, Integer>();
		for (int i = 0; i <= a.length-1; i++)
		{
			map1.put(a[i], i);
		}
		System.out.println(map1);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreemapEx ex = new TreemapEx();
		char[] x = {'d','a','s','b','z'};
		ex.naturalorder(x);
	}

}
