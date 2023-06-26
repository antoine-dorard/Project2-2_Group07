package connection;

import connection.zeromq.ZMQMessage;
import connection.zeromq.ZMQMessenger;

public class PythonConnector {

    ZMQMessenger zmqMessenger;

    public PythonConnector(){
        zmqMessenger = new ZMQMessenger("5555");
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

    public String loadCosineSimilarity() throws Exception {

        ZMQMessage message = new ZMQMessage("load_cosine_model", "");

        ZMQMessage reply = zmqMessenger.sendMessage(message, true);

        return reply.getDataAsString();
    }

    public String askCosineSimilarity() throws Exception {

        String sentence1 = "sentence1";
        String sentence2 = "sentence2";

        ZMQMessage message = new ZMQMessage("bert_cosine_similarity", sentence1+"|||"+sentence2);

        ZMQMessage reply = zmqMessenger.sendMessage(message, true);

        return reply.getDataAsString();
    }



    public static void main(String[] args) throws Exception {

        PythonConnector pythonConn = new PythonConnector();
        pythonConn.open();

        for(int i = 0; i < 10; i++){
            pythonConn.askTAPAS("Question from Java?");
        }

        String shutdownOK = pythonConn.askShutdown();

        System.out.println(shutdownOK);
    }
}
