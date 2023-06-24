package connection.zeromq;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

public class ZMQMessenger {

    public ZMQReq zmqReq;

    public ZMQMessenger(String port) {

        // Construct the REQ ZeroMQ Server.
        zmqReq = new ZMQReq(port);

    }

    public void open(){
        zmqReq.open();
    }

    public void close(){
        zmqReq.close();
    }

    public ZMQMessage sendMessage(ZMQMessage zmqMessage, Boolean waitForReply) throws Exception {

        // From the input ZMQMessage, construct the JSON String to be send.
        String message = zmqMessage.convertToJSONString();

        // Send it.
        zmqReq.sendMessage(message);

        // Define the return reply object.
        ZMQMessage reply = null;

        // Wait for reply ?
        if (waitForReply) {

            // Get a reply.
            reply = waitForMessage();
        }

        return reply;
    }

    private ZMQMessage waitForMessage() throws ParseException {

        String reply = zmqReq.waitForMessage();

        ZMQMessage zmqReply = new ZMQMessage("", null);
        zmqReply.loadFromJSONString(reply);

        return zmqReply;
    }

    public static void main(String[] args) throws Exception {

        ZMQMessenger zmqMessenger = new ZMQMessenger("5555");
        zmqMessenger.open();

        while (!Thread.currentThread().isInterrupted()) {

            ZMQMessage message = new ZMQMessage("tapas_fine_tuned", "data");
            System.out.println("Sending message : " + message.convertToJSONString());

            ZMQMessage reply = zmqMessenger.sendMessage(message, true);

            System.out.println(reply.convertToJSONString());
        }
    }

}
