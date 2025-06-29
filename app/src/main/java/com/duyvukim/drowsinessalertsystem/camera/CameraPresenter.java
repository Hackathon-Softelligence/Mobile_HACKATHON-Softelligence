package com.duyvukim.drowsinessalertsystem.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.camera.view.PreviewView;
import androidx.lifecycle.LifecycleOwner;

import com.duyvukim.drowsinessalertsystem.detection.FaceAnalyzer;
import com.duyvukim.drowsinessalertsystem.detection.IFaceAnalyzerCallbacks;
import com.duyvukim.drowsinessalertsystem.detection.IssuesDetector;
import com.duyvukim.drowsinessalertsystem.services.FirestoreLoggingsService;
import com.duyvukim.drowsinessalertsystem.utils.AppCts;
import com.duyvukim.drowsinessalertsystem.utils.SoundAlertPlayer;
import com.google.mlkit.vision.face.Face;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CameraPresenter implements ICameraContract.Presenter {

    // =========================================
    // === Fields
    // =========================================

    private ICameraContract.View view;
    private CameraFramesSource cameraFramesSource;

    private Context context;
    private SoundAlertPlayer soundAlertPlayer;
    private AtomicInteger frameClosedEyesCounter = new AtomicInteger(0);
    private AtomicInteger multiplePeopleFrameCounter = new AtomicInteger(0);
    private AtomicInteger headPoseProblemCounter = new AtomicInteger(0);
    private AtomicBoolean hasLoggedDrowsy = new AtomicBoolean(false);
    private AtomicBoolean hasLoggedMultiplePeople = new AtomicBoolean(false);
    private AtomicBoolean hasLoggedHeadPose = new AtomicBoolean(false);

    // =========================================
    // === Constructors
    // =========================================

    public CameraPresenter(ICameraContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.soundAlertPlayer = new SoundAlertPlayer();
        soundAlertPlayer.init(context);
    }

    // =========================================
    // === Methods
    // =========================================

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public void startCamera(PreviewView previewView, LifecycleOwner lifecycleOwner) {

        cameraFramesSource = new CameraFramesSource(previewView, lifecycleOwner, imageProxy -> {

            new FaceAnalyzer(new IFaceAnalyzerCallbacks() {
                @Override
                public void onFaceDetected(Face face) {
                    if (face == null) return;

                    // Detect isDrowsy
                    if (IssuesDetector.isDrowsy(face)) {
                        int count = frameClosedEyesCounter.incrementAndGet();

                        Log.d("EyeProb", "" + count);

                        if (count > AppCts.Thresholds.FRAMES_CLOSED_THRESHOLD && !hasLoggedDrowsy.get()) {

                            hasLoggedDrowsy.set(true);
                            view.showMessage("Drowsiness detected");

                            // firebase
                            FirestoreLoggingsService.logDetection(
                                    AppCts.FakeUser.USER_NAME,
                                    AppCts.FakeUser.USER_STUDENTCODE,
                                    AppCts.ProblemStatus.STATUS_IS_DROWSY,
                                    ""
                            );

                            // sound
                            soundAlertPlayer.playSound();
                        }
                    } else {
                        frameClosedEyesCounter.set(0);
                        hasLoggedDrowsy.set(false);
                    }

                    // Detect Head Pose Problem
                    if (!IssuesDetector.isHeadPoseProblem(face).isBlank()) {
                        int count = headPoseProblemCounter.incrementAndGet();

                        if (count > AppCts.Thresholds.FRAMES_HEAD_POSE_PROBLEM_THRESHOLD && !hasLoggedHeadPose.get()) {
                            hasLoggedHeadPose.set(true);
                            view.showMessage("Head pose problem detected");
                            FirestoreLoggingsService.logDetection(
                                    AppCts.FakeUser.USER_NAME,
                                    AppCts.FakeUser.USER_STUDENTCODE,
                                    AppCts.ProblemStatus.STATUS_IS_HEAD_PROBLEM,
                                    ""
                            );

                            soundAlertPlayer.playSound();
                        }
                    } else {
                        headPoseProblemCounter.set(0);
                        hasLoggedHeadPose.set(false);
                    }
                }

                @Override
                public void onMultipleFacesCountDetected(int facesCount) {
                    if (facesCount > 1) {
                        int count = multiplePeopleFrameCounter.incrementAndGet();

                        if (count > AppCts.Thresholds.FRAMES_MULTIPLE_PEOPLE_THRESHOLD && !hasLoggedMultiplePeople.get()) {
                            hasLoggedMultiplePeople.set(true);
                            view.showMessage("Number of people: " + facesCount);
                            FirestoreLoggingsService.logDetection(
                                    AppCts.FakeUser.USER_NAME,
                                    AppCts.FakeUser.USER_STUDENTCODE,
                                    AppCts.ProblemStatus.STATUS_MORE_THAN_ONE_PEOPLE,
                                    ""
                            );

                            soundAlertPlayer.playSound();
                        }


                    } else {
                        multiplePeopleFrameCounter.set(0);
                        hasLoggedMultiplePeople.set(false);
                    }
                }

                @Override
                public void onMultipleFacesDetected(List<Face> faces) {

                }

            }).analyzeImageFrame(imageProxy);
        });

        cameraFramesSource.start();
    }


}
