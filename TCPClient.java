import java.net.*;
import java.io.*;
import java.util.Scanner;

/*
    File:               TCPClient.java
    Date:               20/03/19
    Author:             Ethan Cannon
    Student Number:     12051468
    Purpose: This file is the client side of the TCP oriented connection. It is designed to process
    data entered by the client, send this to the server, where the server wil confirm whether or not
    this data has been saved
*/

public class TCPClient {

    public static void main(String args[]) {

        // declare socket here so can be closed in finally statement
        Socket s = null;

        while(true) {
            try {

                // declare port to use
                int serverPort = 1168;

                // assign socket
                s = new Socket("localhost", serverPort);

                // declare scanner to read user response
                Scanner inObj = new Scanner(System.in);

                // declare input and output streams
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                // query for user data and read response

                // reading member number is unnecessary in this assignment as it is not written to any files
                // or utilized anywhere else
                boolean isDataOkay = false;
                String memberNo;
                String firstName;
                String lastName;
                String address;
                String phone;

                // do entry once before checking
                // ask for data, check, repeat if necessary
                do {

                    System.out.print("Enter Details for Member Number: ");
                    memberNo = inObj.nextLine();

                    System.out.print("Enter your first name: ");
                    firstName = inObj.nextLine();

                    System.out.print("Enter your last name: ");
                    lastName = inObj.nextLine();

                    System.out.print("Enter your address: ");
                    address = inObj.nextLine();

                    System.out.print("Enter your phone number: ");
                    phone = inObj.nextLine();


                    // DATA CHECKING
                    // make sure all entries have data
                    if (!memberNo.equals("") && !firstName.equals("") && !lastName.equals("") &&
                            !address.equals("") && !phone.equals("")) {

                        // make sure phone number is numbers and long enough
                        boolean isPhoneOkay = false;

                        try {

                            long phoneNo = Long.parseLong(phone);
                            int length = String.valueOf(phoneNo).length();

                            isPhoneOkay = length == 9;

                        } catch (NumberFormatException e) {
                            isPhoneOkay = false;
                        }

                        // make sure name is only letters
                        boolean isNamesOkay = false;
                        boolean isFirstNameAllLetters = firstName.chars().allMatch(Character::isLetter);
                        boolean isLastNameAllLetters = lastName.chars().allMatch(Character::isLetter);

                        // check both are okay
                        if (isFirstNameAllLetters && isLastNameAllLetters) {
                            isNamesOkay = true;
                        }

                        // check phone and names checking were okay
                        isDataOkay = isPhoneOkay && isNamesOkay;

                    } else {
                        isDataOkay = false;
                    }

                    // data is not okay, print out error statement
                    if (!isDataOkay) {
                        System.out.println("There was an issue with the data entry, please try again");
                    }

                    // END DATA CHECKING
                } while (!isDataOkay);

                // will not reach this part until data is entered correctly
                // format data to send
                String formattedData = firstName + ":" + lastName + ":" + address + ":" + phone;

                // log sending to server
                System.out.println("Sending Data to Server...................................");

                // print the formatted data which will be sent to server
                System.out.println(formattedData);

                // send data
                out.writeUTF(formattedData);

                // receive reply from server
                String result = in.readUTF();
                System.out.println("Server Response: " + result);
                System.out.println("---------------------------------------------------------");

            // error catching
            } catch (UnknownHostException e) {
                System.out.println("Socket:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("readline:" + e.getMessage() + "\n");
                e.printStackTrace();
            } finally {

                // if socket is not null (socket has been assigned)
                if (s != null) try {
                    // close the socket
                    s.close();
                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
                }
            }
        }
    }
}
