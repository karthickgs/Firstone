package arraysList;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class Permutations {
	public List<List<Integer>> permute(int[] nums) {
		List<List<Integer>> result = new ArrayList<>();
		backtrack(nums, result, new ArrayList<>(), new boolean[nums.length]);
		return result;
	}
	private void backtrack(int[] nums, List<List<Integer>> result, List<Integer> current, boolean[] used) {
		if (current.size() == nums.length) {
			result.add(new ArrayList<>(current));
			return;
		}
		for (int i = 0; i < nums.length; i++) {
			if (used[i]) {
			continue;
			}
			used[i] = true;
			current.add(nums[i]);
			System.out.println(nums[i] + ", " + result + ", " + current + ", " + used[i]);
			backtrack(nums, result, current, used);
			used[i] = false;
			current.remove(current.size() - 1);
		}
	}
	public static void main(String[] args) {
		Permutations solution = new Permutations();
		int[] nums = { 1, 2, 3 };
		List<List<Integer>> permutations = solution.permute(nums);

		// Print the permutations
		for (List<Integer> permutation : permutations) {
			System.out.println(permutation);
		}
	}
}
