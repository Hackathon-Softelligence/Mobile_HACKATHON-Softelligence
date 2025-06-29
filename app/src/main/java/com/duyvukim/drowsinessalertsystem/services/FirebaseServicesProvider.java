package com.duyvukim.drowsinessalertsystem.services;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseServicesProvider {
    public static final FirebaseFirestore getFirebaseFirestore() {
        String projectId = FirebaseApp.getInstance().getOptions().getProjectId();
        return FirebaseFirestore.getInstance("drowsiness-detection");
    }

    public static final FirebaseMessaging getFirebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }
}
