package arraysList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Threesum {
	
	public List<List<Integer>> threesum(int[]nums, int target) {
		
		List<List<Integer>> addi = new ArrayList<List<Integer>>();
		Arrays.sort(nums);
		if (nums.length<3) {
			
			return addi;
		}
		for(int i=0;i<nums.length-2;i++) {
			
			if(i>0&&nums[i]==nums[i-1]) {
				continue;
			}
			int left=i+1;
			int right = nums.length-1;
			while(left<right) {
				int sum = nums[left]+nums[right]+nums[i];
				if(sum==target) {
					addi.add(Arrays.asList(nums[i],nums[left],nums[right]));
					while(left<right&&nums[left]==nums[left+1]) {
					left++;
				}
					while(left<right&&nums[right]==nums[right-1]) {
					right--;
				}
					left++;
					right--;
				}
				
				else if(sum<target) {
					left++;
				}
				else
				{
					right--;
				}
				
				}
			}
		return addi;
		}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Threesum th = new Threesum();
		int[] a= {0,1,2,-1,3,4,-5};
		System.out.println(th.threesum(a, 2));
	}

}
