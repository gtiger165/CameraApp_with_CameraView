package com.maestro.cameraappwithcameraview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = CameraActivity.class.getSimpleName();
    CameraView cv;
    ImageButton ibCamera, ibToggle, ibFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cv = findViewById(R.id.camera_view);
        ibCamera = findViewById(R.id.capturePicture);
        ibToggle = findViewById(R.id.toggleCamera);
        ibFlash = findViewById(R.id.flashCamera);

        ibCamera.setOnClickListener(this);
        ibToggle.setOnClickListener(this);
        ibFlash.setOnClickListener(this);

        cv.setLifecycleOwner(this);
        cv.addCameraListener(cl);
    }

    private CameraListener cl = new CameraListener() {
        @Override
        public void onPictureTaken(@NonNull PictureResult result) {
            super.onPictureTaken(result);

            PreviewActivity.pictureResult = result;
            startActivity(new Intent(CameraActivity.this, PreviewActivity.class));
        }

        @Override
        public void onCameraError(@NonNull CameraException exception) {
            super.onCameraError(exception);
            Toast.makeText(CameraActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCameraError: ", exception);
        }
    };

    @Override
    public void onClick(View v) {
        if (v == ibCamera) {
            Toast.makeText(this, "please wait.....", Toast.LENGTH_SHORT).show();
            cv.takePicture();
        }
        if (v == ibToggle) {
            switch (cv.toggleFacing()) {
                case BACK:
                    Toast.makeText(this, "Ganti ke kamera belakang", Toast.LENGTH_SHORT).show();
                    break;
                case FRONT:
                    Toast.makeText(this, "Ganti ke kamera depan", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        if (v == ibFlash) {
//            switch (cv.getFlash()) {
//                case ON:
//                    ibFlash.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_flash_on_24, null));
//                    break;
//                case OFF:
//                    ibFlash.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_flash_off_24, null));
//                    break;
//                case AUTO:
//            }
        }
    }
}