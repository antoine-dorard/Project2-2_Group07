import cv2
import face_recognition
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.widgets import Slider, RadioButtons

def draw_landmarks(image, landmarks, size, color):
    for _, points in landmarks.items():
        for point in points:
            cv2.circle(image, tuple(point), size, color, -1) 

# Path to the images
path1 = "app/src/main/java/faceRecognition_p3/images/Antoine/Antoine.png"
path2 = "app/src/main/java/faceRecognition_p3/compare_image/Compare.png"

img = cv2.imread(path1)
img2 = cv2.imread(path2)

# Resize images to the same size while maintaining the aspect ratio
scale_percent = min(img.shape[1]/img2.shape[1], img.shape[0]/img2.shape[0])
width = int(img2.shape[1] * scale_percent)
height = int(img2.shape[0] * scale_percent)
dim = (width, height)

img2 = cv2.resize(img2, dim)

# Convert image color spaces after resizing
rgb_img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
rgb_img2 = cv2.cvtColor(img2, cv2.COLOR_BGR2RGB)

img_encoding = face_recognition.face_encodings(rgb_img)[0]
img_encoding2 = face_recognition.face_encodings(rgb_img2)[0]

result = face_recognition.compare_faces([img_encoding], img_encoding2)
face_distances = face_recognition.face_distance([img_encoding], img_encoding2)

print("Result: ", result)
print("Confidence Score: ", 1/(1 + face_distances[0]))  

# Get landmarks for each face after resizing
landmarks1 = face_recognition.face_landmarks(rgb_img)[0] 
landmarks2 = face_recognition.face_landmarks(rgb_img2)[0] 

# Draw landmarks on each face
dot_size = 1
color = (255, 255, 255) # white color by default
draw_landmarks(img, landmarks1, dot_size, color)
draw_landmarks(img2, landmarks2, dot_size, color)

# Calculate basic statistics for the images
mean1 = np.mean(img)# type: ignore
std_dev1 = np.std(img)# type: ignore
median1 = np.median(img)# type: ignore

mean2 = np.mean(img2) # type: ignore
std_dev2 = np.std(img2)# type: ignore
median2 = np.median(img2)# type: ignore

stats_text = f'Img1 - Mean: {mean1:.2f}, Std: {std_dev1:.2f}, Median: {median1:.2f}\n' \
             f'Img2 - Mean: {mean2:.2f}, Std: {std_dev2:.2f}, Median: {median2:.2f}'

# Display images using matplotlib for interactivity
fig, ax = plt.subplots(1,2, figsize=(10,10))

# Remove axis for both images
[axi.axis('off') for axi in ax.ravel()]

# Show images
ax[0].imshow(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
ax[1].imshow(cv2.cvtColor(img2, cv2.COLOR_BGR2RGB))

# Add a slider for adjusting the dot size
slider_ax = plt.axes([0.25, 0.01, 0.50, 0.03])# type: ignore
slider = Slider(slider_ax, 'Dot size', 1, 10, valinit=1, valstep=1)

# Add radio buttons for color change
color_ax = plt.axes([0.025, 0.5, 0.15, 0.15])# type: ignore
radio = RadioButtons(color_ax, ('white', 'red', 'green', 'blue'))

def update(val):
    size = int(slider.val)
    color_dict = {'white': (255, 255, 255), 'red': (255, 0, 0), 'green': (0, 255, 0), 'blue': (0, 0, 255)}
    color = color_dict[radio.value_selected]
    
    img1 = img.copy()
    img2_copy = img2.copy()
    
    draw_landmarks(img1, landmarks1, size, color)
    draw_landmarks(img2_copy, landmarks2, size, color)
    
    ax[0].imshow(cv2.cvtColor(img1, cv2.COLOR_BGR2RGB))
    ax[1].imshow(cv2.cvtColor(img2_copy, cv2.COLOR_BGR2RGB))
    fig.canvas.draw_idle()

slider.on_changed(update)
radio.on_clicked(update)

# Calculate additional statistics for the images
shape1 = img.shape
shape2 = img2.shape

num_landmarks1 = len(landmarks1)
num_landmarks2 = len(landmarks2)

min_pixel1 = np.min(img)
max_pixel1 = np.max(img)

min_pixel2 = np.min(img2)
max_pixel2 = np.max(img2)

stats_text += f'\nImg1 - Shape: {shape1}, Number of landmarks: {num_landmarks1}, Min pixel: {min_pixel1}, Max pixel: {max_pixel1}\n' \
              f'Img2 - Shape: {shape2}, Number of landmarks: {num_landmarks2}, Min pixel: {min_pixel2}, Max pixel: {max_pixel2}'

comparison_text = f'Face comparison result: {result}\nConfidence Score: {1/(1 + face_distances[0]):.2f}'

# Show stats and comparison result on frame
plt.gcf().text(0.02, 0.02, stats_text, fontsize=12)
plt.gcf().text(0.02, 0.95, comparison_text, fontsize=12)

plt.show()