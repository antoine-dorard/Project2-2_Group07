## How to create a new python module callable from java

1. Create a new python module in the folder `app/src/main/python/project22/`  
*E.g.*: `app/src/main/python/project22/tapas_fine_tuned/`
2. Create two new python files in your new folder:   
   1. `__init__.py`  
   2. `<module_name>.py`  
*E.g.*: `app/src/main/python/project22/tapas_fine_tuned/tapas_fine_tuned.py`
3. Add the following code to the `<module_name>.py` file (change all tapas occurrences to your module name):  
  
```python
from threading import Thread
from ..zmq_rep import ZeroMQRep


class TapasFineTuned():

    """
    DESCRIPTION
    ---------------
    

    """


    def __init__(self, replier, debug = False):

        self._debug         = debug
        self._replier       = replier

        self._stop          = False


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

                    ## EXECUTE HERE

                    answer = "This is the 'tft' Python answer.."

                    self._replier.send_response(answer)
```

4. Update the `__init__.py` file (change TapasFineTuned to your module name):  
  
```python
from .tapas_fine_tuned import TapasFineTuned
```

5. Update the `__init__.py` file in the `app/src/main/python/` folder by adding the previous line of code at the end.
6. Update the `app/src/main/python/main.py` file by adding the following code after the comment `# Add your new module here`:  
  
```python
tft = TapasFineTuned(replier, debug = True)
tft.start()
```