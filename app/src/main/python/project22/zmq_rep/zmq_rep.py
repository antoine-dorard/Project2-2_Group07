import zmq
from threading import Thread
import time
import json

class ZeroMQRep():

    """
    DESCRIPTION
    ---------------
    

    """

    def __init__(self, port, debug = False):

        self._debug = debug
        self._port = port

        self.last_message = ""
        self.last_response = ""

        self._stop = False
        
        self.has_new = False


    def open(self):

        if(self._debug): print("Opening ZeroMQRep...")

        # Create context.
        self.context = zmq.Context()

        #  Socket to talk to server
        self.socket = self.context.socket(zmq.REP)
        #self.socket.connect("tcp://localhost:%s" % self._port)
        self.socket.bind("tcp://*:%s" % self._port)

        if(self._debug): print("Opened ZeroMQRep on %s" % ("tcp://localhost:%s" % self._port))


    def close(self):

        if(self._debug): print("Closing ZeroMQRep...")

        # Close socket.
        self.socket.close()

        # Terminate the context.
        self.context.term()


    def wait_for_messages(self):

        try :
            #  Get the reply.
            self.last_message = self.socket.recv()

        except Exception as e :
            if (self._stop):
                # Ignore 'context terminated error',
                # we are supposed to stop anyway.
                pass
            else:
                # Something else went wrong.
                raise e

        self.has_new = True

        if(self._debug): print("Message received : " + str(self.last_message))

        return self.last_message


    def send_response(self, response):

        if(self._debug): print("Sending response : " + response)

        # Data to be written 
        response_dict = { 
          "cmd": self.get_last_message_as_dict()["cmd"], 
          "data": response
        } 

        # Serializing json  
        json_object = json.dumps(response_dict, indent = 4)

        self.last_response = str(json_object)

        # First encode the message, then send it.
        self.socket.send(self.last_response.encode('utf-8'))

        self.has_new = False


    def get_last_message_as_dict(self):

        try :
            message_dict = json.loads(self.last_message)
        except : 
            message_dict = None

        return message_dict


    def start(self):
        self._stop = False
        self.__start_process()

    def stop(self):
        self._stop = True


    def __start_process(self):

        if(self._debug): print("Initializing ZeroMQRep Process...")
        # create an instance of the event handler thread
        self._proc = Thread(name = "zmq_rep_process", target=self.__process)

        if(self._debug): print("Starting ZeroMQRep Process...")
        # start the event handler thread
        self._proc.start()


    def __process(self):

        if(self._debug): print("ZeroMQRep Started!")

        while not self._stop:

            if not self.has_new:
                # Wait for messages..
                message = self.wait_for_messages()

            else :
                time.sleep(0.1)
           
