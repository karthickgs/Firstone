package String;

import java.util.*;

public class RemoveDuplicateCharacters {
	
    public static void main(String[] args) {
 
        String input = "karthick Sudhakar";
        StringBuilder result = new StringBuilder();
        List<Character> seen = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            char[] a = input.toCharArray();
            if (!seen.contains(currentChar)) {
                result.append(currentChar);
                seen.add(currentChar); 
            }
        }
        System.out.println(result.toString());
        System.out.println(seen);
    }
}
