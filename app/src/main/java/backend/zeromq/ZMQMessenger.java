package backend.zeromq;

import org.json.simple.parser.ParseException;
import org.zeromq.ZMQ;

public class ZMQMessenger {

    public ZMQReq zmqReq;

    public ZMQMessenger() {

        // Construct the REQ ZeroMQ Server.
        zmqReq = new ZMQReq();

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
}
