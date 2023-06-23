package connection.zeromq.tests;

import org.zeromq.*;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class ZMQPubSubTest {
    public static void main(String[] args) {
        ZContext context = new ZContext();

        // Create a publisher socket
        ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
        publisher.bind("tcp://*:5555"); // Publish messages on port 5555

        // Create a subscriber socket
        ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
        subscriber.connect("tcp://localhost:5555"); // Connect to publisher

        // Subscribe to all messages
        subscriber.subscribe(ZMQ.SUBSCRIPTION_ALL);

        // Start the subscriber thread
        Thread subscriberThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String message = subscriber.recvStr();
                    if (message != null) {
                        System.out.println("Received: " + message);
                    }
                }
            } catch (ZMQException e) {
                if (e.getErrorCode() == ZMQ.Error.ETERM.getCode()) {
                    // Context was terminated, exit the thread
                    System.out.println("Subscriber thread interrupted. Exiting.");
                } else {
                    // Handle other ZMQExceptions
                    e.printStackTrace();
                }
            } finally {
                subscriber.close();
                context.close();
            }
        });
        subscriberThread.start();

        for(int i = 0; i < 10; i++) {
            // Publish some messages
            publisher.send("Hello, World!");
            publisher.send("This is a test message.");

            // Wait for a while before stopping
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Stop the subscriber thread
        subscriberThread.interrupt();
    }
}