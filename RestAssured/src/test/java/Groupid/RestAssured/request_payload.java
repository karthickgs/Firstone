package Groupid.RestAssured;

public class request_payload {

	public static String request_header() {
		
		return "{\r\n"
				+ "    \"page\": 2,\r\n"
				+ "    \"per_page\": 6,\r\n"
				+ "    \"total\": 12,\r\n"
				+ "    \"total_pages\": 2,\r\n"
				+ "    \"data\": [\r\n"
				+ "        {\r\n"
				+ "            \"id\": 7,\r\n"
				+ "            \"email\": \"michael.lawson@reqres.in\",\r\n"
				+ "            \"first_name\": \"Michael\",\r\n"
				+ "            \"last_name\": \"Lawson\",\r\n"
				+ "            \"avatar\": \"https://reqres.in/img/faces/7-image.jpg\"\r\n"
				+ "        }";
	}

	public static String reqpay(String mail,String pswd) {
		String pay = "{\r\n"
				+ "    \"email\": \""+mail+"\",\r\n"
				+ "    \"password\": \""+pswd+"\"\r\n"
				+ "}";
		return pay;
	}

}
