
import time
import json

from zmq_rep import ZeroMQRep


zmq_rep = ZeroMQRep(port = "5555", debug = True)
zmq_rep.open()

# Data to be written 
dictionary = { 
  "cmd": "Message from Python!", 
  "data": [1, 2, 3, 4, 5], 
  "request": 0
} 

for request in range(30):

    message = zmq_rep.wait_for_messages()

    print(message)

    zmq_rep.send_response(str(message))