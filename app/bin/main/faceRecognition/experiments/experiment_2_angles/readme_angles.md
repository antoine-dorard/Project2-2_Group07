
# Face Recognition in Various Angles

This project is a face recognition application that processes images at different angles to evaluate the performance of the face detection and recognition systems. The application reads images, detects faces, and recognizes them. It also provides detailed output on the faces recognized.

The key aspect of this application is analyzing how the angle of faces in images affects the face detection and recognition rates.

## Motivations and Techniques

The primary motivation for this project is to understand how face detection and recognition algorithms perform with images of faces at various angles. This will help identify the limitations of the implemented models and provide insights for future improvement.

The techniques used include:

-   OpenCV's DNN module for reading a pre-trained Caffe model for face detection.
-   A customized method from the `FaceRec` class for face recognition.
-   Processing images with faces at different angles and recording if a face was detected and/or recognized.
-   Visualization of the data to provide insights into the performance of the face detection and recognition systems.

## Prerequisites

This application depends on several Python libraries. You need to have the following installed:

-   OpenCV: Used for image and video processing.
-   NumPy: Required by OpenCV.
-   Pandas: Used for data manipulation and analysis.
-   Matplotlib: Used for creating static, animated, and interactive visualizations in Python.
-   Glob: Used to find all the pathnames matching a specified pattern.

You can install the dependencies using pip:

`pip install opencv-python numpy pandas matplotlib glob` 

## How to Use

1.  Make sure you have pictures of the same face but at different angles in data/images, if not, use the photobooth.py to take some.
2.  Set your preferred minimum confidence threshold for face detection in the `threshold` variable.
3.  Prepare a directory of images of faces that you want the application to recognize. These should be clear, frontal images of faces. Each individual's images should be in a separate sub-directory named after the individual inside a parent directory. By default, the parent directory should be at "app/src/main/java/faceRecognition/images".
4.  Prepare a directory of images that you want to test. This directory should contain images with faces at various angles. By default, this directory should be at "app/src/main/java/faceRecognition/experiments/data/angles".
5.  Run the application by executing the python file in your preferred Python environment.
6.  The results will be saved in the 'app/src/main/java/faceRecognition/experiments/results' directory as CSV and PNG files.

## Results

The application generates two plots saved as PNG images:

-   Histogram of recognized faces.
-   Pie chart of detected vs not detected faces.

These plots, along with a CSV file containing the data used to generate them, are saved in the 'app/src/main/java/faceRecognition/experiments/results' directory.

## Sources

This project uses the Single Shot Detector (SSD) with a ResNet base network architecture model for face detection, which is pre-trained on the FaceScrub dataset. More details about the model can be found [here](https://www.pyimagesearch.com/2018/02/26/face-detection-with-opencv-and-deep-learning/).

The face recognition part is based on the method proposed by the OpenFace project. More details can be found [here](https://cmusatyalab.github.io/openface/).