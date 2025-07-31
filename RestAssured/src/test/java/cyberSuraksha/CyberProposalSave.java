package cyberSuraksha;

import org.apache.http.HttpStatus;
import static io.restassured.RestAssured.*;

import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.internal.http.HTTPBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CyberProposalSave {
	
	RequestSpecification cyberquesres;
	ResponseSpecification cyberquesreq;
	
public CyberProposalSave() {
	
}	

	public ResponseSpecification prerequisite() {
		
		RequestSpecification cyberquesres = new RequestSpecBuilder().setBaseUri("http://novaapiuat.shriramgi.com/uatNovaWS/novaServices/CyberSuraksha.svc/RestService").setContentType("application/json").
			addHeader("checksum","4e965cda30f6e28f7b8c56ce7c3f1259e120f3b1decbefc54ceb861e3bc0326b").addHeader("userid","7cc2e03ee80900fd1bd6a27f31f1bb1e23d3317987f3df0a2c2122794649b747")
			.addHeader("password","f5ef5c34863428a5a58085b8e24c57b36378cb43a975359e06e99de73a9a6f3f").addHeader("token","SGI-NOVA").build();
		
		ResponseSpecification cyberquesreq = new ResponseSpecBuilder().expectStatusCode(HttpStatus.SC_ACCEPTED).build();
		
		return cyberquesreq;
	}
	
	public void populate_service() {
		
		String po = given().spec(cyberquesres).when().post("/Cyber_FetchDetails").then().log().all().and().extract().asString()
		ObjectMapper om = new ObjectMapper();
		om.readValue(po,);
	}
}
