package String;

public class NumofCharinString {

	public void stringrep(String s1) {

		char[] a = s1.toCharArray();
		for (int i = 0; i < a.length; i++) {

			a[i] = s1.charAt(i);
			int count = 0;
			boolean alreadyCounted = false;
			for (int k = 0; k < i; k++) {
				if (a[i] == a[k]) {
					alreadyCounted = true;
					break;
				}
			}
			if (alreadyCounted) {
				continue; // Skip if already counted
			}
			for (int j = 0; j < a.length; j++) {
				if (a[i] != a[j]) {
					continue;
				} else if (a[i] == a[j]) {
					count++;

				}

			}
			System.out.println("The given character " + a[i] + " has occured " + count + " times");
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NumofCharinString ncs = new NumofCharinString();
		ncs.stringrep("yquyru");
	}

}
