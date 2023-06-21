package backend.zeromq;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ZMQReq {

    private ZContext context;
    private ZMQ.Socket socket;

    public ZMQReq(){

        context = new ZContext();

        // Socket to talk to clients
        socket = context.createSocket(ZMQ.REQ);
        socket.bind("tcp://*:5555");
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

}
