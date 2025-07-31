package com.marine;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class marine_loadcombo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI= "http://novaapiuat.shriramgi.com/";
		String res = given().log().all().body(marine_LOVlist.obj()).header("Content-Type","application/json")
					   .when().post("UATNovaWS/NOVAServices/MarineMobile.svc/RestService/MarineMobileSMILov")
					   .then().log().all().extract().response().asPrettyString();
		
		JsonPath js = new JsonPath(res);
		JsonPath js2 = new JsonPath(marine_LOVlist.obj());
		String req = js.getString("SMIValues.Desc");
		String [] req2 = req.split(";");
		for(int i=0;i<req2.length;i++) {
			String arr =req2[1];
		}
 		System.out.println(req);
 		System.out.println(js.getInt("SMIValues.size()"));
 		String elem = js.get("SMIValues[124].Desc");
 		System.out.println(elem); 
	}

}
