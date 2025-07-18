package Groupid.RestAssured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.*;
import java.awt.datatransfer.MimeTypeParseException;
import java.io.File;
import org.apache.http.HttpStatus;
import org.apache.http.entity.mime.MIME;
import org.testng.annotations.Test;

public class CreateLogin {
	
	@Test
	
	public void add_order() {
		
//		RestAssured.baseURI = "https://rahulshettyacademy.com";
//		String login=given().log().all().header("Content-type","application/json").body(CreateObjectsforOrder.req_createorder()).when().post("/api/ecom/auth/login").
//				then().log().all().assertThat().statusCode(200).extract().response().asString();
//		JsonPath path = new JsonPath(login);
//		String d =path.getString("userId");
//		String token = path.getString("token");
//		System.out.println(d);
		
		//Creating new order
		RequestSpecification req = new RequestSpecBuilder().setContentType("application/json").setBaseUri("https://rahulshettyacademy.com").build();
		ResponseSpecification res = new ResponseSpecBuilder().expectStatusCode(HttpStatus.SC_ACCEPTED).build();		
		String order =given().spec(req).body(CreateObjectsforOrder.req_createorder()).when().post("/api/ecom/auth/login").asString();		
		JsonPath js = new JsonPath(order);
		String userid = js.getString("userId");
		String token = js.getString("token");
		
	
		//Adding products into the cart
		
			given(Addproduct())
		
		         .spec(req).header("Authorization", token)
		         .header("Content-Type", "multipart/form-data")
				.param("productAddBy", userid)
				.param("productCategory", "Electronic Gadget")
				.param("productSubCategory", "Flash Drone")
				.param("productPrice", "15000")
				.param("productDescription", "Kambaro")
				.param("productfor", "All").multiPart("file",new File("D:/Visual Studio 2013/download.jpg"),"multipart/form-data")
				.when().post("/api/ecom/product/add-product").then().log().all().extract().response()
				.asPrettyString();
		
	}

	public RequestSpecification Addproduct() {
		// TODO Auto-generated method stub
		return new RequestSpecBuilder().build();
	}

	

}
