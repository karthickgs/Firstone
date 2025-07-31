package String;

public class RegexString {
	
	public boolean compareregex(String src,String dest) {
		Character ques = '?';
		Character aste = '*';
		int srclen = src.length();
		int destlen = dest.length();
		int max = srclen>destlen?srclen:destlen;
		if(src.contains(String.valueOf(aste))||dest.contains(String.valueOf(aste))) {
			return true;
		}
		for(int i=0;i<=max-1;i++) {
			try {
			if(src.charAt(i)==dest.charAt(i)) {
				System.out.println(src.charAt(i) +" in the "+i+" th character");
				continue;
			}
			
			else if(src.charAt(i)==ques||dest.charAt(i)==ques) {
				System.out.println("String dest or src has question mark in "+i+"th place");
				continue;
			}
			else {
				System.out.println("No asterick or questtion mark");
			}
			
			}catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
				System.out.println(e.getCause());
			}
		}
		
		return false;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RegexString reg = new RegexString();
		reg.compareregex("ba", "ab?");
	}

}
