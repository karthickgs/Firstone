import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.List;
import org.testng.annotations.DataProvider;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

@JsonIgnoreProperties(ignoreUnknown = true)
class mapper {
	
	@JsonCreator
	public mapper(@JsonProperty("Identifier") int identifier,@JsonProperty("firstName") String firstname,
			@JsonProperty("lastName") String lastname,@JsonProperty("age") int age,
			@JsonProperty("gender") String gender,@JsonProperty("email") String email,
			@JsonProperty("phone") String phone) {
		id =identifier;
		this.firstname=firstname;
		this.lastname=lastname;
		this.age =age;
		this.gender=gender;
		this.email=email;
		this.phone=phone;
	}
	
	private int id;
	private String firstname;
	private String lastname;
	private int age;
	private String gender;
	private String email;
	private String phone;
	
	public int getId() {
		return id;
	}


	@Override
	public String toString() {
		return "mapper [getId()=" + getId() + ", getFirstname()=" + getFirstname() + ", getLastname()=" + getLastname()
				+ ", getAge()=" + getAge() + ", getGender()=" + getGender() + ", getEmail()=" + getEmail()
				+ ", getPhone()=" + getPhone() + "]";
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	
}

public class Objectmapper {

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		String res = 	"{"+ "\"identifier\": 1,\r\n"
				+ "      \"firstName\": \"Emily\",\r\n"
				+ "      \"lastName\": \"Johnson\",\r\n"
				+ "      \"maidenName\": \"Smith\",\r\n"
				+ "      \"age\": 28,\r\n"
				+ "      \"gender\": \"female\",\r\n"
				+ "      \"email\": \"emily.johnson@x.dummyjson.com\",\r\n"
				+ "      \"phone\": \"+81 965-431-3024\""+"}";
		ObjectMapper obj = new ObjectMapper();
		mapper print =obj.readValue(res,  mapper.class);
		System.out.println(print);
		mapper test = new mapper(21, "Karthick", "Sudhakar", 35, "Male", "test@gk.in", "8454654655");
//		Objectmapper test1 = new Objectmapper();
//		test.setAge(21);
//		test.setEmail("test@gk.com");
		String val=obj.writeValueAsString(test);
		System.out.println(val);
		
	}

}
