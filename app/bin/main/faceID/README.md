
# Face Detection and Recognition: OpenCV, Python, and Deep Learning

This project focuses on face detection and recognition using OpenCV, Python, and Deep Learning techniques. The repository contains different implementations to give you flexibility in choosing the approach that best suits your needs.

## Getting Started

### Prerequisites

Before you can run the code, you need to have Python and OpenCV installed on your system. Additionally, make sure to install the required dependencies using the following command:

bashCopy code

`pip install -r requirements.txt` 

If you don't have pip installed, please refer to online tutorials for installation instructions.

### Running the Code

To execute the program, you have two options:

1.  Right-click on the desired file and select "Run" or "Debug".
2.  Manually run the file in the terminal.

There are four main files in the `faceID` folder:

-   `DNN.py`: Implements face detection and recognition using deep neural networks. This can be run independently.
-   `HaasClassifier.py`: Implements face detection and recognition using Haar Cascade classifiers. This can be run independently.
-   `DNN-Socket.py`: Acts as a socket for communication with the `FaceDetection.java` file. Run this after starting `FaceDetection.java`.
-   `FaceDetection.java`: Initializes the socket for communication with `DNN-Socket.py`. Run this before starting `DNN-Socket.py`.

### Dependencies

The following libraries are used in this project:

-   OpenCV
-   NumPy

Install these dependencies using pip by running:

bashCopy code

`pip install -r requirements.txt` 

## References

All pre-trained models and classifiers used in this project were obtained from the [OpenCV GitHub repository](https://github.com/opencv/opencv).

## Author

Alex

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://chat.openai.com/LICENSE.md) file for details.

## Acknowledgments

-   [OpenCV](https://opencv.org/)
-   [NumPy](https://numpy.org/)