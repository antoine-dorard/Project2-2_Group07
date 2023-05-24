package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.Scanner;

public class test {

    public static void main(String[] args) throws IOException {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Create a server socket to listen for incoming connections
        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("Listening for connections on port 8000");

        // Accept connections and read messages
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String message;
        while ((message = in.readLine()) != null) {
            if (message.equals("True")) {
                System.out.println("Face detected -Java message");
            } else if (message.equals("False")) {
                System.out.println("No face detected -Java message");
            }
        }

        // Close the input stream and the client socket
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
