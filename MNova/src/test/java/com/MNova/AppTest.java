package com.MNova;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
public static void find() {
	String propno = "Proposal Saved & Generated Successfully- 421010/88/26/P/0000104";
	String getProp = "\\d{6}[\\/]\\d{2}[\\/]\\d{2}[\\/][P][\\/]\\d{7}";
	Pattern pat = Pattern.compile(getProp);
	Matcher match = pat.matcher(propno);
	String prop = "";
	if(match.find()) {
		 prop = match.group();
	}
	System.out.println(prop);
	
}	 

public static void main(String args[]) {
	AppTest.find();
}
}

