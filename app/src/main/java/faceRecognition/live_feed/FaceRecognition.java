package faceRecognition.live_feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

// Main class FaceRecognition
public class FaceRecognition {

    // Python command depending on the OS version
    private static final String[] PYTHON_COMMANDS = { "python", "python3" };
    // Python packages needed for face recognition
    private static final String[] REQUIRED_PACKAGES = { "opencv-python", "face-recognition", "numpy", "pandas" };

    // Function to request user permission to install necessary Python packages
    public boolean requestUserPermission() {
        // Initialize scanner object
        Scanner scanner = new Scanner(System.in);

        // Inform user about the necessary packages
        System.out.println("The required Python packages are not installed.");
        System.out.println("Do you want to install them with admin access? (yes/no) [Press 'Enter' for 'yes']");

        // Collect user response
        String response = scanner.nextLine().toLowerCase();
        scanner.close();

        // Return true if user agrees to install necessary packages, false otherwise
        return response.equals("yes") || response.equals("y") || response.isEmpty();
    }

    // Function to ensure PIP (Python package installer) is installed
    public void ensurePipInstalled(String pythonCommand) {
        try {
            // Initialize process builder to run Python command
            ProcessBuilder pb = new ProcessBuilder(pythonCommand, "-m", "ensurepip", "--default-pip");
            Process process = pb.start();

            // Initialize buffers to read the output and error stream from the process
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            // Print output of pip installation process
            System.out.println("Pip installation output:");
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println(line);
            }

            // Check for pip installation errors
            System.out.println("Checking for PIP installation errors check...");
            while ((line = stderrReader.readLine()) != null) {
                System.out.println(line);
            }

            // Close the buffer readers
            stdoutReader.close();
            stderrReader.close();

            // Wait for process to finish
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error ensuring pip installation with command: " + pythonCommand);
        }
    }

    // Function to run Python script
    public void runPythonScript() {
        // Iterate over possible Python commands
        for (String pythonCommand : PYTHON_COMMANDS) {
            try {
                // Ensure pip is installed
                ensurePipInstalled(pythonCommand);

                // Check if required packages are installed
                ProcessBuilder pb = new ProcessBuilder(pythonCommand, "-c",
                        "import cv2; import face_recognition; import numpy; import socket");
                Process checkProcess = pb.start();
                int checkExitCode = checkProcess.waitFor();

                // If not all packages are installed
                if (checkExitCode != 0) {
                    // Request user permission to install packages
                    if (requestUserPermission()) {
                        System.out.println("Installing required Python packages...");

                        // Check python version and adjust pip command accordingly
                        String pipCommand = pythonCommand.equals("python") ? "pip" : "pip3";
                        // Iterate over required packages
                        for (String packageName : REQUIRED_PACKAGES) {
                            // Initialize process builder to install packages
                            ProcessBuilder installPb;
                            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                                // Command for Windows
                                installPb = new ProcessBuilder("cmd", "/c", "start", "/wait", "/b", pipCommand,
                                        "install", packageName);
                            } else {
                                // Command for Linux/macOS
                                installPb = new ProcessBuilder("sudo", pipCommand, "install", packageName);
                            }
                            Process installProcess = installPb.start();

                            // Buffer reader for package installation output
                            BufferedReader installOutputReader = new BufferedReader(
                                    new InputStreamReader(installProcess.getInputStream()));
                            String installLine;
                            // Print installation process
                            while ((installLine = installOutputReader.readLine()) != null) {
                                System.out.println(installLine);
                            }
                            installOutputReader.close();

                            // Wait for installation process to finish
                            installProcess.waitFor();
                        }
                    } else {
                        System.out
                                .println("Please install the required Python packages manually and try again.");
                        return;
                    }
                }

                // Run the Python script
                String scriptPath = "app/src/main/java/faceRecognition/live_feed/mainDetection.py";
                Process process = new ProcessBuilder(pythonCommand, scriptPath).start();

                // Buffers for Python script output and errors
                BufferedReader stdoutReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                BufferedReader stderrReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));
                String line;

                // Print Python script output
                System.out.println("Standard output:");
                while ((line = stdoutReader.readLine()) != null) {
                    System.out.println(line);
                }

                // Print Python script errors
                System.out.println("Standard error:");
                while ((line = stderrReader.readLine()) != null) {
                    System.out.println(line);
                }

                stdoutReader.close();
                stderrReader.close();

                // Wait for Python script to finish
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    break;
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Error running Python script with command: " + pythonCommand);
            }
        }
    }

    // Main function
    public static void main(String[] args) throws IOException {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        try (
                // Create a server socket to listen for incoming connections
                ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Listening for connections on port 8000");

            // Start the Python script in a separate thread
            Thread pythonThread = new Thread(() -> {
                new FaceRecognition().runPythonScript();
                System.out.println("Python script finished running");
            });
            pythonThread.start();

            // Accept connections and read messages
            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Received message: " + message);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading message: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error creating server socket: " + e.getMessage());
        }
    }
}
