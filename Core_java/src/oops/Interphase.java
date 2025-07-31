package oops;

interface vehicle{
	
	public static final String temp = "B";
	void ShiftGear(String a);
	void FuelIndicator(int b);
	public static void Engine_checkup(String x){
		if(temp.equalsIgnoreCase("A")) {
			System.out.println("Engine health is good");
		}
	}
}


public class Interphase implements vehicle{
	
	String x;
	int y;
	
	public void ShiftGear(String a) {
		System.out.println("Do not shift the gear till you accelerate "+ a+" gear(s)");
	}
	public void FuelIndicator(int b) {
		System.out.println("Indicates the problems at "+b+" km");
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Interphase in = new Interphase();
		in.FuelIndicator(100);
		in.ShiftGear("Four");
		vehicle.Engine_checkup("a");
		
	
		
	
	}
	
	
	

}
