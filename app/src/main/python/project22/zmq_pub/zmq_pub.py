import zmq

class ZeroMQPublisher():

    """
    DESCRIPTION
    ---------------
    

    """

    def __init__(self, port, debug = False):

        self._debug = debug
        self._port = port


    def open(self):

        if(self._debug): print("Opening ZeroMQPublisher...")

        # Create context.
        self.context = zmq.Context()

        # Create a Subscriber Socket.
        self.socket = self.context.socket(zmq.PUB)

        # Bind to address.
        self.socket.bind("tcp://*:%s" % self._port)

        if(self._debug): print("Opened ZeroMQPublisher on %s" % ("tcp://*:%s" % self._port))


    def close(self):

        if(self._debug): print("Closing ZeroMQPublisher ...")

        # Close socket.
        self.socket.close()

        # Terminate the context.
        self.context.term()


    def send_message(self, message):

        # First encode the message, then send it.
        self.socket.send(message.encode('utf-8'))