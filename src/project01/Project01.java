package project01;

import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
 * @Sawyer Cross
 */
public class Project01 {

    static double maxDistance = 0;
    static String maxLocation = "";
    static String maxZip = "";
    static String maxCity = "";

    public static void main(String[] args) throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser(); //file selection
        int openFiledialog = chooser.showOpenDialog(null);
        while (openFiledialog == JFileChooser.APPROVE_OPTION
        && !chooser.getSelectedFile().getName().endsWith(".txt")) 
        {
        JOptionPane.showMessageDialog(null, "The file "
        + chooser.getSelectedFile() + " is a .txt source file.",
        "Open Error", JOptionPane.ERROR_MESSAGE);
        openFiledialog = chooser.showOpenDialog(null);
        }
        File inputFile = chooser.getSelectedFile();
        Scanner readFile = new Scanner(inputFile);
        Scanner userinput = new Scanner(System.in); //scanner to get info from user

        giveIntro(); //intro to gather input from user
        System.out.print("What zip code are you interested in? ");
        String zipcode = userinput.nextLine();
        
        while (validateZip(zipcode) != true) { //while loop to run check to validate zipCode, checks is num, length and not null
        System.out.print("What zip code are you interested in? ");
        zipcode = userinput.nextLine();
        }
        System.out.print("And what proximity (in miles)?: ");
        double miles = userinput.nextDouble();

        String cords = find(zipcode, readFile);
        readFile = new Scanner(inputFile);
        showMatches(cords, readFile, miles);

    }

    public static void giveIntro() { //method to just present intro text to user
        System.out.println("Welcome to the zip code database.\n"
                + "Give me a 5-digit zip code and a\n"
                + "proximity and I'll tell you where\n"
                + "that zip code is located along\n"
                + "with a list of other zip codes\n"
                + "within the given proximity.\n");
    }

    public static boolean isaNumber(String number){
        try{
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException msg){
            return false;
        }
    }

    public static boolean validateZip(String zipCode){
        if (isaNumber(zipCode) == false){
            System.out.println("Must be a number of length 5. ");
            return false;
        }
        if(zipCode.length() != 5){
            System.out.println("The length of a zip MUST be 5. ");
            return false;
        }
        else{
            return true;
        }
    }

    public static String find(String target, Scanner input) { //method to find zip and then return the lat/long of that zip
        String latlong = "0,0";
        while (input.hasNext()) { //while loop that stores each line as a variable to allow comparison of zip
            int zip = input.nextInt();
            input.nextLine();
            String location = input.nextLine();
            String cords = input.nextLine();

            if (Integer.parseInt(target) == zip) {
                latlong = cords;
                System.out.println("\n" + zip + ": " + location);
                break;
            }
        }
        return latlong;
    }

    public static void showMatches(String targetCoordinates, Scanner input, double miles) throws FileNotFoundException { //method to all matches within the distance and return that list while outputing to a file
        PrintWriter outputFile = new PrintWriter("ZipCodeOutput.txt");

        String[][] fulllist = new String[43191][3];
        while (input.hasNext()) {
            for (int x = 0; x < fulllist.length; x++) { //for loop to iterate through each line of file, while assigning values to 2d array
                for (int y = 0; y <= 2; y++) {
                    String line = input.nextLine();
                    fulllist[x][y] = line;
                }
            }
        }
        String[] cords = new String[2]; //split cords into usable lat/long for distance method
        String[] targetCords = new String[2]; //split target cords into usable lat/long for distance
        targetCords = targetCoordinates.split(" ");
        double lat1 = Double.parseDouble(targetCords[0]);
        double long1 = Double.parseDouble(targetCords[1]);
        System.out.println("zip codes within " + miles + " miles:");
        outputFile.println("zip codes within " + miles + " miles:");

        for (int x = 0; x < fulllist.length; x++) { //for loop to split lat and long of all locations
            for (int y = 2; y < 3; y++) {
                cords = fulllist[x][y].split(" ");
                double lat2 = Double.parseDouble(cords[0]);
                //System.out.println(lat2);
                double long2 = Double.parseDouble(cords[1]);
                //System.out.println(long2);
                double distance = distance(lat1, long1, lat2, long2);
                String zip = fulllist[x][0]; //get values from same row of x
                String city = fulllist[x][1];
                if (distance < miles) { //if to check distance is lower than miles then print to user/file
                    System.out.printf("%10s %s %.2f ", zip, city, distance);
                    System.out.println();
                    outputFile.printf("%10s %s %.2f ", zip, city, distance);
                    outputFile.println();
                    maxLocation = findMaxDistance(zip, city, distance);
                }
            }
        }
        System.out.println();
        System.out.println("The farthest location is: " + maxLocation);
        outputFile.println();
        outputFile.print("The farthest locaiton is: " + maxLocation);
        outputFile.close();
    }

    public static double distance(double lat1, double long1, double lat2, double long2) { //method to convert lats and longs of target cords and cords within the files to usable distances
        double RADIUS = 3956.6;
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        long1 = Math.toRadians(long1);
        long2 = Math.toRadians(long2);
        double distanceMiles = RADIUS * (Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(long1 - long2)));
        return distanceMiles;
    }

    public static String findMaxDistance(String zip, String city, double distance) { //method that compares distance to current max to find the farthest location
        if (distance > maxDistance) {
            maxDistance = distance;
            distance = Math.round(distance * 100); //to round distance
            distance = distance / 100;
            maxZip = zip;
            maxCity = city;
            maxLocation = zip + ", " + city + ", " + distance;
        }
        return maxLocation;
    }
}
