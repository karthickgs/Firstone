package String;

import java.util.ArrayList;
import java.util.List;

public class Permutation {
	
	public void permeate(String[] a) {
		List<String> pr = new ArrayList<String>();
		for(int i=0;i<=a.length-1;i++) {
			for(int j=0;j<=a.length-1;j++) {
				String c=a[i]+a[j];
				if(!pr.contains(c)) {
					System.out.println(c);
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Permutation pr = new Permutation();
		String [] ab = {"zoo","Keep","circus","gate"};
		pr.permeate(ab);
	}

}
