import zmq
import time
import json
from .pythonMethods import double

def execute(command, data):
    print(command)
    print(data)

    if(command == "double"):
        return double(int(data))
    else:
        return ""


context = zmq.Context()

#  Socket to talk to server
print("Connecting to java server")
socket = context.socket(zmq.REP)
socket.connect("tcp://localhost:5555")

dictionary = {
    "cmd": "",
    "data": ""
}

stop = False

while not stop:

    #  Get the reply.
    message = socket.recv()

    # Convert message to dictionary.
    message_dict = json.loads(message)

    # Should we stop?
    if message_dict["cmd"] == "Stop":
        stop = True
    else :
        stop = False

    new_data = execute(message_dict["cmd"], message_dict["data"])

    dictionary["cmd"] = message_dict["cmd"]
    dictionary["data"] = new_data

    time.sleep(1)

    # Serializing json
    json_object = json.dumps(dictionary, indent = 4)

    request_message = str(json_object)
    socket.send(request_message.encode('utf-8'))
