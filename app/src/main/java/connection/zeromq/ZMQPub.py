import zmq
import random
import sys
import time
import json

port = "5556"
if len(sys.argv) > 1:
    port =  sys.argv[1]
    int(port)

context = zmq.Context()
socket = context.socket(zmq.PUB)
socket.bind("tcp://*:%s" % port)

# Data to be written 
dictionary = { 
  "cmd": "Message from Python!", 
  "data": [1, 2, 3, 4, 5], 
  "request": 0
} 

for request in range(30):

    # Update the dictionary
    dictionary["request"] = request
    # Serializing json  
    json_object = json.dumps(dictionary, indent = 4)

    print("Sending request %s " % request)

    # Construct the request message to Java.
    # request_message = "This is message %s from Python!" % request
    request_message = str(json_object)
    socket.send(request_message.encode('utf-8'))

    time.sleep(1)