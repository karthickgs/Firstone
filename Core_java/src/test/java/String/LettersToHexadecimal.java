package String;

public class LettersToHexadecimal {

	public void addhdeci(String hexs) {

		StringBuilder sb = new StringBuilder();

		char com = hexs.charAt(0);
		int count = 0;

		for (int i = 0; i < hexs.length(); i++) {

			if (com == hexs.charAt(i)) {

				count++;

			}

			else {
				sb.append(com).append(count);
				com = hexs.charAt(i);
				count = 1;

			}

		}
		sb = sb.append(com).append(count);

		System.out.println(sb.toString());

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		LettersToHexadecimal lth = new LettersToHexadecimal();
		lth.addhdeci("aaaabbbcc");
	}

}
