package com.duyvukim.drowsinessalertsystem.detection;

import com.google.firebase.Timestamp;

public class DetectionLog {

    // ==============================
    // === Fields
    // ==============================

    public String id;
    public String name;
    public String studentNo;
    public String status;
    public String imageUrl;
    public Timestamp timestamp; // ← 🔥 Add this line

    // ==============================
    // === Constructors
    // ==============================

    public DetectionLog() {

    }

    public DetectionLog(String id, String name, String status, String imageUrl, String studentNo, Timestamp timestamp) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.imageUrl = imageUrl;
        this.studentNo = studentNo;
        this.timestamp = timestamp;
    }
}
