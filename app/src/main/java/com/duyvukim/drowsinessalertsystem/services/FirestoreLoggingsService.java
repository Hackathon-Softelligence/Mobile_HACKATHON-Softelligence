package com.duyvukim.drowsinessalertsystem.services;

import android.util.Log;

import com.duyvukim.drowsinessalertsystem.detection.DetectionLog;
import com.duyvukim.drowsinessalertsystem.utils.AppCts;
import com.google.firebase.Timestamp;

import java.util.UUID;

public class FirestoreLoggingsService {

    /**
     * @param name
     * @param studentNo
     * @param status
     * @param imageUrl
     */
    public static void logDetection(String name, String studentNo, String status, String imageUrl) {
        String id = UUID.randomUUID().toString();

        Timestamp timestamp = Timestamp.now();
        DetectionLog log = new DetectionLog(id, name, status, imageUrl, studentNo, timestamp);

        FirebaseServicesProvider.getFirebaseFirestore()
                .collection(AppCts.Firebase.NAME_COLLECTION_DETECTION_LOGS)
                .document(studentNo)
                .collection("logs")
                .document(id)
                .set(log)
                .addOnSuccessListener(unused -> {
                    Log.d("FirestoreLogger", "logDetection: success");
                })
                .addOnFailureListener(e -> {
                    Log.d("FirestoreLogger", "logDetection: unsuccess");
                });
    }
}
