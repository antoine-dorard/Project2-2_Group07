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

            int faceCounter = 0;

            System.out.println("\n\n\n\n\n\n\n\n\n");
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("True")) {
                    faceCounter++;

                    if(faceCounter > 50){
                        System.out.println("Face detected !");
                        break;
                    }
                }else if(message.equals("False")){
                    faceCounter = 0;
                }

                printProgress(faceCounter, 50);
            }

            in.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private void printProgress(int current, int total) {
        StringBuilder progress = new StringBuilder("[");
        int percentage = (current * 100) / total;
        for(int i = 0; i < 100; i++){
            if(i < percentage){
                progress.append("=");
            }else if(i == percentage){
                progress.append(">");
            }else{
                progress.append(" ");
            }
        }
        progress.append("] " + percentage + "%");
        System.out.print("\r" + progress.toString());
    }
}
