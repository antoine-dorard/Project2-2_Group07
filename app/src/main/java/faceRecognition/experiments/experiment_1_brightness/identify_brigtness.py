import cv2
import numpy as np
import random
import pandas as pd
import os
import matplotlib.pyplot as plt
from methods_face_detection import FaceRec

# Load the pre-trained face detection model from disk
model_file = "app/src/main/java/faceRecognition/models/res10_300x300_ssd_iter_140000.caffemodel"
config_file = "app/src/main/java/faceRecognition/models/deploy.prototxt"
net = cv2.dnn.readNetFromCaffe(config_file, model_file)
print("Face detection model loaded.")

# Open the video capture device (use 0 for built-in webcam)
cap = cv2.VideoCapture(0)
print("Video capture starting, please wait for face encodings to load...")

# Set the minimum confidence threshold for face detection
threshold = 0.8

# Instantiate FaceRec object and load encodings
sfr = FaceRec()
sfr.load_encoding_images("app/src/main/java/faceRecognition/images")
print("Face encodings loaded.")

results = []

while len(results) < 200:  # limit the experiment to 100 iterations
    # Capture a frame from the video feed
    ret, frame = cap.read()

    # Adjust brightness level randomly
    brightness = random.uniform(0, 3)  # Random brightness factor
    frame = cv2.convertScaleAbs(frame, alpha=brightness, beta=50)

    # Create a blob from the frame and normalize it
    blob = cv2.dnn.blobFromImage(frame, 1.0, (300, 300), (104.0, 177.0, 123.0))

    # Use the blob as input to the model and obtain the face detections
    net.setInput(blob)
    detections = net.forward()

    face_detected = False
    face_recognized = False
    recognized_names = []
    # Loop over the detections
    for i in range(detections.shape[2]):
        # Extract the confidence (i.e., probability) associated with the detection
        confidence = detections[0, 0, i, 2]

        # If the detection is confident enough
        if confidence > threshold:
            # Mark that a face was detected
            face_detected = True

            # Compute the (x, y)-coordinates of the bounding box for the face
            box = detections[0, 0, i, 3:7] * [frame.shape[1], frame.shape[0], frame.shape[1], frame.shape[0]]
            (x, y, x2, y2) = box.astype("int")

            # Extract the face ROI and convert it from BGR to RGB
            faceROI = frame[y:y2, x:x2]
            rgb_faceROI = cv2.cvtColor(faceROI, cv2.COLOR_BGR2RGB)

            # Attempt to recognize the face in the image
            _, face_names, _ = sfr.detect_known_faces(rgb_faceROI)

            # If the face was recognized, record the names of the recognized individuals
            recognized_names = [name for name in face_names if name != "Stranger"]
            if recognized_names:
                face_recognized = True
            break

    # Store the results
    results.append({"Brightness": brightness, "Face Detected": face_detected, "Face Recognized": face_recognized, "Names": ', '.join(recognized_names)})

    # Display the image in the window
    cv2.imshow("Face Detection and Recognition", frame)

    # Break the loop on 'q' key press
    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

# Release the video capture and close any open windows
cap.release()
cv2.destroyAllWindows()
print("Video capture stopped.")

# Create a DataFrame from the results
df = pd.DataFrame(results)

# Create a directory for storing the results, if it doesn't exist
results_dir = "app/src/main/java/faceRecognition/experiments/results/brightness"
if not os.path.exists(results_dir):
    os.makedirs(results_dir)

# Save the DataFrame to a CSV file
csv_path = os.path.join(results_dir, "experiment_brightness.csv")
df.to_csv(csv_path, index=False)
print(f"Results saved in '{csv_path}'")

# Print statistics
print(f"Total frames processed: {len(df)}")
print(f"Frames where a face was detected: {df['Face Detected'].sum()}")
print(f"Frames where a face was recognized: {df['Face Recognized'].sum()}")

# Generate and save plots
df.groupby('Brightness')['Face Detected'].mean().plot(kind='line', title='Detection rate by brightness')
plt.savefig(os.path.join(results_dir, 'detection_rate_by_brightness.png'))

df.groupby('Brightness')['Face Recognized'].mean().plot(kind='line', title='Recognition rate by brightness')
plt.savefig(os.path.join(results_dir, 'recognition_rate_by_brightness.png'))

df['Names'].value_counts().plot(kind='bar', title='Number of recognitions per person')
plt.savefig(os.path.join(results_dir, 'number_of_recognitions_per_person.png'))

print(f"Graphs saved in '{results_dir}'")
