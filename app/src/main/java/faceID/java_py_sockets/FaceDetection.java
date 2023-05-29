package faceID.java_py_sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FaceDetection {

    public boolean requestUserPermission() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("The required Python packages are not installed.");
        System.out.println("Do you want to install them with admin access? (yes/no) [Press 'Enter' for 'yes']");

        String response = scanner.nextLine().toLowerCase();
        scanner.close();

        return response.equals("yes") || response.equals("y") || response.isEmpty();
    }

    public void ensurePipInstalled(String pythonCommand) {
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

    public void runPythonScript() {
        Runnable pythonScriptRunner = new Runnable() {
            @Override
            public void run() {
                String[] pythonCommands = { "python", "python3" };
                String[] requiredPackages = { "opencv-python" };

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
                        String scriptPath = "app/src/main/java/faceID/java_py_sockets/DNN-Socket.py";
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

    public static void main(String[] args) throws IOException {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Create a server socket to listen for incoming connections
        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("Listening for connections on port 8000");

        new FaceDetection().runPythonScript();

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
