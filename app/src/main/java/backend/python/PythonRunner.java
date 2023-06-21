package backend.python;

import backend.zeromq.ZMQMessage;
import backend.zeromq.ZMQMessenger;
import backend.zeromq.ZMQReq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class PythonRunner {

    public void runPythonScript() {
        Runnable pythonScriptRunner = new Runnable() {
            @Override
            public void run() {
                String pythonCommand = "python";

                // Run the Python script
                String scriptPath = "app/src/main/java/backend/python/pythonExecute.py";
                try {
                    Process process = Runtime.getRuntime().exec(new String[]{pythonCommand, scriptPath});

                    BufferedReader stdoutReader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));
                    BufferedReader stderrReader = new BufferedReader(
                            new InputStreamReader(process.getErrorStream()));
                    String line;

                    System.out.println("Standard output:");
                    while ((line = stdoutReader.readLine()) != null) {
                        System.out.println(line);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread pythonScriptThread = new Thread(pythonScriptRunner);
        pythonScriptThread.start();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        new PythonRunner().runPythonScript();

        ZMQMessenger zmqMessenger = new ZMQMessenger();

        for(int i = 0; i < 20; i++) {

            // Sending message.
            System.out.println("Sending message...");

            // Construct sending message.
            ZMQMessage message = new ZMQMessage("double", "2");

            // Send a response and wait for reply.
            ZMQMessage reply = zmqMessenger.sendMessage(message, true);

            // Print reply.
            System.out.println(reply.convertToJSONString());
        }

        System.out.println("send stop");
        ZMQMessage stopMsg = new ZMQMessage("Stop", "");
        zmqMessenger.sendMessage(stopMsg, false);
    }
}
