package com.duyvukim.drowsinessalertsystem.utils;

public class AppCts {
    public static class Notifications {
        public final static String NOTIFICATION_CHANNEL = "drowsy_alert";
        public final static int NOTIFICATION_ID = 2001;
    }

    public static class Identities {
        public final static String EXAM_CODE = "111111";
    }

    public static class Firebase {
        public final static String NAME_COLLECTION_DETECTION_LOGS = "detection_logs";
    }

    public static class FakeUser {
        public final static String USER_NAME = "Nguyễn Văn An";
        public final static String USER_STUDENTCODE = "SE123456";
    }

    public static class ProblemStatus {
        public final static String STATUS_IS_DROWSY = "Ngủ gật";
        public final static String STATUS_IS_HEAD_PROBLEM = "Đầu lắc qua lắc lại";
        public final static String STATUS_MORE_THAN_ONE_PEOPLE = "Xuất hiện nhiều người";
    }

    public static class Thresholds {
        public final static int FRAMES_CLOSED_THRESHOLD = 100;
        public final static int FRAMES_MULTIPLE_PEOPLE_THRESHOLD = 100;
        public static final int FRAMES_HEAD_POSE_PROBLEM_THRESHOLD = 200;
        public final static float LEFT_EYE_CLOSED_THRESHOLD = 0.4f;
        public final static float RIGHT_EYE_CLOSED_THRESHOLD = 0.4f;

    }
}
