package arraysList;

public class example {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String [] charac = {"Save","the","world"};
		String temp =charac[0];
		for(int i=1;i<charac.length;i++) {
			if(charac[i].compareTo(temp) <0 ) {
				temp = charac[i];
				System.out.println(temp);
			}
		}

	}
	

}
