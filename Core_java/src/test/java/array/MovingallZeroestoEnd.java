package array;

public class MovingallZeroestoEnd {
	
	public static void addzeores(int [] arr) {
		
		int n =arr.length-1;
		int temp=0;
		 int count = 0;

        for (int i = 0; i < arr.length; i++) {

            // If the current element is non-zero
            if (arr[i] != 0) {

                // Swap the current element with the 0 at index 'count'
                temp = arr[i]; //1
                arr[i] = arr[count];//1
                arr[count] = temp; //1

                // Move 'count' pointer to the next position
                count++;
            }
        }
	}

	public static void main(String[] args) {
		
		MovingallZeroestoEnd mtz = new MovingallZeroestoEnd();
		int d[] = {1,0,1,0,0,0,1};
		addzeores(d);
		for(int i:d) {
			System.out.println(i);
		}

	}

}
