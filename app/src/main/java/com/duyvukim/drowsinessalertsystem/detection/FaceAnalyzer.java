package com.duyvukim.drowsinessalertsystem.detection;

import android.media.Image;

import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageProxy;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

public class FaceAnalyzer {

    // =========================================
    // === Fields
    // =========================================

    private final FaceDetector faceDetector;
    private final IFaceAnalyzerCallbacks faceCallback;
    private final FaceDetectorOptions faceDetectorOptions;

    // =========================================
    // === Constructors
    // =========================================

    public FaceAnalyzer(IFaceAnalyzerCallbacks faceCallbacks) {
        this.faceCallback = faceCallbacks;

        // Build the face detector with the custom options
        faceDetectorOptions = new FaceDetectorOptions.Builder()
                .enableTracking() // for real time frames
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();

        this.faceDetector = FaceDetection.getClient(faceDetectorOptions);
    }

    // =========================================
    // === Methods
    // =========================================

    /**
     * Analyze the image frame from the camera
     *
     * @param imageProxy
     */
    @ExperimentalGetImage
    public void analyzeImageFrame(ImageProxy imageProxy) {

        // analyze the image frame by frame captured from the camera
        // CameraX wraps frame of image into the ImageProxy and send to the analyzer
        Image imageFrame = imageProxy.getImage();
        if (imageFrame == null) return;

        // pass the image frame to the face detector
        InputImage inputImage = InputImage.fromMediaImage(imageFrame, imageProxy.getImageInfo().getRotationDegrees());

        // process the image
        faceDetector.process(inputImage)
                .addOnSuccessListener(faces -> {

                    if (faces.size() <= 0) return;

                    // pass the number of faces detected
                    faceCallback.onMultipleFacesCountDetected(faces.size());

                    // pass the list of faces for rendering overlay
//                    faceCallback.onMultipleFacesDetected(faces);

                    for (Face face : faces) {

                        // pass the face to the callback if detected
                        faceCallback.onFaceDetected(face);
                    }
                })
                .addOnCompleteListener(task -> imageProxy.close());
    }
}