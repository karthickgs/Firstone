package file_handling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class InputStream {

	public static void frombyte(File file) throws IOException {

		FileInputStream fis = new FileInputStream(file);
		int readasString = 0;
		while ((readasString=fis.read())!=-1) {

			System.out.println((char)readasString);

		}
		
	}
	
	public static void tobyte (File file) throws IOException {
		
		OutputStream fos = new FileOutputStream(file);
		fos.write(("Hello world".getBytes()));
		fos.flush();
		System.out.println("Written successfully");
		
	}
	
	public static void main(String[] args) throws IOException {
		
		//File file = new File("C:\\Users\\k1027\\Documents\\Mobile\\File1.txt");
		//InputStream.frombyte(file);
		File file2 = new File("C:\\Users\\k1027\\Documents\\Mobile\\File2.txt");
		InputStream.tobyte(file2);
		
		
	}
}

	