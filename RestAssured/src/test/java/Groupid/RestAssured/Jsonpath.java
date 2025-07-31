package Groupid.RestAssured;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class Jsonpath {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		 
		String name = "Karthick Sudhakar";		
		String [] revname = name.split("//s");
		String n = "";
		
		for(int i=revname.length;i<=0;i--) 
		{
			String m = n+revname;
			System.out.println(m);
		}
	}

	
}
