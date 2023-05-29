import cv2
import os


def capture_images(save_path):
    # Create the directory if not exists
    if not os.path.exists(save_path):
        os.makedirs(save_path)

    cap = cv2.VideoCapture(0)

    if not cap.isOpened():
        print("Unable to open camera")
        return

    i = 0
    while True:
        ret, frame = cap.read()

        if ret:
            cv2.imshow("Image Capture", frame)
        else:
            print("Unable to read frame")
            return

        key = cv2.waitKey(1)

        # If 'c' is pressed, capture the image
        if key == ord("c"):
            # Save the image to the specified path
            image_path = os.path.join(save_path, f"image_{i+1}.png")
            cv2.imwrite(image_path, frame)
            print(f"Saved image {i+1} to {image_path}")
            i += 1
        # If 'q' is pressed, quit the program
        elif key == ord("q"):
            break

    cap.release()
    cv2.destroyAllWindows()


# Use the function
capture_images(save_path="app/src/main/java/faceID/experiments/data/angles")
