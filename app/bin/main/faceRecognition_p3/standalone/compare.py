import cv2
import face_recognition
import numpy as np

def draw_landmarks(image, landmarks):
    for _, points in landmarks.items():
        for point in points:
            cv2.circle(image, tuple(point), 1, (255,255,255), -1)  # landmarks in green color

img = cv2.imread("app/src/main/java/faceRecognition_p3/compare_image/Messi_compare.jpg")
rgb_img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
img_encoding = face_recognition.face_encodings(rgb_img)[0]

img2 = cv2.imread("app/src/main/java/faceRecognition_p3/images/Messi.jpg")
rgb_img2 = cv2.cvtColor(img2, cv2.COLOR_BGR2RGB)
img_encoding2 = face_recognition.face_encodings(rgb_img2)[0]

result = face_recognition.compare_faces([img_encoding], img_encoding2)
face_distances = face_recognition.face_distance([img_encoding], img_encoding2)

print("Result: ", result)
print("Confidence Score: ", 1/(1 + face_distances[0]))  

# Get landmarks for each face
landmarks1 = face_recognition.face_landmarks(rgb_img)[0] 
landmarks2 = face_recognition.face_landmarks(rgb_img2)[0] 

# Draw landmarks on each face
draw_landmarks(img, landmarks1)
draw_landmarks(img2, landmarks2)

# Resize images to the same size
max_height = max(img.shape[0], img2.shape[0])
max_width = max(img.shape[1], img2.shape[1])

img = cv2.resize(img, (max_width, max_height))
img2 = cv2.resize(img2, (max_width, max_height))

# Concatenate images horizontally
combined_image = np.concatenate((img, img2), axis=1)

cv2.imshow("Img & Img 2", combined_image)
cv2.waitKey(0)
