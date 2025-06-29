package com.duyvukim.drowsinessalertsystem.camera;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

// Just like the list view adapter, place storing image frames
// CameraSource → FaceAnalyzer → EyeDetector → bật chuông cảnh báo

/**
 * 1. Initialize CameraX
 * 2. Show the front camera preview
 * 3. Streaming each camera frame into the FaceAnalyzer for processing
 */
public class CameraFramesSource {

    // =========================================
    // === Fields
    // =========================================

    private PreviewView previewView;
    private LifecycleOwner lifecycleOwner;
    private ICameraFramesSourceCallbacks callbacks;

    // =========================================
    // === Constructors
    // =========================================

    public CameraFramesSource(
            PreviewView previewView,
            LifecycleOwner lifecycleOwner,
            ICameraFramesSourceCallbacks callbacks
    ) {
        this.previewView = previewView;
        this.lifecycleOwner = lifecycleOwner;
        this.callbacks = callbacks;
    }

    // =========================================
    // === Methods
    // =========================================

    public void start() {
        // Using listenable future to get the camera provider back
        // asynchronously, so have to wait for it to be ready
        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture
                = ProcessCameraProvider.getInstance(previewView.getContext());

        // interact the camera provider on the UI thread
        cameraProviderListenableFuture.addListener(() -> {
                    try {

                        // 1. Get the camera controller
                        ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();

                        // 2. setup camera preview
                        Preview preview = buildPreview(previewView);

                        // 3. setup image (frame) analyzer
                        ImageAnalysis imageAnalyzer = buildImageAnalyzer();

                        // 4. Setup front camera
                        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                        // 5. Bind the camera to the lifecycle
                        //
                        cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalyzer
                        );


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                ContextCompat.getMainExecutor(previewView.getContext()));
    }

    /**
     * Only get the latest frame from the camera
     * drop the old one if new frame is coming
     *
     * @return
     */
    private ImageAnalysis buildImageAnalyzer() {
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        // Pass the frame to the analyzer
        // Run on the UI thread
        imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(previewView.getContext())
                , callbacks::onPassFrameToAnalyzer);

        return imageAnalysis;
    }

    /**
     * Build the camera preview for display only
     *
     * @param previewView
     * @return
     */
    private Preview buildPreview(PreviewView previewView) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        return preview;
    }
}
