import cv2

# Load the pre-trained face detection model
model_file = 'app/src/main/java/faceID/data/res10_300x300_ssd_iter_140000.caffemodel'
config_file = 'app/src/main/java/faceID/data/deploy.prototxt'
net = cv2.dnn.readNetFromCaffe(config_file, model_file)

# Open the video capture device (use 0 for built-in webcam)
cap = cv2.VideoCapture(0)

# Set the confidence threshold for the DNN
treshold = 0.3

while True:
    # Capture a frame from the video feed
    ret, frame = cap.read()

    # Prepare the input blob for the DNN
    blob = cv2.dnn.blobFromImage(frame, 1.0, (300, 300), (104.0, 177.0, 123.0))

    # Set the input for the DNN and perform a forward pass
    net.setInput(blob)
    detections = net.forward()

    # Loop through the detections
    for i in range(detections.shape[2]):
        # Extract the confidence of the detection
        confidence = detections[0, 0, i, 2]

        # Set a threshold for confidence (e.g., 0.5)
        if confidence > treshold:
            # Get the coordinates of the bounding box
            box = detections[0, 0, i, 3:7] * [frame.shape[1],
                                              frame.shape[0], frame.shape[1], frame.shape[0]]
            (x, y, x2, y2) = box.astype("int")

            # Draw the bounding box around the face
            cv2.rectangle(frame, (x, y), (x2, y2), (0, 255, 0), 2)

            # Display the confidence percentage
            text = f"{confidence * 100:.2f}%"
            cv2.putText(frame, text, (x, y - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.45, (0, 255, 0), 1)

    # Show the output
    cv2.imshow('Face Detection', frame)

    # Exit if the user presses 'q'
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release the video capture device and close the window
cap.release()
cv2.destroyAllWindows()
