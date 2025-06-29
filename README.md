# Drowsiness Alert System 🚨😴

An Android-based real-time drowsiness detection system using ML Kit Face Detection and Firebase Cloud Firestore for logging incidents. This project is designed to enhance road safety by identifying signs of driver fatigue or distraction.

---

## 🔍 Features

- 🚗 Real-time **face and eye detection** using ML Kit
- 🧠 Head pose estimation and validation
- 👥 Detection of **multiple people** in camera frame
- 🔴 **Red bounding boxes** drawn around detected faces
- 📸 Automatic **incident logging** to Firestore
- ☁️ Firebase integration: Firestore + Cloud Storage (planned)
- ⚙️ MVP Architecture with clean separation of concerns

---

## 🧱 Technologies Used

- **Android (Java)**
- **CameraX** for real-time frame capture
- **Google ML Kit** for Face Detection
- **Firebase Firestore** for incident logging
- **View Binding** for efficient UI management
- **AtomicBoolean & AtomicInteger** for thread-safe detection counters

---

## 📦 Project Structure


---

## 📝 How it Works

1. **CameraX** streams frames to `FaceAnalyzer`
2. **ML Kit** processes the frame and detects:
   - Eye state (open/closed)
   - Face pose (looking away/down)
   - Number of faces
3. On detection thresholds:
   - A **warning is shown**
   - An event is **pushed to Firestore** with user details and (optional) image

---

## 🔐 Firestore Structure

