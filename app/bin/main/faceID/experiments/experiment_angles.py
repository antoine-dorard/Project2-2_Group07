#take some pictures of your face at different angles. use the photo booth script to take the pictures.

import cv2
import numpy as np
import pandas as pd
import os
import glob
import time

# Load the pre-trained face detection model
model_file = 'app/src/main/java/faceID/data/res10_300x300_ssd_iter_140000.caffemodel'
config_file = 'app/src/main/java/faceID/data/deploy.prototxt'
net = cv2.dnn.readNetFromCaffe(config_file, model_file)

# Set the minimum confidence threshold for face detection
threshold = 0.8

results = []

# Load images
image_files = glob.glob('app/src/main/java/faceID/experiments/data/angles/*.png')  # replace 'images' with your actual directory

for image_file in image_files:
    frame = cv2.imread(image_file)

    # Prepare the input blob for the DNN
    blob = cv2.dnn.blobFromImage(frame, 1.0, (300, 300), (104.0, 177.0, 123.0))

    # Set the input for the DNN and perform a forward pass
    net.setInput(blob)
    detections = net.forward()

    face_detected = False
    # Loop through the detections
    for i in range(detections.shape[2]):
        # Extract the confidence of the detection
        confidence = detections[0, 0, i, 2]

        if confidence > threshold:
            face_detected = True
            break

    results.append({'Image': image_file, 'Face Detected': face_detected})

# Create a DataFrame from results and display it
df = pd.DataFrame(results)

# Save results to CSV
results_dir = 'app/src/main/java/faceID/experiments/results'
if not os.path.exists(results_dir):
    os.makedirs(results_dir)
df.to_csv(os.path.join(results_dir, 'experiment_angles.csv'), index=False)

print(f"Results saved in '{os.path.join(results_dir, 'experiment_angles.csv')}'")
