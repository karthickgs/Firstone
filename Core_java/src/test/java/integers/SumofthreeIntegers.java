package integers;

import java.util.*;

public class SumofthreeIntegers {

	// Using two pointers technique

	public List<List<Integer>> add3int(int[] arr, int sumfinal) {

		
		Arrays.sort(arr);
		List<List<Integer>> list = new ArrayList<>();
		int arrlength = arr.length;
		for (int i = 0; i < arrlength - 2; i++) {
			int left = i+1;
			int right = arrlength - 1;
			int sum = 0;
			while (left < right) {
				sum = arr[left] + arr[right] + arr[i];
				if (sum == sumfinal) {

					list.add(Arrays.asList(arr[left], arr[right], arr[i]));

					while (left < right && arr[left] == arr[left + 1]) {
						left++;
					}
					while (left < right && arr[right] == arr[right - 1]) {
						right--;
					}
							left++;
		              right--;

				} else if (sum < sumfinal) {
					left++;
				}
				else {
					right--;
				}

			}
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SumofthreeIntegers soft = new SumofthreeIntegers();
		int [] x= {1,2,5,1,4,8,2,0};
		List<List<Integer>> res=soft.add3int(x, 6);
		System.out.println(res);
	}

}
