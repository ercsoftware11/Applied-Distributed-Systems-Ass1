import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/*
    File:               UDPServer.java
    Date:               20/03/19
    Author:             Ethan Cannon
    Student Number:     12051468
    Purpose: This file will serve as the server for the UDPServer, where it receives a command from the client
    (the manager) where the server will read the serialized file, and return the results to the client
*/

public class UDPServer {

    // declare required variables here so both methods can access (assign in main though)
    private static DatagramSocket aSocket = null;
    private static File serializeFile = new File("memberlistObject");
    private static DatagramPacket request;

    public static void main(String args[]) {

        while (true) {
            try {

                // create socket at agreed port
                aSocket = new DatagramSocket(2268);

                // buffer to receive data
                byte[] buffer = new byte[1000];

                // declare clients request
                request = new DatagramPacket(buffer, buffer.length);

                // receive request from socket
                aSocket.receive(request);

                // convert request into string
                String clientRequest = new String(request.getData(), 0, request.getLength());

                // check if request is correct
                if (clientRequest.equals(serializeFile.getName())) {

                    // check file exists before trying to access
                    if (serializeFile.exists()) {

                        // declare input stream to read file
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serializeFile));

                        // declare placeholder object
                        Object obj;
                        StringBuilder stringBuilder = new StringBuilder();

                        try {

                            // declare format to display data (format can be reused)
                            String format = "|%1$-20s|%2$-20s|%3$-20s|%4$-20s\n";

                            // append headings
                            stringBuilder.append(String.format(format,
                                    "First Name",
                                    "Last Name",
                                    "Address",
                                    "Phone Number"));

                            // append footer of header
                            stringBuilder.append("\n========================================================\n");

                            // append a new line for extra spacing
                            stringBuilder.append("\n");

                            // loop will read file and will throw an exception when at end
                            // (null will not be at end of file, however end of file will throw exception as
                            // condition cannot be met. Use this to determine end of file
                            while ((obj = ois.readObject()) != null) {
                                if (obj instanceof Member) {

                                    // append objects in file to string builder in table format
                                    stringBuilder.append(String.format(format,
                                            ((Member) obj).getFirstName(),
                                            ((Member) obj).getLastName(),
                                            ((Member) obj).getAddress(),
                                            ((Member) obj).getPhoneNumber()
                                    ));
                                }
                            }
                        } catch (EOFException e) {
                            // do nothing - this is supposed to happen
                        } finally {

                            // append end of data footer
                            stringBuilder.append("\n========================================================\n");

                            sendDataToClient(stringBuilder.toString(),
                                    "Client Request: " + clientRequest);
                        }
                    } else {
                        sendDataToClient("File not found", "File not found");
                    }
                } else {
                    sendDataToClient("Not a valid command", "Invalid Command Given");
                }
            } catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (aSocket != null) aSocket.close();
            }
        }
    }

    private static void sendDataToClient(String data, String confirmationMsg) {

        try {

            // declare data to be sent back
            byte[] sendData = data.getBytes();

            // format data to datagram packet and use address and port from initial request host
            DatagramPacket reply = new DatagramPacket(sendData, sendData.length,
                    request.getAddress(), request.getPort());

            aSocket.send(reply);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // write result to output
            System.out.println(confirmationMsg);
        }
    }
}
