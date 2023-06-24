package connection.zeromq;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.util.EventListener;

public class ZMQSub extends Thread {

    private ZContext context;
    private ZMQ.Socket socket;
    private String addr;

    public ZMQSub(String address){
        addr = address; // "tcp://localhost:5556"
    }

    public void open(){
        // Create context.
        context = new ZContext();

        // Create a Subscriber Socket.
        socket = context.createSocket(SocketType.SUB);
        // Connect to publisher
        socket.connect(addr);

        // Subscribe to all messages.
        socket.subscribe(ZMQ.SUBSCRIPTION_ALL);

        socket.setReceiveTimeOut(-1);
    }

    public void close(){
        try {
            socket.close();
            context.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        System.out.println("Subscriber started..");
        open();
        System.out.println("Subscriber opened..");

        try {
            while (!Thread.currentThread().isInterrupted()) {
                String message = socket.recvStr();
                if (message != null) {
                    System.out.println("Received: " + message);
                    if (message.equals("<SHUTDOWN>")) {
                        System.out.println("Shutdown received.. Exiting.");
                        interrupt();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        // Construct and start the subscriber.
        ZMQSub subscriber = new ZMQSub("tcp://localhost:5556");
        subscriber.start();
        // Will be stopped by PUB.
    }
}
