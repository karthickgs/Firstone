package oops;

import java.io.*;

public class TryCatch {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		try
		
		(FileInputStream fis = new FileInputStream(new File("C:\\Users\\k1027\\Documents\\mobile login.txt"));)
		{
			while(fis.read()!=-1) {
				
				byte[] red = fis.readAllBytes();
				String read = new String(red);
				System.out.println(read);
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IOException("File not found "+e.getMessage());
		} 
				
	}
}


