from threading import Thread
from ..zmq_rep import ZeroMQRep
from .tapas_predictor import TapasPredictor
import pandas as pd
import os

class TapasFineTuned():

    """
    DESCRIPTION
    ---------------
    

    """


    def __init__(self, replier, debug = False):

        self._debug         = debug
        self._replier       = replier

        self._stop          = False

        self._current_dir   = os.path.dirname(os.path.realpath(__file__))

        # Initialize the Tapas Predictor class instance.
        self.predictor = TapasPredictor(model_path = self._current_dir + "\\tapas_predictor\\schedule-tapas-small-finetuned-sqa.pth")
        self.table =  pd.read_csv(self._current_dir + "\\tapas_predictor\\student_schedule_week.csv").astype(str)


    def start(self):
        self._stop = False
        self.__start_process()

    def stop(self):
        self._stop = True


    def __start_process(self):

        if(self._debug): print("Initializing TapasFineTuned Process...")
        # create an instance of the event handler thread
        self._proc = Thread(name = "tapas_fine_tuned_process", target=self.__process)

        if(self._debug): print("Starting TapasFineTuned Process...")
        # start the event handler thread
        self._proc.start()


    def __process(self):

        if(self._debug): print("TapasFineTuned Started!")

        while not self._stop:

            if (self._replier.has_new):

                reply = self._replier.get_last_message_as_dict()

                if (reply["cmd"] == "tapas_fine_tuned"):

                    sentence = reply["data"]

                    if(self._debug): print("tft cmd received : " + sentence)

                    if(isinstance(sentence, str)):
                        queries = [sentence]
                    elif(isinstance(sentence, list)):
                        queries = sentence
                    else:
                        raise Exception("Type of input 'sentence' is not 'str' or 'list', so it's not supported...")

                    answers = self.predictor.generate_answers(queries, self.table)

                    #answer = "This is the 'tft' Python answer.."
                    answer = answers[0]

                    self._replier.send_response(answer)