package com.duyvukim.drowsinessalertsystem.camera;

import static androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.duyvukim.drowsinessalertsystem.MainActivity;
import com.duyvukim.drowsinessalertsystem.databinding.ActivityCameraBinding;
import com.duyvukim.drowsinessalertsystem.utils.ScreenShot;

public class CameraActivity extends AppCompatActivity implements ICameraContract.View {

    // =========================================
    // === Fields
    // =========================================

    private ActivityCameraBinding binding;
    private ICameraContract.Presenter presenter;

    // =========================================
    // === Lifecycle
    // =========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //BackPressed Logic
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Custom logic here
                // For example, show a confirmation dialog
                binding.quittingCard.setVisibility(View.VISIBLE);
                binding.noButton.setOnClickListener(v->binding.quittingCard.setVisibility(View.GONE));
                binding.yesButton.setOnClickListener(v->{
                    showLoading();
                    Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        });

        // Setup Sounds

        // Setup bindings
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.attentionCard.setVisibility(View.VISIBLE);

        setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.gotItButton.setOnClickListener(v->{
            binding.attentionCard.setVisibility(View.GONE);
            // Setup presenter
            presenter = new CameraPresenter(this, this);

            // Start the camera stream
            presenter.startCamera(binding.previewView, this);
        });
    }

    // =========================================
    // === Methods
    // =========================================

    @Override
    public void showAlert() {
        Toast.makeText(this, "Alert!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String msg) {
        binding.statusText.setText(msg);
        binding.statusText.setTextColor(Color.RED);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        ScreenShot.capture(this, new ScreenShot.ScreenShotCallback() {
            @Override
            public void onScreenShot(Bitmap bitmap) {
                Log.d("ScreenShot", "Bitmap:" + bitmap.toString());
            }

            @Override
            public void onError(Exception e) {
                //TODO: handle error
            }
        });
    }

    @Override
    public void updateFaceOverlaySourceInfo(int imageWidth, int imageHeight, boolean isFrontCamera) {

    }

    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);
            }
        },3000);
    }
}