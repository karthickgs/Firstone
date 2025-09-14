package String;

public class brackets {

	public boolean isValid(String s) {
	
		int end = s.length()-1;
		String o = "{([";
		String p = "})]";
		for(int i=0;i<=o.length()-1;i++) {
			if(o.charAt(i)==s.charAt(0)&&p.charAt(i)==s.charAt(end)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		brackets kets = new brackets();
		Boolean s=kets.isValid("{}");
		System.out.println(s);
	}

}
