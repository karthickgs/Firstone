package arraysList;
import java.util.*;

public class Maxbanned {
	
	public int maxCount(int[] banned, int max) {
        // Convert int[] to Integer[] so we can use Arrays.asList()
        List<Integer> bannedList = new ArrayList<>();
        for (int num : banned) {
            bannedList.add(num);
        }
        
        // Now, iterate through the numbers from 0 to max and remove banned numbers
        int count = 0;
        for (int i = 0; i <= max-1; i++) {
            if (!bannedList.contains(i)) {
            	
            	count++;
            	System.out.println(count);
            }
        }
    // Return the count of numbers not in the banned list
		return count;
	}
	
	public static void main(String[] args) {
		Maxbanned max = new Maxbanned();
		int[] arr = {1, 3, 5, 7}; // Banned numbers
		int result = max.maxCount(arr, 29); // Max value to check up tok	
		System.out.println(result); // Output the result
	}
}
