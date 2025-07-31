package Groupid.RestAssured;

import io.restassured.RestAssured;
import junit.framework.Assert;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Login_oldpassword {
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://reqres.in/";
		given().log().all().body(request_payload.request_header()).header("Content-Type","application/json").
		when().post("/api/users")
		.then().assertThat().log().all().body("email",equalTo("michael.lawson@reqres.in"));

	}

}
