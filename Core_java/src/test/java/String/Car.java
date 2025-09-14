package String;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Car {

    int nowheels;
    int alloy;
    int speed;

    public void wheelType(int numbers) {
        if (numbers < 4) {
            System.out.println("Oops, the wheel got punctured");
        } else {
            nowheels = numbers;
            System.out.println("The number of wheels: " + nowheels);
        }
    }

    public void initialspeed() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter initial speed: ");
        String input = br.readLine(); // Read full line
        speed = Integer.parseInt(input); // Convert the input to an integer
        System.out.println("Total Speed: " + speed);
    }

    public void speed(int customisedspeed) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the gear (1-5): ");
        String gearInput = br.readLine();
        int gears = Integer.parseInt(gearInput); // Convert gear input to an integer
        
        int totalspeed = customisedspeed + speed;
        int reducedspeed = customisedspeed-speed ;

        System.out.print("Enter Inc or Dec to adjust the speed: ");
        String Id = br.readLine();

        switch (gears) {
            case 1:
                if (customisedspeed >= 0 && customisedspeed <= 20) {
                    System.out.println("Gear 1 selected");
                    adjustSpeed(Id, totalspeed, reducedspeed);
                } else {
                    System.out.println("Invalid speed range for Gear 1");
                }
                break;

            case 2:
                if (customisedspeed >= 21 && customisedspeed <= 40) {
                    System.out.println("Gear 2 selected");
                    adjustSpeed(Id, totalspeed, reducedspeed);
                } else {
                    System.out.println("Invalid speed range for Gear 2");
                }
                break;

            case 3:
                if (customisedspeed >= 41 && customisedspeed <= 60) {
                    System.out.println("Gear 3 selected");
                    adjustSpeed(Id, totalspeed, reducedspeed);
                } else {
                    System.out.println("Invalid speed range for Gear 3");
                }
                break;

            case 4:
                if (customisedspeed >= 61 && customisedspeed <= 80) {
                    System.out.println("Gear 4 selected");
                    adjustSpeed(Id, totalspeed, reducedspeed);
                } else {
                    System.out.println("Invalid speed range for Gear 4");
                }
                break;

            case 5:
                if (customisedspeed >= 81 && customisedspeed <= 150) {
                    System.out.println("Gear 5 selected");
                    adjustSpeed(Id, totalspeed, reducedspeed);
                } else {
                    System.out.println("Invalid speed range for Gear 5");
                }
                break;

            default:
                System.out.println("Alert: Your operation is denied");
        }
    }

    private void adjustSpeed(String Id, int totalspeed, int reducedspeed) {
        if (Id.equals("Inc")) {
            System.out.println("Total Speed: " + totalspeed);
        } else if (Id.equals("Dec")) {
            System.out.println("Total Speed: " + reducedspeed);
        } else {
            System.out.println("Invalid speed adjustment command");
        }
    }
    public class Aero  extends Car {

	    public static void main(String[] args) throws IOException {
	        Car obj =  new Car();
	        obj.wheelType(5);
	        obj.initialspeed();
	        obj.speed(100);
	    }
	}
}


