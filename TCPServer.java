import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

/*
    File:               TCPServer.java
    Date:               20/03/19
    Author:             Ethan Cannon
    Student Number:     12051468
    Purpose: This file is the server side of the TCP application. It is designed to receive data sent to it
    from the client, save this to a text file, and every 2 seconds, convert the contents of the text file
    to a java serialized file by the name of memberlistObject
*/

public class TCPServer {

    public static void main(String args[]) {

        try {

            // setup server
            int serverPort = 1168;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            // begin timer
            int interval = 2000;
            Timer tm = new Timer();
            tm.schedule(new WriteToFile(), interval, interval);

            // begin listening
            while (true) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
            }

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}

class Connection extends Thread {

    // declare required variables
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private String fileMemberlist = "memberlist.txt";

    Connection(Socket aClientSocket) {

        try {

            // assign socket
            clientSocket = aClientSocket;

            // assign input and output streams
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            // run thread
            this.start();

        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {

        try {

            // read data sent from client
            String clientData = in.readUTF();

            // display output to command screen
            System.out.println("Received data from client");

            // save info to file
            FileWriter fr = new FileWriter(fileMemberlist, true);
            fr.write(clientData + System.lineSeparator());
            fr.close();

            // send acknowledgement back to client
            out.writeUTF("Data saved successfully");

        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {/*close failed*/}
        }

    }
}

class WriteToFile extends TimerTask {

    // declare buffered reader to read file
    private BufferedReader br;

    public void run() {

        // declare file to read from
        File fileMemberlist = new File("memberlist.txt");

        // check if file exists
        if (fileMemberlist.exists()) {

            // declare serializable file (only if this process is going to run)
            File serializeFile = new File("memberlistObject");

            try {

                // assign buffer reader
                br = new BufferedReader(new FileReader(fileMemberlist.getName()));

                // place holder for current line in file (looping)
                String strCurrentLine;

                // array list to itterate through to write data to file
                ArrayList<Member> memberArrayList = new ArrayList<Member>();

                // declare member object to fill info with
                Member m = null;

                // iterate through file - read until null (no more lines)
                while ((strCurrentLine = br.readLine()) != null) {

                    // declare object to use
                    m = new Member();

                    // declare delimiter (colon - :)
                    StringTokenizer tokenizer = new StringTokenizer(strCurrentLine, ":");

                    // iterate through current line, splitting by delimiter and build object
                    while (tokenizer.hasMoreTokens()) {
                        m.setFirstName(tokenizer.nextToken());
                        m.setLastName(tokenizer.nextToken());
                        m.setAddress(tokenizer.nextToken());
                        m.setPhoneNumber(tokenizer.nextToken());
                    }

                    // add created member to array list
                    memberArrayList.add(m);
                }

                // declare output stream to use to write serialized file
                FileOutputStream fos = new FileOutputStream(serializeFile, false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                // itterate through array list to write to serialized file
                for (Member aMemberArrayList : memberArrayList) {
                    oos.writeObject(aMemberArrayList);
                }

                // close output stream once done
                oos.close();

            } catch (IOException ex) {

                // print any errors
                ex.printStackTrace();

            } finally {
                try {
                    // if buffered reader was used, close it
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
