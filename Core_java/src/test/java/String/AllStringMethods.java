package String;

public class AllStringMethods {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str1 = "I am the best tester in the world";
		String[] str4= {};
		String str2 = "Karthick is best cook in the earth";
		for(int i=0;i<str2.length();i++) {
			str4 = str2.split("\\s");
		}
		String str3 = "kar";
		char [] char1 = {'a','b','c','d','e','f'};
	
		
		//Content equals
		System.out.println(str1.contentEquals("I am the best tester in the world"));
		System.out.println(str2.contentEquals("best"));
		
		//getchars
		
		str2.getChars(5, 8, char1, 2);
		System.out.println(char1);
		
		//cpoyvalueof
		
		str3=str3.copyValueOf(char1, 1, 2);
		System.out.println(str3);
	
		//regionmatches
		for(String s:str4) {
		boolean b =str1.contains(s);
		System.out.println(b);
		
		
		}
		
	}

}
