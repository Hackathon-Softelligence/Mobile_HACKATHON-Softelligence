package com.duyvukim.drowsinessalertsystem.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.PixelCopy;
import android.view.View;
import android.view.Window;

public class ScreenShot {
    public interface ScreenShotCallback {
        void onScreenShot(Bitmap bitmap);

        void onError(Exception e);
    }

    // Chụp màn hình Activity, trả về Bitmap qua callback
    public static void capture(Activity activity, ScreenShotCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Không quan trọng lắm
            // Dùng PixelCopy cho API 26+
            Window window = activity.getWindow();
            View view = window.getDecorView().getRootView();
            Bitmap bitmap = Bitmap.createBitmap(
                    view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888
            );
            PixelCopy.request(window, bitmap, result -> {
                if (result == PixelCopy.SUCCESS) {
                    callback.onScreenShot(bitmap);
                } else {
                    callback.onError(new Exception("PixelCopy failed: " + result));
                }
            }, new Handler(Looper.getMainLooper()));
        } else {
            // Dùng DrawingCache cho API thấp hơn
            try {
                View rootView = activity.getWindow().getDecorView().getRootView();
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
                rootView.setDrawingCacheEnabled(false);
                callback.onScreenShot(bitmap);
            } catch (Exception e) {
                callback.onError(e);
            }
        }
    }
}