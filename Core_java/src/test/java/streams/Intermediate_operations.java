package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Intermediate_operations {
	
	String name;
	int age;
	String area;
	int salary;
	
	
	public String getname() {
		return name;
		
	}
	public int getage() {
		return age;
		
	}
	public String getarea() {
		return area;
		
	}
	public int getsalary() {
		return salary;
		
	}
	
	Intermediate_operations(String name,int age,String area, int salary){
		
		this.name =name;
		this.age =age;
		this.area=area;
		this.salary=salary;
		 
	}

	public void addop() {
		List<Intermediate_operations> usert = Arrays.asList(new Intermediate_operations("Karthick",28,"Japan",5000),
				new Intermediate_operations("Sudhakar",28,"Egypt",50000),
				new Intermediate_operations("Harini",25,"Purto Rico",1000000),
				new Intermediate_operations("Sudha",45,"Japan",300000),
				new Intermediate_operations("Hritik",25,"Purto Rico",1000000));
		
		List<Integer>ageof =usert.stream().map(Intermediate_operations::getsalary).filter(s->s>300000).collect(Collectors.toList());
		
		List<String> names = usert.stream().map(Intermediate_operations::getname).filter(s->s.startsWith("H")).collect(Collectors.toList());
		
		List<String> greet = List.of("Good morning","Good night");
		
		List<String> numbers1= greet.stream().map(k->k.split(" ")).map(Arrays::toString).collect(Collectors.toList());
		
		List<Integer> add = List.of(1,2,3,4,5,6,7,8);
		
//		List<Integer> add1 = add.stream().peek(a->logger.debug(a)).collect(Collectors.toList());
		
		System.out.println(ageof.toString());
		System.out.println(names.toString()); 
		System.out.println(numbers1.toString());
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Intermediate_operations iop = new Intermediate_operations("Sudha",23,"Banglore",90000);
		iop.addop();
		
	}

}
