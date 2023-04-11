import cv2

# Load the pre-trained face detection classifier
face_cascade = cv2.CascadeClassifier(
    'app/src/main/java/faceID/data/haarcascade_frontalface_default.xml')

# Open the video capture device (0 for built-in webcam)
cap = cv2.VideoCapture(0)

while True:
    # Capture a frame from the video feed
    ret, frame = cap.read()

    # Convert the frame to grayscale (face detection works better on grayscale images)
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Detect faces in the grayscale image
    faces = face_cascade.detectMultiScale(
        gray, scaleFactor=1.1, minNeighbors=5)

    # Draw a bounding box around each detected face and add text with confidence score
    for (x, y, w, h) in faces:
        # Calculate the percentage of the frame that the face takes up
        cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
        


    # Show the output
    cv2.imshow('Face Detection', frame)

    # Exit if the user presses 'q'
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release the video capture device and close the window
cap.release()
cv2.destroyAllWindows()
