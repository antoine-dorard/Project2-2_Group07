import cv2
import numpy as np
import pandas as pd
import os
import glob
import time
import matplotlib.pyplot as plt
from methods_face_detection import FaceRec

# Load the pre-trained face detection model
model_file = 'app/src/main/java/faceRecognition/models/res10_300x300_ssd_iter_140000.caffemodel'
config_file = 'app/src/main/java/faceRecognition/models/deploy.prototxt'

# Read the model using OpenCV's DNN module
net = cv2.dnn.readNetFromCaffe(config_file, model_file)

# Set the minimum confidence threshold for face detection
threshold = 0.8

# Instantiate FaceRec object and load encodings
sfr = FaceRec()

# Load the images to be used for encoding
sfr.load_encoding_images("app/src/main/java/faceRecognition/images")

results = []

# Load all .png images from the specified directory
image_files = glob.glob('app/src/main/java/faceRecognition/experiments/data/angles/*.png')

# Iterate over all images
for image_file in image_files:
    # Read each image file
    frame = cv2.imread(image_file)

    # Preprocess the image to prepare it for face detection
    blob = cv2.dnn.blobFromImage(frame, 1.0, (300, 300), (104.0, 177.0, 123.0))

    # Set the processed image as input to the network
    net.setInput(blob)

    # Run a forward pass to compute detections
    detections = net.forward()

    face_detected = False
    face_recognized = False
    recognized_names = []

    # Iterate over all detections
    for i in range(detections.shape[2]):
        confidence = detections[0, 0, i, 2]

        # If confidence is greater than threshold, detect the face
        if confidence > threshold:
            face_detected = True
            box = detections[0, 0, i, 3:7] * np.array([frame.shape[1], frame.shape[0], frame.shape[1], frame.shape[0]])
            (x, y, x2, y2) = box.astype("int")
            
            # Get the face region of interest
            faceROI = frame[y:y2, x:x2]
            
            # Convert the color space to RGB
            rgb_faceROI = cv2.cvtColor(faceROI, cv2.COLOR_BGR2RGB)
            
            # Detect known faces within the ROI
            _, face_names, _ = sfr.detect_known_faces(rgb_faceROI)
            
            recognized_names = [name for name in face_names if name != "Stranger"]
            
            if recognized_names:
                face_recognized = True
            break

    results.append({'Image': image_file, 'Face Detected': face_detected, 'Face Recognized': face_recognized, 'Names': ', '.join(recognized_names)})

# Convert results to a pandas DataFrame
df = pd.DataFrame(results)

results_dir = 'app/src/main/java/faceRecognition/experiments/results/angles'

# Check if results directory exists, if not, create it
if not os.path.exists(results_dir):
    os.makedirs(results_dir)
csv_path = os.path.join(results_dir, 'experiment_angles.csv')

# Save DataFrame to CSV
df.to_csv(csv_path, index=False)

# Notify user about the result saving
print(f"Results saved in '{csv_path}'")

# Print total number of images processed
print(f"Total images processed: {len(df)}")

# Print total number of faces detected
faces_detected = df['Face Detected'].sum()
print(f"Total faces detected: {faces_detected}")

# Print total number of faces recognized
faces_recognized = df['Face Recognized'].sum()
print(f"Total faces recognized: {faces_recognized}")

# Plot histogram of recognized faces
plt.figure(figsize=(10, 5))
df['Names'].value_counts().plot(kind='bar')
plt.title('Histogram of Recognized Faces')
plt.xlabel('Names')
plt.ylabel('Frequency')
plt.grid(axis='y')

# Save histogram plot
plt.savefig(os.path.join(results_dir, 'recognized_faces_histogram.png'))

# Plot pie chart of detected vs not detected faces
plt.figure(figsize=(10, 5))
df['Face Detected'].value_counts().plot(kind='pie', autopct='%1.1f%%')
plt.title('Detected vs Not Detected Faces')
plt.ylabel('')

# Save pie chart
plt.savefig(os.path.join(results_dir, 'detected_faces_piechart.png'))

# Notify user about the graphs saving
print(f"Graphs saved in '{results_dir}'")
