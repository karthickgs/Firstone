package String;

public class RotationalStrings {

	
	
	public void addrotation(String s) {
		String temp[]= {};
		String appendstr ="";
		StringBuilder result= new StringBuilder();
		for(int i=0;i<s.length();i++) {
			temp= s.split("\\.");
		}
		
		for(int j=0;j<temp.length;j++) {
			appendstr =temp[temp.length-(j+1)];
			result = result.append(appendstr);
			if(j!=temp.length-1) {
			result.append('.');
			}
			
		}
		System.out.println(result.toString());
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RotationalStrings rs = new RotationalStrings();
		rs.addrotation("Kar.thi.ck");

	}

}
