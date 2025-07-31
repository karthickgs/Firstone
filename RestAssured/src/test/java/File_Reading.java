import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class File_Reading {
	
	@Override
	public String toString() {
		return "File_Reading [name=" + name + ", id=" + id + ", age=" + age + ", email=" + email + "]";
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	private String name;
	private int id;
	private int age;
	private String email;
	

	public static void main(String[] args) throws StreamReadException, DatabindException, IOException {
		// TODO Auto-generated method stub
		File f = new File("C:\\Users\\k1027\\Documents\\Mobile\\Myfile.json");
		ObjectMapper map = new ObjectMapper();
		File_Reading val=map.readValue(f, File_Reading.class);
		System.out.println(val);
	}

}
