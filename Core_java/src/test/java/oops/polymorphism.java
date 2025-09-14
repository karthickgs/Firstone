package oops;

public class polymorphism {
	
public void reverse(String name) {
		
		String a = "";
		char c =' ';
		String d = "";
		
		for(int i=0;i<name.length();i++) {
			
			d = name.substring(8, 18);
			int j =0;
			while(j<d.length())
			c = d.charAt(j);
			a=c+a;
			j++;
		}
		System.out.println(a);
	}
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		polymorphism poly = new polymorphism();
		poly.reverse("Welcome to Google FANG company");
	}

}
