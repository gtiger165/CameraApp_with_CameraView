package com.maestro.cameraappwithcameraview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;

import java.io.File;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = PreviewActivity.class.getSimpleName();
    public static PictureResult pictureResult = null;
    ImageView ivResult;
    Button btnGetPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        PictureResult result = pictureResult;

        if (result == null) {
            Log.d(TAG, "onCreate: result null");
            finish();
            return;
        }

        ivResult = findViewById(R.id.imageResult);
        btnGetPath = findViewById(R.id.btnGetPath);

        btnGetPath.setOnClickListener(this);

        try {
            result.toBitmap(new BitmapCallback() {
                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    ivResult.setImageBitmap(bitmap);
                }
            });
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == btnGetPath) {
            String ext = "";

            switch (pictureResult.getFormat()) {
                case JPEG:
                    ext = "jpeg";
                    break;
                case DNG:
                    ext = "dng";
                    break;
            }

            File file = new File(getFilesDir(), "temp_picture." + ext);

            CameraUtils.writeToFile(pictureResult.getData(), file, new FileCallback() {
                @Override
                public void onFileReady(@Nullable File file) {
                    if (file != null) {
                        Intent i = new Intent(PreviewActivity.this, MainActivity.class);
                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                        i.putExtra("get_uri", uri.toString());
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(PreviewActivity.this, "Opps something went a fucking wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}