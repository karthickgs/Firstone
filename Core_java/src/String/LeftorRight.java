package String;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LeftorRight {

	public class LetorRight {
	    public char[] canBeSubsequence(String str1, String str2) {
	        int n1 = str1.length();
	        int n2 = str2.length();
	        
	        // Pointers to iterate over both str1 and str2
	        int i = 0, j = 0;
	        
	        // Iterate over both strings
	        while (i < n1 && j < n2) {
	            // Check if characters match directly or cyclically
	            char c1 = str1.charAt(i);
	            char c2 = str2.charAt(j);
	            
	            if (c1 == c2) {
	                // If they match, move both pointers forward
	                i++;
	                j++;
	            } else if (canIncrement(c1, c2)) {
	                // If we can increment c1 to match c2
	                i++;
	                j++;
	            } else {
	                // If we cannot match c1 to c2, just move i forward
	                i++;
	            }
	        }
			return null;
	        
	       
	    }

	    // Helper function to check if a character can be incremented to match another
	    private boolean canIncrement(char c1, char c2) {
	        return (c1 - 'a' + 1) % 26 == (c2 - 'a');  // Cyclic increment check
	    }
	    
	    public static void main(String[] args) {
	    	LeftorRight solution = new LeftorRight();
	        
	        String str1 = "abc";
	        String str2 = "khd";
	        
	        System.out.println( solution.canBeSubsequence(str1, str2)); // Output: true
	    }
	}

	

	

	}
