package String;

public class ReplaceChar {
	
	public String replaceCh(String res, int index,char appendch) {
		
		StringBuilder sb = new StringBuilder(res);
		sb.replace(index, index+1, String.valueOf(appendch));
		
		return sb.toString();
	}

	public static void main(String[] args) {
		
		ReplaceChar rc = new ReplaceChar();
		String rep=rc.replaceCh("Geeks Gor Geeks", 6, 'h');
		System.out.println(rep);
	}

}
