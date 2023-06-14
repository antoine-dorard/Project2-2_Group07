package zeromq;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ZMQserver
{

    public static void main(String[] args) throws Exception
    {

        runPythonScript();

        HashMap<String, Object> replyHM = new HashMap<>();
        HashMap<String, Object> msgHM = new HashMap<>();
        msgHM.put("cmd", "Message from Java!");
        msgHM.put("data", "");
        msgHM.put("request", 0);

        try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(ZMQ.REP);
            socket.bind("tcp://*:5555");

            while (!Thread.currentThread().isInterrupted()) {

                System.out.println("Now waiting on messages...");

                // Block until a message is received
                byte[] reply = socket.recv(0);

                // Convert reply to string
                String replyStr = new String(reply, ZMQ.CHARSET);

                // Convert that reply string to a JSON Object.
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(replyStr);

                // Construct the reply HashMap.
                String[] ids = {"cmd", "data", "request"};
                for (String id : ids) {
                    replyHM.put(id, json.get(id));
                }

                System.out.println(replyHM);

                // Construct a response.
                Object request = json.get("request");
                Integer intRequest = ((Long) request).intValue();
                msgHM.put("request", intRequest);

                // Before converting to JSON, convert the data to string (only accepts strings).
                int[] data = new int[]{10, 20, 30};
                msgHM.put("data", Arrays.toString(data));

                // Convert to JSON.
                JSONObject msgJSON = new JSONObject(msgHM);

                // Send a response
                String response = msgJSON.toString();
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
            }
        }
    }

    public static void runPythonScript() {
        Runnable pythonScriptRunner = new Runnable() {
            @Override
            public void run() {
                String[] pythonCommands = { "python", "python3" };
                String[] requiredPackages = {};

                for (String pythonCommand : pythonCommands) {
                    try {
                        ensurePipInstalled(pythonCommand);

                        // Check if required packages are installed
                        ProcessBuilder pb = new ProcessBuilder(pythonCommand, "-c", "import cv2; import socket");
                        Process checkProcess = pb.start();
                        int checkExitCode = checkProcess.waitFor();

                        // Install missing packages
                        if (checkExitCode != 0) {
                            if (requestUserPermission()) {
                                System.out.println("Installing required Python packages...");

                                String pipCommand = pythonCommand.equals("python") ? "pip" : "pip3";
                                for (String packageName : requiredPackages) {
                                    ProcessBuilder installPb;
                                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                                        // Windows
                                        installPb = new ProcessBuilder("cmd", "/c", "start", "/wait", "/b", pipCommand,
                                                "install", packageName);
                                    } else {
                                        // Linux/macOS
                                        installPb = new ProcessBuilder("sudo", pipCommand, "install", packageName);
                                    }
                                    Process installProcess = installPb.start();

                                    // Show installation progress and status
                                    BufferedReader installOutputReader = new BufferedReader(
                                            new InputStreamReader(installProcess.getInputStream()));
                                    String installLine;
                                    while ((installLine = installOutputReader.readLine()) != null) {
                                        System.out.println(installLine);
                                    }
                                    installOutputReader.close();

                                    installProcess.waitFor();
                                }
                            } else {
                                System.out
                                        .println("Please install the required Python packages manually and try again.");
                                return;
                            }
                        }

                        // Run the Python script
                        //app/src/main/java/zeromq/ZMQclient.py
                        String scriptPath = "app/src/main/java/zeromq/ZMQclient.py";
                        Process process = Runtime.getRuntime().exec(new String[] { pythonCommand, scriptPath });

                        BufferedReader stdoutReader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));
                        BufferedReader stderrReader = new BufferedReader(
                                new InputStreamReader(process.getErrorStream()));
                        String line;

                        System.out.println("Standard output:");
                        while ((line = stdoutReader.readLine()) != null) {
                            System.out.println(line);
                        }

                        System.out.println("Standard error:");
                        while ((line = stderrReader.readLine()) != null) {
                            System.out.println(line);
                        }

                        stdoutReader.close();
                        stderrReader.close();

                        int exitCode = process.waitFor();
                        if (exitCode == 0) {
                            break;
                        }
                    } catch (IOException | InterruptedException e) {
                        System.out.println("Error running Python script with command: " + pythonCommand);
                    }
                }
            }
        };

        Thread pythonScriptThread = new Thread(pythonScriptRunner);
        pythonScriptThread.start();
    }

    public static void ensurePipInstalled(String pythonCommand) {
        try {
            String pipCommand = pythonCommand.equals("python") ? "pip" : "pip3";
            ProcessBuilder pb = new ProcessBuilder(pythonCommand, "-m", "ensurepip", "--default-pip");
            Process process = pb.start();

            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            System.out.println("Pip installation output:");
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println(line);
            }

            System.out.println("Pip installation error check...");
            while ((line = stderrReader.readLine()) != null) {
                System.out.println(line);
            }

            stdoutReader.close();
            stderrReader.close();

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error ensuring pip installation with command: " + pythonCommand);
        }
    }

    public static boolean requestUserPermission() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("The required Python packages are not installed.");
        System.out.println("Do you want to install them with admin access? (yes/no) [Press 'Enter' for 'yes']");

        String response = scanner.nextLine().toLowerCase();
        scanner.close();

        return response.equals("yes") || response.equals("y") || response.isEmpty();
    }
}