package arraysList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.*;

public class foursum {
    
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        
        // Sort the array to apply two-pointer technique
        Arrays.sort(nums);
        
        // Iterate through the array with the first pointer
        for (int i = 0; i < nums.length - 3; i++) {
            // Skip the duplicate elements for the first pointer
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            // Iterate with the second pointer
            for (int j = i + 1; j < nums.length - 2; j++) {
                // Skip duplicate elements for the second pointer
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;
                
                int left = j + 1;  // Left pointer
                int right = nums.length - 1;  // Right pointer
                
                // Use two pointers to find the last two numbers
                while (left < right) {
                    int sum = nums[i] + nums[j] + nums[left] + nums[right];
                    
                    if (sum == target) {
                        // Found a valid quadruplet
                        result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        
                        // Move left pointer and skip duplicates
                        while (left < right && nums[left] == nums[left + 1]) left++;
                        // Move right pointer and skip duplicates
                        while (left < right && nums[right] == nums[right - 1]) right--;
                        
                        // Move both pointers inward
                        left++;
                        right--;
                    } else if (sum < target) {
                        // If sum is less than target, move left pointer to the right
                        left++;
                    } else {
                        // If sum is greater than target, move right pointer to the left
                        right--;
                    }
                }
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        int[] nums = {1, 0, -1, 0, -2, 2};
        int target = 2;
        List<List<Integer>> quadruplets = fourSum(nums, target);
        
        // Print the result
        for (List<Integer> quadruplet : quadruplets) {
            System.out.println(quadruplet);
        }
    }
}


