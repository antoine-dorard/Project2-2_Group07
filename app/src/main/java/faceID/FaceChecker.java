package faceID;

import faceID.java_py_sockets.FaceDetection;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

public class FaceChecker {

    FaceDetection faceDetection;

    public FaceChecker(){
        this.faceDetection = new FaceDetection();
    }

    public boolean checkFace(){
        try {
            System.out.println("Current working directory: " + System.getProperty("user.dir"));

            // Create a server socket to listen for incoming connections
            ServerSocket serverSocket = null;
            serverSocket = new ServerSocket(8000);

            System.out.println("Listening for connections on port 8000");

            faceDetection.runPythonScript();

            // Accept connections and read messages
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("True")) {
                    System.out.println("Face detected !");
                    break;
                }
            }

            Thread.sleep(1000);

            in.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
