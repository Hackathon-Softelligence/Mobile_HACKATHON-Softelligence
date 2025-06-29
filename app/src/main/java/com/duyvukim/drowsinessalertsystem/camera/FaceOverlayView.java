package com.duyvukim.drowsinessalertsystem.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FaceOverlayView extends View {

    private final Paint paint;
    private List<Rect> faceBounds = new ArrayList<>();
    private int imageWidth = 0;
    private int imageHeight = 0;
    private boolean isFrontCamera = true; // default to front camera

    public FaceOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(0xFFFF0000); // Red
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
    }

    public void setImageSourceInfo(int imageWidth, int imageHeight, boolean isFrontCamera) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.isFrontCamera = isFrontCamera;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (imageWidth == 0 || imageHeight == 0) return;

        float scaleX = (float) getWidth() / imageHeight; // Image rotated to portrait
        float scaleY = (float) getHeight() / imageWidth;

        for (Rect faceRect : faceBounds) {
            float left = faceRect.left * scaleX;
            float top = faceRect.top * scaleY;
            float right = faceRect.right * scaleX;
            float bottom = faceRect.bottom * scaleY;

            if (isFrontCamera) {
                float mirroredLeft = getWidth() - right;
                float mirroredRight = getWidth() - left;
                canvas.drawRect(mirroredLeft, top, mirroredRight, bottom, paint);
            } else {
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }
}
