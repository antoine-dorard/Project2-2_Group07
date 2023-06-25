import cv2
import os

def capture_images(save_path):
    
    print("Starting image capture. Press 'c' to capture an image and 'q' to quit the program.")
    print(f"Images will be saved in '{save_path}' and used for angle recognition.")
    
    # Create the directory if not exists
    if not os.path.exists(save_path):
        print(f"Creating directory '{save_path}' for saving images.")
        os.makedirs(save_path)

    # Initialize the camera (0 is the default camera)
    cap = cv2.VideoCapture(0)

    # Check if camera opened successfully
    if not cap.isOpened():
        print("Unable to open camera. Check if the camera is working properly.")
        return

    # Index for naming the image files
    i = 0

    # Start the image capture loop
    while True:
        # Read the frame from the camera
        ret, frame = cap.read()

        # If the frame was read successfully, display it on a window named 'Image Capture'
        if ret:
            cv2.imshow("Image Capture", frame)
        else:
            # If unable to read the frame, close the program
            print("Unable to read frame. Check if the camera is working properly.")
            return

        # Wait for user input for 1 millisecond
        key = cv2.waitKey(1)

        # If 'c' is pressed, capture the image
        if key == ord("c"):
            # Define the image save path
            image_path = os.path.join(save_path, f"image_{i+1}.png")
            
            # Save the image to the specified path
            cv2.imwrite(image_path, frame)

            # Notify the user that an image has been captured and saved
            print(f"Saved image {i+1} to {image_path}")

            # Increment the image index for the next image
            i += 1

        # If 'q' is pressed, quit the program
        elif key == ord("q"):
            print("Quitting program.")
            break

    # Release the camera resource and destroy the 'Image Capture' window
    cap.release()
    cv2.destroyAllWindows()
    print("Program ended. Camera released.")

# Use the function
capture_images(save_path="app/src/main/java/faceRecognition/experiments/data/angles")
