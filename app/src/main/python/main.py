
import time
import json

from project22 import ZeroMQPublisher, ZeroMQRep, FaceRecognition, TapasFineTuned

print("Python started...")

#publisher = ZeroMQPublisher(port = "5556", debug = True)
#publisher.open()

replier = ZeroMQRep(port = "5557", debug = True)
replier.open()
replier.start()

# Add your new module here:
tft = TapasFineTuned(replier, debug = True)
tft.start()

for i in range(30):

    if(replier.has_new):
        reply = replier.get_last_message_as_dict()

        print("cmd : " + reply["cmd"])
        print("data : " + reply["data"])

        if (reply["cmd"] != "tapas_fine_tuned"):
            replier.send_response("response")

        elif (reply["cmd"] == "<SHUTDOWN>"):
            replier.send_response("<SHUTDOWNOK>")
            break

    time.sleep(1)



tft.stop()

replier.stop()
replier.close()

#publisher.close()
