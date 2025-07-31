package googleMapsAPI;

import java.util.List;

public class Googlemapssetters {
	
	Location location;
	int accuracy;
	String name;
	String phone;
	String address;
	List<String> types;
	String website;
	String langauge;
	

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public Location getLocation() {
		return location;
	}

	public void setLocation(Location d) {
		this.location = d;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getLangauge() {
		return langauge;
	}

	public void setLangauge(String langauge) {
		this.langauge = langauge;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
