package connection.zeromq;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Arrays;
import java.util.HashMap;

public class ZMQReq {

    private ZContext context;
    private ZMQ.Socket socket;
    private String address;

    public ZMQReq(String port){

        //address = "tcp://*:" + port; //"tcp://*:5555"
        address = "tcp://localhost:" + port;

    }

    public void open(){
        // Create context.
        context = new ZContext();

        // Socket to talk to clients
        socket = context.createSocket(ZMQ.REQ);
        socket.connect(address);
    }

    public void close(){
        try {
            socket.close();
            context.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void sendMessage(String message){

        // Send message over Socket.
        socket.send(message.getBytes(ZMQ.CHARSET), 0);
    }


    public String waitForMessage(){

        // Block until a message is received
        byte[] reply = socket.recv(0);

        // Convert reply to string
        return (new String(reply, ZMQ.CHARSET));
    }


    public static void main(String[] args) throws Exception {

        ZMQReq zmqReq = new ZMQReq("5555");
        zmqReq.open();

        HashMap<String, Object> replyHM = new HashMap<>();
        HashMap<String, Object> msgHM = new HashMap<>();
        msgHM.put("cmd", "Message from Java!");
        msgHM.put("data", "");
        msgHM.put("request", 0);

        while (!Thread.currentThread().isInterrupted()) {

            // Construct a response.
            msgHM.put("request", (Integer) msgHM.get("request") + 1);

            // Convert to JSON.
            JSONObject msgJSON = new JSONObject(msgHM);

            // Send a response
            String message = msgJSON.toString();

            System.out.println("Sending message : " + message);
            zmqReq.sendMessage(message);


            System.out.println("Now waiting on messages...");
            String reply = zmqReq.waitForMessage();

            System.out.println(reply);
        }
    }

}
