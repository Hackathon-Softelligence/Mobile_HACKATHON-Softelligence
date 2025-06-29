package com.duyvukim.drowsinessalertsystem;

import static androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.duyvukim.drowsinessalertsystem.camera.CameraActivity;
import com.duyvukim.drowsinessalertsystem.databinding.ActivityMainBinding;
import com.duyvukim.drowsinessalertsystem.utils.AppCts;
import com.duyvukim.drowsinessalertsystem.utils.PassTextWatcher;
import com.duyvukim.drowsinessalertsystem.utils.SoundAlertPlayer;

public class MainActivity extends AppCompatActivity {

    // ====================================
    // === Fields
    // ====================================

    private ActivityMainBinding binding;
    private String pass;

    // ====================================
    // === Lifecycles
    // ====================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Custom logic here
                // For example, show a confirmation dialog
                binding.quittingCard.setVisibility(View.VISIBLE);
                binding.noButton.setOnClickListener(v -> binding.quittingCard.setVisibility(View.GONE));
                binding.yesButton.setOnClickListener(v -> finishAffinity());
            }
        });

        checkAndRequestCameraPermission();
        checkAndRequestNotificationPermission();
        setUpPassInput();

        SoundAlertPlayer player = new SoundAlertPlayer();

        player.init(this);
        binding.joinExamButton.setOnClickListener(v -> {
            if (pass == null || pass.isEmpty()) setUpError();

            if (pass.equals(AppCts.Identities.EXAM_CODE)) {
                showLoading();
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            } else {
                Toast.makeText(this, "Code not found!", Toast.LENGTH_SHORT).show();
                clearInput();
            }


//            player.playSound();
        });

    }

    private void setUpPassInput() {
        binding.digit1.addTextChangedListener(new PassTextWatcher(binding.digit1, binding.digit2));
        binding.digit2.addTextChangedListener(new PassTextWatcher(binding.digit2, binding.digit3));
        binding.digit3.addTextChangedListener(new PassTextWatcher(binding.digit3, binding.digit4));
        binding.digit4.addTextChangedListener(new PassTextWatcher(binding.digit4, binding.digit5));
        binding.digit5.addTextChangedListener(new PassTextWatcher(binding.digit5, binding.digit6));
        binding.digit6.addTextChangedListener(new PassTextWatcher(binding.digit6, null) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (s.length() == 1) {
                    pass = getOtpCode(); // Gọi phương thức để lấy toàn bộ mã
//                    Toast.makeText(MainActivity.this, "Mã code: " + pass, Toast.LENGTH_SHORT).show();
                    // Tại đây, bạn có thể gửi mã này đi xác thực, hoặc thực hiện hành động tiếp theo
                }
            }
        });
    }

    private String getOtpCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.digit1.getText().toString());
        sb.append(binding.digit2.getText().toString());
        sb.append(binding.digit3.getText().toString());
        sb.append(binding.digit4.getText().toString());
        sb.append(binding.digit5.getText().toString());
        sb.append(binding.digit6.getText().toString());
        return sb.toString();
    }

    private void setUpError() {
        binding.digit1.setError("");
        binding.digit2.setError("");
        binding.digit3.setError("");
        binding.digit4.setError("");
        binding.digit5.setError("");
        binding.digit6.setError("");
    }

    private void clearInput() {

        binding.digit1.setText("");
        binding.digit2.setText("");
        binding.digit3.setText("");
        binding.digit4.setText("");
        binding.digit5.setText("");
        binding.digit6.setText("");

        // Tùy chọn: Chuyển focus về ô đầu tiên sau khi xóa
        binding.digit1.requestFocus();
    }

    // ====================================
    // === Camera Permission
    // ====================================

    // --- Camera Permission ---
    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Camera permission GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Camera permission DENIED", Toast.LENGTH_SHORT).show();
                    // Handle the case where the user denies the permission
                }
            });

    private void checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // Explain to the user why you need the permission
                Toast.makeText(this, "Camera permission is needed to take pictures.", Toast.LENGTH_LONG).show();
                // Then, request the permission
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            } else {
                // Directly request the permission
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        }
    }

    // --- Notification Permission (Android 13+) ---
    private final ActivityResultLauncher<String> requestNotificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, "Notification permission DENIED", Toast.LENGTH_SHORT).show();
                    // Handle the case where the user denies the permission
                }
            });
    // --- Notification Permission Logic ---

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // TIRAMISU is Android 13 (API 33)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Notification permission already granted", Toast.LENGTH_SHORT).show();
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // Explain to the user why you need the permission
                    Toast.makeText(this, "Notification permission is needed to send you updates.", Toast.LENGTH_LONG).show();
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                } else {
                    // Directly request the permission
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        }
//        else {
//            // For Android 12 and below, POST_NOTIFICATIONS permission is not needed (it's granted by default)
//            Toast.makeText(this, "Notifications are enabled (pre-Android 13)", Toast.LENGTH_SHORT).show();
//        }
    }


    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);
            }
        }, 3000);
    }
}