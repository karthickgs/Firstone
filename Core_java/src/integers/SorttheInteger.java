package integers;

public class SorttheInteger {

	public void add(int [] a) {
		
		for(int i=0;i<a.length-1;i++) {
			int temp=0;
			for(int k=i;k<a.length-1;k++) {
			if(a[i]>a[k+1]) {
				temp = a[i];
				a[i] = a[k+1];
				a[k+1] = temp;
			}
		}}
		for(int j:a) {
			System.out.println(j);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SorttheInteger sw = new SorttheInteger();
	  int [] x = {8,1,6,12,43,3,8,90};
	  sw.add(x);
	}

}
