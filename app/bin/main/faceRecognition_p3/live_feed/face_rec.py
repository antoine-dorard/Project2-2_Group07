import face_recognition
import cv2
import os
from pathlib import Path
import glob
import numpy as np

class FaceRec:
    def __init__(self):
        self.known_face_encodings = []
        self.known_face_names = []

        # Resize frame for a faster speed
        self.frame_resizing = 0.25
    
    def load_encoding_images(self, images_folder):
        root = Path(images_folder)
        
        # Loop over all directories
        for directory in root.iterdir():
            if directory.is_dir():
                person_name = directory.name  # assuming the directory name is the person's name
                
                # Loop over all files in the directory
                for file in directory.iterdir():
                    if file.is_file():
                        img_path = str(file)
                        img = cv2.imread(img_path)
                        rgb_img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
                
                        # Get encoding for multiple faces
                        img_encodings = face_recognition.face_encodings(rgb_img)

                        # Store file name and file encoding
                        for img_encoding in img_encodings:
                            self.known_face_encodings.append(img_encoding)
                            self.known_face_names.append(person_name)

        print("Encoding images loaded")

        
    # Detect faces in frame    
    def detect_known_faces(self, frame):
        small_frame = cv2.resize(frame, (0, 0), fx=self.frame_resizing, fy=self.frame_resizing)
        # Find all the faces and face encodings in the current frame of video
        # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
        rgb_small_frame = cv2.cvtColor(small_frame, cv2.COLOR_BGR2RGB)
        face_locations = face_recognition.face_locations(rgb_small_frame)
        face_encodings = face_recognition.face_encodings(rgb_small_frame, face_locations)

        face_names = []
        face_scores = []  # confidence scores list
        for face_encoding in face_encodings:
            # See if the face is a match for the known face(s)
            matches = face_recognition.compare_faces(self.known_face_encodings, face_encoding)
            name = "Stranger"
            score = 0  # default confidence score

            # Or instead, use the known face with the smallest distance to the new face
            face_distances = face_recognition.face_distance(self.known_face_encodings, face_encoding)
            best_match_index = np.argmin(face_distances)
            if matches[best_match_index]:
                name = self.known_face_names[best_match_index]
                # Calculate confidence score
                score = 1 / (1 + face_distances[best_match_index])
            face_names.append(name)
            face_scores.append(score)  # add score to the scores list

        # Convert to numpy array to adjust coordinates with frame resizing quickly
        face_locations = np.array(face_locations)
        face_locations = face_locations / self.frame_resizing
        return face_locations.astype(int), face_names, face_scores  # return scores as well
