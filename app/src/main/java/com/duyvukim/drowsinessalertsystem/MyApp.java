package com.duyvukim.drowsinessalertsystem;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Add Firebase
        FirebaseApp.initializeApp(this);
    }
}
