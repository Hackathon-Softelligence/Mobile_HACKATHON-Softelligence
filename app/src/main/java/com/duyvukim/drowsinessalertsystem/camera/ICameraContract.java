package com.duyvukim.drowsinessalertsystem.camera;

import androidx.camera.view.PreviewView;
import androidx.lifecycle.LifecycleOwner;

public interface ICameraContract {

    // =========================================
    // === View
    // =========================================

    interface View {
        void showAlert();

        void showMessage(String msg);

        void updateFaceOverlaySourceInfo(int imageWidth, int imageHeight, boolean isFrontCamera);
    }

    // =========================================
    // === Presenter
    // =========================================

    interface Presenter {
        void startCamera(PreviewView previewView, LifecycleOwner lifecycleOwner);
    }
}
