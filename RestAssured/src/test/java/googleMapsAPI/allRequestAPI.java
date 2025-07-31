package googleMapsAPI;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.*;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import googleMapsAPI.Googlemapssetters.*;

@JsonInclude(value = Include.NON_EMPTY)
public class allRequestAPI {
    // Define the RequestSpecification for the request
//	PrintStream log = new PrintStream(new FileOutputStream("Log.txt"));
    RequestSpecification req = new RequestSpecBuilder()
            .setBaseUri("https://rahulshettyacademy.com")
//    		.addFilter(RequestLoggingFilter.logRequestTo(log))
            .setContentType("application/json")
            .build();

    // Define the ResponseSpecification to expect a status code of 201
    ResponseSpecification res = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .build();

    // Method to make API request
    public void addAPI() {
        // Create the request object for the Google Maps API
        Googlemapssetters map = new Googlemapssetters();
        Location location = new Location();
        location.setLatitude(23.763);
        location.setLongi(31.895);
        map.setLocation(location);
        map.setAccuracy(57);
        map.setLangauge("Tamil");
        map.setAddress("119, Misa Washington street, kk nagar");
        map.setName("Abimanyau");
        map.setPhone("6872658285");
        List<String> types = new ArrayList<>();
        types.add("Fridge");
        types.add("Geyser");
        map.setTypes(types);
        map.setWebsite("www.jaslogo.com");

        // Sending the API request
        Response resp = given()
                .spec(req)
                .queryParam("key", "qaclick123")
                .header("Content-type", "application/json")
                .body(map)
                .log().all()
            .when()
                .post("/maps/api/place/add/json")
            .then()
                .log().all()
                .extract()
                .response();
       
        
        String name = resp.jsonPath().get("name");
        

        // Handling response
        if (resp.getStatusCode() == 200 || resp.getStatusCode() == 201) {
        	System.out.println(resp.asString());
        	
        	
        	
            String scope = resp.jsonPath().getString("scope"); // Access "scope" correctly
            String header = resp.getHeader("Content-Type");
            System.out.println("Response Header: " + header);
            System.out.println("Scope: " + scope);
        	

        } else {
            System.out.println("Error: Received unexpected status code: " + resp.getStatusCode());
        }
    }

    // Main method to execute the API request
    public static void main(String[] args) {
        allRequestAPI ap = new allRequestAPI();
        ap.addAPI();
    }
}
