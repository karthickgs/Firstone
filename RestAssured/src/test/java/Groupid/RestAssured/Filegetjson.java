package Groupid.RestAssured;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.http.protocol.HTTP;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Filegetjson {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
			RestAssured.baseURI = "https://reqres.in/";
			Response resp = given().header("Content-Type", "application/json").log().all()
					.body(new String(
							Files.readAllBytes(Paths.get("C:\\Users\\k1027\\Documents\\Mobile\\usercreate_post.json"))))
					.when().post("/api/users").then().log().all().assertThat().statusCode(201).extract().response();
					

			JsonPath js = resp.jsonPath();
			String date = js.get("createdAt");
			System.out.println(date);

		}

	
}
