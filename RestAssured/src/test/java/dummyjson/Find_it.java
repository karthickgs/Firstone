package dummyjson;

import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;

import org.apache.http.HttpRequest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Find_it {
	
	public void addrequest() {
		
		RequestSpecification reqsepc = new RequestSpecBuilder().setBaseUri("https://dummyjson.com").setContentType("application/json").build();
		
		RestAssured.filters(new RequestLoggingFilter(LogDetail.HEADERS), new ResponseLoggingFilter(LogDetail.HEADERS));
		
        String  responadduser = given().spec(reqsepc).body(Dummy_payload.addPayload()).log().all()
								.when().post("/users/add"). then().extract().response().asString();
        JsonPath js = new JsonPath(responadduser);
        String hair = js.getString("users.find{it.id==209}.gender");
        System.out.println(hair);
	
	}
		public void adduser() {
		
		RestAssured.baseURI  = "https://dummyjson.com";
		RestAssured.basePath = "/users";
		Response res = given().contentType("application/json").log().all().when().get().then().log().all().extract().response();
		JsonPath jpath = new JsonPath(res.asString());
		Double lat = jpath.getDouble("users.find{it.id==1 }.address.coordinates.lat");
		System.out.println(lat);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Find_it f = new Find_it();
		
		f.addrequest();
		f.adduser();
		
	}

}
