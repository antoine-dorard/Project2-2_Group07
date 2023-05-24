import cv2
import numpy as np
import pandas as pd
import os
import glob
import time
import matplotlib.pyplot as plt

# Load the pre-trained face detection model
model_file = "app/src/main/java/faceID/data/res10_300x300_ssd_iter_140000.caffemodel"
config_file = "app/src/main/java/faceID/data/deploy.prototxt"
net = cv2.dnn.readNetFromCaffe(config_file, model_file)

results_dir = "app/src/main/java/faceID/experiments/results"
if not os.path.exists(results_dir):
    os.makedirs(results_dir)

# Load images
image_files = glob.glob("app/src/main/java/faceID/experiments/data/angles/*.png")

# List of thresholds for face detection
thresholds = [0.6, 0.7, 0.8, 0.9]

for threshold in thresholds:
    results = []

    for image_file in image_files:
        frame = cv2.imread(image_file)

        # Simulate lighting conditions by scaling pixel values
        lighting_scale = np.random.uniform(0.5, 1.5)
        frame = cv2.convertScaleAbs(frame, alpha=lighting_scale, beta=0)

        # Add Gaussian noise to the frame
        row, col, _ = frame.shape
        mean = 0
        std_dev = np.random.randint(1, 50)  # Random standard deviation for the noise
        gauss = np.random.normal(mean, std_dev, (row, col, 1)).astype(np.uint8)
        gauss = gauss.repeat(3, axis=2)
        noisy_frame = cv2.add(frame, gauss)

        # Prepare the input blob for the DNN
        blob = cv2.dnn.blobFromImage(
            noisy_frame, 1.0, (300, 300), (104.0, 177.0, 123.0)
        )

        # Set the input for the DNN and perform a forward pass
        net.setInput(blob)
        start_time = time.time()
        detections = net.forward()
        detection_time = time.time() - start_time

        face_detected = 0
        avg_confidence = 0
        # Loop through the detections
        for i in range(detections.shape[2]):
            # Extract the confidence of the detection
            confidence = detections[0, 0, i, 2]

            if confidence > threshold:
                face_detected += 1
                avg_confidence += confidence

        avg_confidence /= max(face_detected, 1)
        results.append(
            {
                "Image": image_file,
                "Faces Detected": face_detected,
                "Avg Confidence": avg_confidence,
                "Detection Time": detection_time,
                "Noise Std Dev": std_dev,
                "Lighting Scale": lighting_scale,
            }
        )

    # Create a DataFrame from results and display it
    df = pd.DataFrame(results)

    # Save results to CSV
    csv_file_path = os.path.join(
        results_dir, f"experiment_angles_t{int(threshold*10)}.csv"
    )
    df.to_csv(csv_file_path, index=False)

    print(f"Results saved in '{csv_file_path}'")

    # Plotting
    df.plot(
        kind="bar",
        x="Image",
        y="Faces Detected",
        title=f"Faces Detected (threshold = {threshold})",
    )
    plt.figure(figsize=(10, 6))

    # Faces Detected
    plt.subplot(221)
    df.plot(
        kind="bar",
        x="Image",
        y="Faces Detected",
        ax=plt.gca(),
        title=f"Faces Detected (threshold = {threshold})",
    )
    plt.xticks([])  # remove xticks for clarity

    # Average Confidence
    plt.subplot(222)
    df.plot(
        kind="bar",
        x="Image",
        y="Avg Confidence",
        ax=plt.gca(),
        title=f"Average Confidence (threshold = {threshold})",
    )
    plt.xticks([])  # remove xticks for clarity

    # Detection Time
    plt.subplot(223)
    df.plot(
        kind="bar",
        x="Image",
        y="Detection Time",
        ax=plt.gca(),
        title=f"Detection Time (threshold = {threshold})",
    )
    plt.xticks([])  # remove xticks for clarity

    # Noise and Lighting Conditions
    plt.subplot(224)
    df.plot(
        kind="line", x="Image", y="Noise Std Dev", ax=plt.gca(), label="Noise Std Dev"
    )
    df.plot(
        kind="line",
        x="Image",
        y="Lighting Scale",
        ax=plt.gca(),
        label="Lighting Scale",
        title="Noise and Lighting Conditions",
    )
    plt.xticks([])  # remove xticks for clarity

    plt.tight_layout()
    plt.savefig(os.path.join(results_dir, f"plot_threshold_{int(threshold * 10)}.png"))
    plt.show()
