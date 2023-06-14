import zmq
import time
import json

context = zmq.Context()

#  Socket to talk to server
print("Connecting to java server")
socket = context.socket(zmq.REQ)
socket.connect("tcp://localhost:5555")


# Data to be written 
dictionary = { 
  "cmd": "Message from Python!", 
  "data": [1, 2, 3, 4, 5], 
  "request": 0
} 

#  Do 10 requests, waiting each time for a response
for request in range(30):

    # Request every second.
    time.sleep(1)

    # Update the dictionary
    dictionary["request"] = request
    # Serializing json  
    json_object = json.dumps(dictionary, indent = 4) 

    print("Sending request %s " % request)

    # Construct the request message to Java.
    # request_message = "This is message %s from Python!" % request
    request_message = str(json_object)
    socket.send(request_message.encode('utf-8'))

    #  Get the reply.
    message = socket.recv()
    print("Received reply from Java : %s [ %s ]" % (request, message))

    # Convert message to dictionary.
    message_dict = json.loads(message)
    print(message_dict)
