
import time
import json

from zmq_pub import ZeroMQPublisher


port = "5556"

zmq_pub = ZeroMQPublisher(port = "5556", debug = True)
zmq_pub.open()

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
    
    zmq_pub.send_message(request_message)

    time.sleep(1)

zmq_pub.send_message("<SHUTDOWN>")