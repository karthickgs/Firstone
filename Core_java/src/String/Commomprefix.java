package String;

public class Commomprefix {

	// Function to find the longest common prefix
	public static String longestCommonPrefix(String[] strs) {
		// If the array is empty, return an empty string
		if (strs == null || strs.length == 0) {
			return "";
		}

		// Start with the first string as the prefix
		String prefix = strs[0];
		String s1 = "Karthick";

		// Loop through the rest of the strings
		for (int i = 1; i < strs.length; i++) {

			// Compare the current prefix with the i-th string
			//System.out.println(strs[i].indexOf(prefix));
			
			while (strs[i].indexOf(prefix) != 0) {
				// Shorten the prefix by one character

				prefix = prefix.substring(0, prefix.length() - 1);

				// If the prefix is empty, no common prefix exists
				if (prefix.isEmpty()) {
					return "";
				}
			}
		}

		return prefix;
	}

	public void longestprefixtwo(String source[]) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < source[0].length(); i++) {

			char res = source[0].charAt(i);
			for (int j = 1; j < source.length; j++) {

				if (i >= source[j].length() || source[j].charAt(i) != res) {
					System.out.println(sb.toString());
					return;
				}
			}

			sb.append(res);
		}

	}

	public void longestprefixthree(String source[]) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < source[0].length(); i++) {
			char res = source[0].charAt(i);
			for (int j = 1; j < source.length; j++) {
				if (i >= source[j].length() || source[j].charAt(i) != res) {
					System.out.println(sb.toString());
					return;
				}
			}
			sb.append(res);
		}
		System.out.println(sb.toString());
	}

	public static void main(String[] args) {

		Commomprefix cm = new Commomprefix();
		String[] strs5 = { "dog", "doj", "doob" };
		cm.longestprefixtwo(strs5);
		cm.longestprefixthree(strs5);
		// String[] strs1 = {"ka", "flow", "flight"};
//	        System.out.println("Longest common prefix: " + longestCommonPrefix(strs1));
//
//	        // Test case 2
//	        String[] strs2 = {"dog", "racecar", "car"};
//	        System.out.println("Longest common prefix: " + longestCommonPrefix(strs2));
//
//	        // Test case 3: Empty array
//	        String[] strs3 = {};
//	        System.out.println("Longest common prefix: " + longestCommonPrefix(strs3));
//
//	        // Test case 4: Single string
//	        String[] strs4 = {"apple"};
//	        System.out.println("Longest common prefix: " + longestCommonPrefix(strs4));

//	        System.out.println("Longest common prefix: " + longestprefixtwo(strs2));
	}

}
