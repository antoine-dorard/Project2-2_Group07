from threading import Thread
from ..zmq_rep import ZeroMQRep

import torch
from BertForSTS import BertForSTS
from CosineSimilarityLoss import CosineSimilarityLoss
from transformers import BertTokenizer


class CosineSimilarity():
    """
    DESCRIPTION
    ---------------

    """

    def __init__(self, replier, debug=False):
        self._debug = debug
        self._replier = replier

        self._stop = False

        self.model = None
        self.tokenizer = None

    def start(self):
        self._stop = False
        self.__start_process()

    def stop(self):
        self._stop = True

    def __start_process(self):

        if (self._debug): print("Initializing TapasFineTuned Process...")
        # create an instance of the event handler thread
        self._proc = Thread(name="tapas_fine_tuned_process", target=self.__process)

        if (self._debug): print("Starting TapasFineTuned Process...")
        # start the event handler thread
        self._proc.start()

    def __process(self):

        if (self._debug): print("TapasFineTuned Started!")

        while not self._stop:

            if (self._replier.has_new):

                reply = self._replier.get_last_message_as_dict()

                if (reply["cmd"] == "load_cosine_model"):
                    sentence = reply["data"]
                    if (self._debug): print("tft cmd received : " + sentence)

                    self.load_model()

                    answer = "cosine model loaded."
                    self._replier.send_response(answer)

                if (reply["cmd"] == "bert_cosine_similarity"):
                    sentence = reply["data"]
                    sentence = sentence.split("|||")
                    if (self._debug): print("tft cmd received : " + sentence)

                    score = self.predict_cosine_similarity(sentence, sentence)

                    answer = str(score)
                    self._replier.send_response(answer)

    def predict_cosine_similarity(self, sentence1, sentence2):
        test_input = self.tokenizer([sentence1, sentence2], padding='max_length', max_length=128, truncation=True, return_tensors="pt")
        test_input['input_ids'] = test_input['input_ids']
        test_input['attention_mask'] = test_input['attention_mask']
        del test_input['token_type_ids']

        output = self.model(test_input)
        return torch.nn.functional.cosine_similarity(output[0], output[1], dim=0).item()

    def load_model(self):
        f = open("myfile.txt", "x")

        self.tokenizer = BertTokenizer.from_pretrained('bert-base-uncased')
        loaded_model = BertForSTS()
        loaded_model.load_state_dict(torch.load("/app/model"))
        self.model = loaded_model
