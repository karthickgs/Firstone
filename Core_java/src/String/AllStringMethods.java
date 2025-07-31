package String;

public class AllStringMethods {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str1 = "I am the best tester in the world";
		String str2 = "Karthick is best cook in the earth";
		String str3 = "";
		char [] char1 = {'a','b','c','d','e','f'};
		
		//Content equals
		System.out.println(str1.contentEquals("I am the best tester in the world"));
		System.out.println(str2.contentEquals("best"));
		
		//getchars
		
		str2.getChars(5, 7, char1, 4);
		System.out.println(char1);
		
		//cpoyvalueof
		
		str3 = str3.copyValueOf(char1, 3, 2);
		System.out.println(str3);
		
		//regionmatches
		
		System.out.println(str3.regionMatches(true, 9, str2, 13, 17));
		
		
	}

}
