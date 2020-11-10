package com.maestro.cameraappwithcameraview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int BUFFER_SIZE = 1024 * 8;
    private String TAG = MainActivity.class.getSimpleName();
    TextView tvPath;
    Button btnCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvPath = findViewById(R.id.photoPath);
        btnCamera = findViewById(R.id.btnCamera);

        btnCamera.setOnClickListener(this);

        if (getIntent() != null) {
            if (getIntent().getStringExtra("get_uri") != null) {
                Uri uri = Uri.parse(getIntent().getStringExtra("get_uri"));
                String path = getFilePathFromUri(this, uri);
//                String[] filePathColumn = {MediaStore.Images.Media._ID};
//
//                Cursor cursor = getContentResolver().query(uri, filePathColumn,
//                        null, null, null);
//                assert cursor != null;
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
//
//                String mediaPath = cursor.getString(columnIndex);
//                Log.d(TAG, "onActivityResult: media path -> " + mediaPath);

                tvPath.setText("Uri Path : " + path);
//                cursor.close();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnCamera) {
            startActivity(new Intent(this, CameraActivity.class));
        }
    }

    public static String getFilePathFromUri(Context c, Uri u) {
        String fileName = getFileName(u);
        File directory = new File(c.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/absen_foto");

        if (!directory.exists())
            directory.mkdirs();

        if (!TextUtils.isEmpty(fileName)) {
            File copiedFile = new File(directory + File.separator + fileName);
            copy(c, u, copiedFile);
            return copiedFile.getAbsolutePath();
        } else {
            return null;
        }
    }

    private static void copy(Context c, Uri u, File copiedFile) {
        try {
            InputStream inputStream = c.getContentResolver().openInputStream(u);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(copiedFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static int copystream(InputStream input, OutputStream output) throws Exception, IOException{
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }

    private static String getFileName(Uri u) {
        if (u == null) return null;
        String fileName = null;
        String path = u.getPath();
        assert path != null;
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }
}