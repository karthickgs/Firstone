package oops;

public class Getters {
	
	public  Setters_encap setters;
	
	public Getters (Setters_encap setters) {
		
		this.setters = setters;
	}
	
	public String getname() {
		return setters.getname();
		
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Setters_encap se = new Setters_encap("Alice");
		Getters get = new Getters(se);
		String nm =get.getname();
		System.out.println(nm);
	}

}
