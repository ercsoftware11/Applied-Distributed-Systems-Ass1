import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/*
    File:               UDPClient.java
    Date:               20/03/19
    Author:             Ethan Cannon
    Student Number:     12051468
    Purpose: This file will serve as the client for the UDP connection, where it can be used to send the
    memberlistObject command to the server in order to receive a reply from the server of the client information
*/

public class UDPClient {

    public static void main(String args[]) {

        //The first argument is the message/command to send to the server.

        DatagramSocket aSocket = null;
        try {

            //Create a UDP socket
            aSocket = new DatagramSocket();

            //Prepare the message to send to the server
            byte[] m = args[0].getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");

            //Agreed port
            int serverPort = 2268;

            //Create a UDP datagram
            DatagramPacket request =
                    new DatagramPacket(m, args[0].length(), aHost, serverPort);

            //Send the request
            aSocket.send(request);

            //Prepare a buffer to receive the reply from the server
            byte[] buffer = new byte[1000];

            //Waiting for reply
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);

            //Display the reply
            String response = new String(reply.getData(), 0, reply.getLength());
            System.out.print("Server Response: \n" + response);

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (aSocket != null) aSocket.close();
        }
    }
}
