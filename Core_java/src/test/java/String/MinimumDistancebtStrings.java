package String;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinimumDistancebtStrings {
	

	
	/**
	 * @param s
	 * @param word1
	 * @param word2
	 * @return
	 */
	public static int shortdistance(List<String> s, String word1, String word2) {
		int finali = 0;
		
		Optional<String> findex = s.stream().map(String::strip).filter(i->i.length()>4).findFirst();
		
		

		

		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> s1 = List.of("geeks", "for", "geeks", "contribute",  "practice");
		int indexdiff=MinimumDistancebtStrings.shortdistance(s1, "geeks", "practice");
		System.out.println(indexdiff);
		
	}

}
