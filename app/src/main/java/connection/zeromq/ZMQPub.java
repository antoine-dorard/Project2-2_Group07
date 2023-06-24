package connection.zeromq;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

public class ZMQPub extends Thread {

    ZContext context;
    ZMQ.Socket socket;

    public void open(){
        // Create context.
        context = new ZContext();

        // Create a publisher socket
        socket = context.createSocket(SocketType.PUB);
        socket.bind("tcp://localhost:5556"); // Publish messages on port 5556
    }

    public void close(){
        // Send shutdown to Subscriber.
        socket.send("<SHUTDOWN>");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            // Ignore.
            System.out.println(e);
        }
        socket.close();
        context.close();
    }

    @Override
    public void run() {
        System.out.println("Publisher started..");
        open();
        System.out.println("Publisher opened..");

        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Publish some messages
                System.out.println("Sending messages..");
                socket.send("Hello, World!");
                socket.send("This is a message from the PUB.");

                // Wait for a while before stopping
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                    // Interrupted.
                    close();
                }
            }
        } catch (ZMQException e) {
            if (e.getErrorCode() == ZMQ.Error.ETERM.getCode()) {
                // Context was terminated, exit the thread.
                System.out.println("Subscriber thread interrupted. Exiting.");
            } else {
                // Handle other ZMQExceptions.
                System.out.println(e);
            }
        } finally {
            System.out.println("Closing..");
            close();
        }
    }

    public static void main(String[] args) {
        // Construct and start the subscriber.
        ZMQPub publisher = new ZMQPub();
        publisher.start();

        for (int i = 0; i < 10; i++) {
            // Wait for 10 seconds before stopping.
            try {
                System.out.println(i);
                Thread.sleep(1000);
            } catch (Exception e) {
                // Ignore.
                System.out.println(e);
            }
        }

        // Stop the subscriber.
        publisher.interrupt();
    }
}
