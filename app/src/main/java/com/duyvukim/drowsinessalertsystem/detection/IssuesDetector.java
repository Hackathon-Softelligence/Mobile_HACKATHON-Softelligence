package com.duyvukim.drowsinessalertsystem.detection;

import android.util.Log;

import com.duyvukim.drowsinessalertsystem.utils.AppCts;
import com.google.mlkit.vision.face.Face;

public class IssuesDetector {

    /**
     * Detect the problem with the head pose
     *
     * @param face
     * @return
     */
    public static String isHeadPoseProblem(Face face) {
        float HEAD_YAW_THRESHOLD = 60.0f; // Rotation around Y-axis (left-right head turn)
        float HEAD_PITCH_THRESHOLD = 60.0f; // Rotation around X-axis (up-down head tilt)
        float HEAD_ROLL_THRESHOLD = 45.0f; // Rotation around Z-axis (sideways head tilt)

        float headYaw = face.getHeadEulerAngleY();   // Y-axis rotation (left/right)
        float headPitch = face.getHeadEulerAngleX(); // X-axis rotation (up/down)
        float headRoll = face.getHeadEulerAngleZ();  // Z-axis rotation (sideways tilt)
        // Check for "looking away" based on yaw angle
        if (headYaw > HEAD_YAW_THRESHOLD) {
            // Positive Yaw means head turned to the user's left (i.e., looking right from camera's perspective)
            return "Head Turned Left";
        } else if (headYaw < -HEAD_YAW_THRESHOLD) {
            // Negative Yaw means head turned to the user's right (i.e., looking left from camera's perspective)
            return "Head Turned Right";
        }

        // Check for head tilt based on pitch angle
        if (headPitch > HEAD_PITCH_THRESHOLD) {
            // Positive Pitch means head tilted downwards
            return "Head Tilted Down";
        } else if (headPitch < -HEAD_PITCH_THRESHOLD) {
            // Negative Pitch means head tilted upwards
            return "Head Tilted Up";
        }

        // Check for sideways head roll
        if (headRoll > HEAD_ROLL_THRESHOLD) {
            // Positive Roll typically means head tilted to the user's right shoulder
            return "Head Rolled Right";
        } else if (headRoll < -HEAD_ROLL_THRESHOLD) {
            // Negative Roll typically means head tilted to the user's left shoulder
            return "Head Rolled Left";
        }

        // If no significant head turn or tilt is detected
        return "Looking Straight";
    }

    /**
     * Detect the drowsiness
     * @param face
     * @return
     */
    public static boolean isDrowsy(Face face) {

        Float leftProb = face.getLeftEyeOpenProbability();
        Float rightProb = face.getRightEyeOpenProbability();

        Log.d("Face", "Face: " + face);
        Log.d("EyeProb", "Left: " + leftProb);
        Log.d("EyeProb", "Right: " + leftProb);

        // If both eyes are closed, consider the person as drowsy
        // 1: eye is fully open
        // 0: eye is fully closed

        boolean isLeftEyeSleep = leftProb != null && leftProb < AppCts.Thresholds.LEFT_EYE_CLOSED_THRESHOLD;
        boolean isRightEyeSleep = rightProb != null && rightProb < AppCts.Thresholds.RIGHT_EYE_CLOSED_THRESHOLD;

        return isLeftEyeSleep || isRightEyeSleep;
    }
}
