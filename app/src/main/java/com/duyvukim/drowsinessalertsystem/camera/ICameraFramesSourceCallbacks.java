package com.duyvukim.drowsinessalertsystem.camera;

import androidx.camera.core.ImageProxy;

public interface ICameraFramesSourceCallbacks {
    /**
     * This method will be called when received the frame as the image proxy
     *
     * @param imageProxy
     */
    public void onPassFrameToAnalyzer(ImageProxy imageProxy);
}
