import cv2
import numpy as np
import random
import pandas as pd
import os

# Load the pre-trained face detection model
model_file = "app/src/main/java/faceID/data/res10_300x300_ssd_iter_140000.caffemodel"
config_file = "app/src/main/java/faceID/data/deploy.prototxt"
net = cv2.dnn.readNetFromCaffe(config_file, model_file)

# Open the video capture device (use 0 for built-in webcam)
cap = cv2.VideoCapture(0)

# Set the minimum confidence threshold for face detection
threshold = 0.8

results = []

while len(results) < 100:  # limit the experiment to 100 iterations
    # Capture a frame from the video feed
    ret, frame = cap.read()

    # Add Gaussian noise to the frame
    row, col, ch = frame.shape
    mean = 0
    std_dev = random.randint(1, 50)  # Random standard deviation for the noise
    gauss = np.random.normal(mean, std_dev, (row, col, ch)) * 0.5
    gauss = gauss.reshape(row, col, ch)
    noisy = cv2.add(frame, gauss.astype(frame.dtype))

    # Prepare the input blob for the DNN
    blob = cv2.dnn.blobFromImage(noisy, 1.0, (300, 300), (104.0, 177.0, 123.0))

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

    results.append(
        {"Standard Deviation of Noise": std_dev, "Face Detected": face_detected}
    )

    # Show the output
    cv2.imshow("Face Detection", noisy)

    # Exit if the user presses 'q'
    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

# Release the video capture device and close the window
cap.release()
cv2.destroyAllWindows()

# Create a DataFrame from results and display it
df = pd.DataFrame(results)

# Save results to CSV
if not os.path.exists("app/src/main/java/faceID/experiments/results"):
    os.makedirs("app/src/main/java/faceID/experiments/results")
df.to_csv("app/src/main/java/faceID/experiments/results/experiment_noise.csv", index=False)

print("Results saved in 'app/src/main/java/faceID/experiments/results/experiment_noise.csv'")
