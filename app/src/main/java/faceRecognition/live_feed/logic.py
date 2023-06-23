import cv2
import numpy as np
from face_rec import FaceRec
import logging
import socket
import sys

# Initialize logging
logging.basicConfig(level=logging.INFO)

try:
    # Load the pre-trained face detection model
    model_file = 'app/src/main/java/faceRecognition_p3/data/res10_300x300_ssd_iter_140000.caffemodel'
    config_file = 'app/src/main/java/faceRecognition_p3/data/deploy.prototxt'
    net = cv2.dnn.readNetFromCaffe(config_file, model_file)
except Exception as e:
    logging.error(f"Error loading model: {e}")
    exit(1)

# Set the minimum confidence threshold for face detection
threshold = 0.8

# Create a dictionary to save face_names and their corresponding unique IDs
face_id = dict()
current_max_id = 0

# Encode faces from a folder
sfr = FaceRec()
sfr.load_encoding_images("app/src/main/java/faceRecognition_p3/images")

# Load Camera
cap = cv2.VideoCapture(0)

if not cap.isOpened():
    logging.error("Unable to open camera")
    exit(1)

def send_message_to_java(message: str):
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_address = ('localhost', 8000)
        sock.connect(server_address)
        sock.sendall(message.encode('utf-8'))
    except socket.error as e:
        print(f"Socket error: {e}")
    finally:
        sock.close()

while True:
    ret, frame = cap.read()
    
    if not ret:
        logging.error("Error reading frame")
        continue

    # Prepare the input blob for the DNN
    try:
        blob = cv2.dnn.blobFromImage(frame, 1.0, (300, 300), (104.0, 177.0, 123.0))
    except Exception as e:
        logging.error(f"Error preparing blob: {e}")
        continue

    # Set the input for the DNN and perform a forward pass
    net.setInput(blob)
    detections = net.forward()

    # Detect Faces
    for i in range(detections.shape[2]):
        confidence = detections[0, 0, i, 2]

        if confidence > threshold:
            # Get the coordinates of the bounding box
            box = detections[0, 0, i, 3:7] * [frame.shape[1],
                                              frame.shape[0], frame.shape[1], frame.shape[0]]
            (x, y, x2, y2) = box.astype("int")

            # Extract the face ROI
            faceROI = frame[y:y2, x:x2]
            rgb_faceROI = cv2.cvtColor(faceROI, cv2.COLOR_BGR2RGB)
            
            # Detect and recognize faces using face_recognition
            face_locations, face_names, face_scores = sfr.detect_known_faces(rgb_faceROI)
            
            for face_loc, name, score in zip(face_locations, face_names, face_scores):
                # If face is known and has no id, assign a unique id
                if name != "Stranger" and name not in face_id:
                    face_id[name] = current_max_id
                    current_max_id += 1
                    send_message_to_java(f"Face detected: {name}")

                # Draw rectangle and label
                label = f"{name}_{face_id.get(name, '')}: {score * 100:.2f}%"
                cv2.putText(frame, label, (x, y - 10), cv2.FONT_HERSHEY_DUPLEX, 1, (0, 0, 200), 2)
                cv2.rectangle(frame, (x, y), (x2, y2), (0, 0, 200), 4) # type: ignore

    cv2.imshow("Frame", frame)

    key = cv2.waitKey(1)
    if key == 27:
        break

cap.release()
cv2.destroyAllWindows()
