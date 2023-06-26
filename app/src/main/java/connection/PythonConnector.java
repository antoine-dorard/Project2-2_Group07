package connection;

import connection.zeromq.ZMQMessage;
import connection.zeromq.ZMQMessenger;

public class PythonConnector {

    ZMQMessenger zmqMessenger;

    public PythonConnector(){
        zmqMessenger = new ZMQMessenger("5557");
    }


    public void open(){
        zmqMessenger.open();
    }


    public String askTAPAS(String question) throws Exception {

        ZMQMessage message = new ZMQMessage("tapas_fine_tuned", question);

        ZMQMessage reply = zmqMessenger.sendMessage(message, true);

        return reply.getDataAsString();
    }

    public String askShutdown() throws Exception {

        ZMQMessage message = new ZMQMessage("<SHUTDOWN>", "");

        ZMQMessage reply = zmqMessenger.sendMessage(message, true);
        // Should be "<SHUTDOWNOK>"

        return reply.getDataAsString();
    }



    public static void main(String[] args) throws Exception {

        PythonConnector pythonConn = new PythonConnector();
        pythonConn.open();

        String[] questions = {
                "What lectures is there on Monday at 9",
                "What lectures is there on Monday at 11",
                "What lectures is there on Monday at 13",
                "What lectures is there on Monday at 15",
                "What lectures is there on Monday at 17",
                "What lectures is there on Tuesday at 9",
                "What lectures is there on Tuesday at 11",
                "What lectures is there on Tuesday at 13",
                "What lectures is there on Tuesday at 15",
                "What lectures is there on Tuesday at 17",
                "What lectures is there on Wednesday at 9",
                "What lectures is there on Wednesday at 11",
                "What lectures is there on Wednesday at 13",
                "What lectures is there on Wednesday at 15",
                "What lectures is there on Wednesday at 17",
                "What lectures is there on Thursday at 9",
                "What lectures is there on Thursday at 11",
                "What lectures is there on Thursday at 13",
                "What lectures is there on Thursday at 15",
                "What lectures is there on Thursday at 17",
                "What lectures is there on Friday at 9",
                "What lectures is there on Friday at 11",
                "What lectures is there on Friday at 13",
                "What lectures is there on Friday at 15",
                "What lectures is there on Friday at 17"
        };
        for(String question : questions){
            System.out.println("Question : " + question);
            String reply = pythonConn.askTAPAS(question);
            System.out.println("Answer : " + reply);
        }

        String shutdownOK = pythonConn.askShutdown();

        System.out.println(shutdownOK);
    }
}
