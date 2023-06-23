package connection.zeromq.tests;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.util.Arrays;
import java.util.HashMap;

public class ZMQReqTest
{

    public static void main(String[] args) throws Exception
    {

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
}