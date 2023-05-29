# Project2-2_Group07

Our chatbot is designed to be user-friendly and accurate in its responses.
To ensure that our chatbot is delivering accurate responses, we have implemented a spelling checker using multiple algorithms such as 
the Levenshtein distance algorithm, the soundex algorithm, and more.
Additionally, we have created an editor that allows users to add their own questions, giving our chatbot the ability to learn and expand its knowledge base.
We have also included a sleek and intuitive graphical user interface (GUI) to make interacting with our chatbot as smooth and enjoyable as possible.


### Versions
* **Java:** 18  
* **Gradle:** 7.5 or above
* **Python:** 3.*

### How to run

1. Build gradle dependencies
2. Make sure you have python installed and added to your PATH variable, the dependencies will be installed automatically
3. Run the program with the main method App.java (and not the gradle task run)

### Possible issues:
* __Python related:__  
If you encounter problems during the start of the app, before the camera has opened,
it is most likely because python is not found on your machine. If you have python installed, you can try to add it to 
your PATH variable. If after some debugging you don't reach a solution, you can deactivate the camera checking at the end
by switching the CHECK_FACE boolean to false in App.java. This will allow you to run the app without the camera, but you
will not be able to use the face recognition feature.
* __Known mac issue:__  
Unfortunately, a last minute bug has been discovered that prevents the app from running on mac. This is caused by the
incapacity of the app to read the content of fonts on mac (the fonts are not loaded); Thus that the main window cannot
be displayed. This issue is not present on windows and linux. We are sorry for the inconvenience.
