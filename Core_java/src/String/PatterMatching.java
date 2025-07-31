package String;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatterMatching {
	
	public void patternregex() {
		
		String str = "My name is abiman and my mail id is abIman@gmail.com";
		String regex = "([aA-zZ]+@gmail.com)";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(str);
		
		if(mat.find()) {
			String st = mat.group();
			System.out.println(st);
		}
		else {
			System.out.println("No matchers found");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PatterMatching pm = new PatterMatching();
		pm.patternregex();
	}

}
