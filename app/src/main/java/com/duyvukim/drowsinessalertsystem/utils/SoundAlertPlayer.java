package com.duyvukim.drowsinessalertsystem.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import com.duyvukim.drowsinessalertsystem.R;

public class SoundAlertPlayer {
    private static final String TAG = "SoundAlertPlayer";
    private SoundPool soundPool;
    private int soundId;
    private boolean isLoaded = false;

    // Khởi tạo SoundPool và tải âm thanh
    public void init(Context context) {
        if (soundPool != null) { // Tránh khởi tạo lại
            Log.w(TAG, "SoundPool already initialized.");
            return;
        }

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5) // Tối đa 5 luồng âm thanh phát đồng thời
                .setAudioAttributes(audioAttributes)
                .build();

        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0) {
                isLoaded = true;
                Log.d(TAG, "Sound loaded successfully! ID: " + soundId);
            } else {
                isLoaded = false;
                Log.e(TAG, "Failed to load sound. Status: " + status);
            }
        });

        soundId = soundPool.load(context, R.raw.alert_sound, 1);
    }

    /**
     * Phát âm thanh lặp lại một số lần nhất định mà không có độ trễ.
     * Âm thanh sẽ phát liên tiếp rất nhanh, có thể chồng chéo.
     */
    public void playSound() {
        if (!isLoaded) {
            Log.e(TAG, "Sound not loaded yet! Cannot play.");
            return;
        }
        soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
//        Log.d(TAG, "Playing sound " + numberOfTimes + " times.");
//        for (int i = 0; i < numberOfTimes; i++) {
//
//        }
    }

    /**
     * Giải phóng tài nguyên SoundPool. Luôn gọi phương thức này khi không còn cần SoundAlertPlayer nữa.
     */
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            isLoaded = false;
            Log.d(TAG, "SoundPool released.");
        }
    }
}