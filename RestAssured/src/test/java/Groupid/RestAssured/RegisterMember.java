package Groupid.RestAssured;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegisterMember {

	@Test(dataProvider = "dp")
	public void getdata(String mail,String pwsd) {
		RestAssured.baseURI = "https://reqres.in/";
		Response resp = given().header("Content-Type","application/json").log().all().body(request_payload.reqpay("michael.lawson@reqres.in","7409789s")).
					  when().post("/api/register").
					  then().log().all().assertThat().statusLine("HTTP/1.1 200 OK").extract().response();
		
		JsonPath js = resp.jsonPath();	  
		String email = js.get("token");
		System.out.println(email);
		
	}
	@DataProvider(name = "dp")
	public Object[][] getprov() {
		
		Object obj =new Object[][] {{"rachel.howell@reqres.in","987uih"},{"george.edwards@reqres.in","8942yh"},{"byron.fields@reqres.in","8904tjg"}};
		return (Object[][]) obj;
	}
	
}
