from threading import Thread

class FaceRecognition():

    """
    DESCRIPTION
    ---------------
    

    """

    def __init__(self, events, parameters, debug = False):

        self._debug         = debug


    def __start_process(self):

        if(self._debug): print("Initializing FaceRecognition Process...")
        # create an instance of the event handler thread
        self._proc = Thread(name = "face_recognition_process", target=self.__process)

        if(self._debug): print("Starting FaceRecognition Process...")
        # start the event handler thread
        self._proc.start()


    def __process(self):

        if(self._debug): print("FaceRecognition Started!")

        while True:

            print('stuff')