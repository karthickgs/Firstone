package arraysList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lettercombinationphone {

	public List<String> phone(String alp) {

		List<String> result = new ArrayList<String>();
		String[] phone = { "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };
		result.add("");
		for (char digit : alp.toCharArray()) {

			String ph = phone[digit - '0'];
			List<String> newres = new ArrayList<String>();
			for (String b : result) {
				for (char s : ph.toCharArray()) {
					newres.add(s+b);
				}
			}
			result = newres;
		}
		return result;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Lettercombinationphone lp = new Lettercombinationphone();
		List<String>combi=  lp.phone("23");
		System.out.println(combi);
	}

}
