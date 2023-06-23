package faceRecognition.live_feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class FaceRecognition {

    private static void runPythonScript() {
        String pythonScriptPath = "app/src/main/java/faceRecognition_p3/live_feed/logic.py"; 

        Process p;
        try {
            p = Runtime.getRuntime().exec(new String[] { "python", pythonScriptPath });

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("Error running Python script: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Start the Python script
        new Thread(FaceRecognition::runPythonScript).start();

        // Start local server to receive messages from Python script
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Server started on port 8000");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        System.out.println("Received Message: " + receivedMessage);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
