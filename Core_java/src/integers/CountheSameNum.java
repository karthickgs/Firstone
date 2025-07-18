package integers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CountheSameNum {
	
	
	int[] array = {2,1,4,5};
	int target = 9;

	  public void countAndSay(int  k) {
		  
		  int i=0;
		  int modulo=0;
		  List<Integer> ref = Arrays.stream(array)
                  .boxed()
                  .collect(Collectors.toList());
		  while(i < k) {
			  modulo = target%array[i];
			  ref.
			  
		  
		
	  }
	       	    
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
