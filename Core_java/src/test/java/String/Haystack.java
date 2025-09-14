package String;

public class Haystack {
	 public int strStr(String haystack, String needle) {
		 if(needle.compareTo(haystack)==0) {
			 return 0;
		 }
		 else if(needle.compareTo(haystack)==1) {
			 return 1;
		 }
		return -1;
		
		 
	 }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Haystack hs = new Haystack();
		hs.strStr("sadbutsad", "sad");
	}

}
