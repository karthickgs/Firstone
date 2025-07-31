package String;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CharOccurence {
   public void addchar(String cha) {
	   
	   for(int i=0;i<=cha.length()-1;i++) {
		   int count=0;
		   for(int j=0;j<cha.length()-1;j++) {
			   if(cha.charAt(i)==cha.charAt(j)) {
				   count++;
			   }
		   }
		   System.out.println("Occurances of '"+cha.charAt(i)+"':"+count);
	   }
   }
   
   public String reverseString(char[] s) {
       String res= " ";
       for(int i=0;i<s.length;i++){
           
           res=s[i]+res;
           
       }
        return res;
   }
   public void lengthOfLongestSubstring(String s) {
	  
        StringBuilder str = new StringBuilder();
        for(int i=0;i<s.length();i++) {
        	int j=1;
        	if(s.charAt(i)!=s.charAt(j)) {
        		String a = String.valueOf(s.charAt(i)).concat(String.valueOf(s.charAt(j)));
        		str = str.append(a);
        		j++;
        	}
        	
        }
        System.out.println(str);
        
       }
   public char[] reverse(char[] s) {
       char[] res= new char[s.length];
       int count=0;
       for(int i=s.length-1;i>=0;i--){
           
          res[count] = s[i];
           count ++;
       }
        return res;
   }

    public static void main(String[] args) {
        
        CharOccurence chars= new CharOccurence();
//      chars.addchar("Kaammmrthick");
//      chars.lengthOfLongestSubstring("n");
        char [] s = {'H','e','l','l','o'};
        String res=chars.reverseString(s);
        System.out.println(res);
        char [] x = chars.reverse(s);
        for(char i:x) {
        	System.out.println(i);
        }
    }
}

