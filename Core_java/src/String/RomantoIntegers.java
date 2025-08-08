package String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RomanLetters {

		    // Function to convert Roman numeral to integer
		    public static int romanToInt(String s) {
		        // Define a map for Roman numeral symbols and their integer values
		        java.util.Map<Character, Integer> romanMap = new java.util.HashMap<>();
		        romanMap.put('I', 1);
		        romanMap.put('V', 5);
		        romanMap.put('X', 10);
		        romanMap.put('L', 50);
		        romanMap.put('C', 100);
		        romanMap.put('D', 500);
		        romanMap.put('M', 1000);

		        int result = 0;

		        // Loop through the Roman numeral string
		        for (int i = 0; i < s.length(); i++) {
		            // Get the value of the current Roman numeral symbol
		            int currentVal = romanMap.get(s.charAt(i));

		            // If this symbol is smaller than the next symbol, subtract it
		            if (i + 1 < s.length() && currentVal < romanMap.get(s.charAt(i + 1))) {
		                result -= currentVal;
		            } else {
		                // Otherwise, add it
		                result += currentVal;
		            }
		        }

		        return result;
		    }
		    
		    public static String longestCommonPrefix(String[] strs) {
		    	
		    	List<String> com = new ArrayList<String>();
		    	String s =" ";
		    	for(int i=0;i<strs.length;i++) {
		    		for(int j=0;j<=strs[i].length();j++) {
		    	if(String.valueOf(strs[i].charAt(j)).equalsIgnoreCase(String.valueOf(strs[i+1].charAt(j+1)))) {
		    		StringBuffer bs = new StringBuffer();
		    		bs.append(String.valueOf(strs[i].charAt(i)));
		    	
		    		 s =bs.toString();
		    	}
		    	else {
		    		System.out.println("There is no matching  prefix in common");
		    	}
		    	
		    		}
		    	}
				return s;
				
		    	
		    } 

		    public static void main(String[] args) {
		        // Test cases
//		        String[] romanNumbers = {"MMXXIV", "MCMLXXXVII", "MMMCMXCIX", "XLIV", "IX", "LVIII","MMM"};
//
//		        // Loop through the Roman numerals and convert each to integer
//		        for (String roman : romanNumbers) {
//		            System.out.println(roman + " is equal to: " + romanToInt(roman));
		    	
		    	String [] common = {"flower","friends","fry"};
		    	for(String i:common) {
		    		System.out.println(longestCommonPrefix(common));
		    	}
		        }
		    }
		

	