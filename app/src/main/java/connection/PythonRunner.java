package connection;

import connection.zeromq.ZMQMessage;
import connection.zeromq.ZMQMessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class PythonRunner {

    public void runPythonScript(String scriptPath) {
        Runnable pythonScriptRunner = new Runnable() {
            @Override
            public void run() {
                String pythonCommand = "python";

                // Run the Python script
                //String scriptPath = "app/src/main/python/main.py";
                try {
                    System.out.println("Executing : " + pythonCommand + scriptPath);
                    Process process = Runtime.getRuntime().exec(new String[]{pythonCommand, scriptPath});
                    System.out.println("Done Executing" + pythonCommand + scriptPath);

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

        new PythonRunner().runPythonScript("app/src/main/python/main.py");

        PythonConnector pythonConn = new PythonConnector();
        pythonConn.open();

        for(int i = 0; i < 10; i++){
            pythonConn.askTAPAS("What lectures is there on Monday at 9?");
        }

        String shutdownOK = pythonConn.askShutdown();

        System.out.println(shutdownOK);
    }
}
