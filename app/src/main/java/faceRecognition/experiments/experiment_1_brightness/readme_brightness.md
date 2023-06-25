# Face Recognition Experiment

This project is a face recognition application experiment that uses OpenCV and deep learning. The application processes video frames in real-time from a webcam feed, detects faces within the frames, recognizes them and outputs some statistics. It adjusts the brightness of each frame randomly to analyze the effect of brightness on the detection and recognition rates.

The program is designed to run for a total of 100 iterations.

## Motivations and Techniques

The main motivation of this experiment is to analyze how different brightness levels affect the accuracy of face detection and face recognition.

The techniques used include:

-   Using OpenCV's DNN module to load a pre-trained Caffe model for face detection.
-   Adjusting brightness levels of the frames randomly.
-   Extracting the face Region of Interest (ROI) and attempting to recognize the face using a method from the `FaceRec` class.
-   Recording whether a face was detected and/or recognized, along with the names of recognized individuals (if any).
-   Calculating detection and recognition rates and plotting them against brightness levels.
-   Counting the number of times each individual was recognized.

## Prerequisites

This application depends on several Python libraries. You'll need to have the following installed:

-   OpenCV: Used for image and video processing.
-   NumPy: Required by OpenCV.
-   Pandas: Used for data manipulation and analysis.
-   Matplotlib: Used for creating static, animated, and interactive visualizations in Python.
-   Random: Used to generate random numbers.

You can install the dependencies using pip:

`pip install opencv-python numpy pandas matplotlib` 

## How to Use

1.  Make sure you have a camera connected.
2.  Set your preferred minimum confidence threshold for face detection in the `threshold` variable.
3.  Prepare a directory of images of faces that you want the application to recognize. These should be clear, frontal images of faces. Each individual's images should be in a separate sub-directory named after the individual inside a parent directory. By default, the parent directory should be at "app/src/main/java/faceRecognition/images".
4.  Run the application by executing the python file in your preferred Python environment.
5.  The application will display the video feed in a new window. To stop the program, press 'q' while the window is in focus.
6.  The results will be saved in 'app/src/main/java/faceRecognition/experiments/results' directory as CSV and PNG files.

## Results

The application generates three plots saved as PNG images:

-   Detection rate by brightness level.
-   Recognition rate by brightness level.
-   Number of recognitions per person.

These plots, along with a CSV file containing the data used to generate them, are saved in the 'app/src/main/java/faceRecognition/experiments/results' directory.

## Sources

This project uses the Single Shot Detector (SSD) with a ResNet base network architecture model for face detection, which is pre-trained on the FaceScrub dataset. More details about the model can be found [here](https://www.pyimagesearch.com/2018/02/26/face-detection-with-opencv-and-deep-learning/).

The face recognition part is based on the method proposed by the OpenFace project. More details can be found [here](https://cmusatyalab.github.io/openface/).