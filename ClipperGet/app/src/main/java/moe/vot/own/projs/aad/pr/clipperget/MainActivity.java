package moe.vot.own.projs.aad.pr.clipperget;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ClipperGet";
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private PreviewView previewView;
    private Button recordButton;
    private TextView statusText, timerText;
    
    private VideoCapture<Recorder> videoCapture;
    private Recording recording;
    private ProcessCameraProvider cameraProvider;
    private ExecutorService cameraExecutor;
    
    private boolean isRecording = false;
    
    private final ActivityResultLauncher<String[]> permissionsLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                var allGranted = true;
                for (Boolean isGranted : permissions.values())
                    if (!isGranted) {
                        allGranted = false;
                        break;
                    }

                if (allGranted)
                    startCamera();
                else {
                    Toast.makeText(this, "NO PERMISSION", Toast.LENGTH_LONG).show();
                    recordButton.setEnabled(false);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        if (allPermissionsGranted())
            startCamera();
        else
            permissionsLauncher.launch(REQUIRED_PERMISSIONS);
        
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private void initViews() {
        previewView = findViewById(R.id.cameraPreview);
        recordButton = findViewById(R.id.recordButton);
        statusText = findViewById(R.id.statusText);

        recordButton.setOnClickListener(v -> toggleRecording());
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS)
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                
                var preview = new Preview.Builder().build();
                
                var recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HD))
                        .build();
                videoCapture = VideoCapture.withOutput(recorder);
                
                var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);
                
                recordButton.setEnabled(true);
                statusText.setText("READY");
                
            } catch (ExecutionException | InterruptedException ex) {
                Log.e(TAG, "FAIL: CAM", ex);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void toggleRecording() {
        if (isRecording)
            stopRecording();
        else
            startRecording();

    }
    
    private void startRecording() {
        if (videoCapture == null) return;
        
        var contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, 
                "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis()) + ".mp4");
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/ClipperGet");
        
        MediaStoreOutputOptions outputOptions = new MediaStoreOutputOptions.Builder(
                getContentResolver(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues)
                .build();
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            recording = videoCapture.getOutput()
                    .prepareRecording(this, outputOptions)
                    .withAudioEnabled()
                    .start(ContextCompat.getMainExecutor(this), event -> {
                        if (event instanceof VideoRecordEvent.Start) {
                            isRecording = true;

                            recordButton.setText("END");
                            recordButton.setBackgroundResource(R.drawable.record_button_recording);
                            statusText.setText("ING");

                            Toast.makeText(this, "STARTED", Toast.LENGTH_SHORT).show();
                        } else if (event instanceof VideoRecordEvent.Finalize) {
                            VideoRecordEvent.Finalize finalizeEvent = (VideoRecordEvent.Finalize) event;
                            
                            if (finalizeEvent.hasError()) {
                                runOnUiThread(() -> {
                                    isRecording = false;
                                    recordButton.setText("REC");
                                    recordButton.setBackgroundResource(R.drawable.record_button_bg);
                                    statusText.setText("READY");
                                    var errorMsg = "ERR::" + finalizeEvent.getError();
                                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                });
                            } else {
//                                var videoUri = finalizeEvent.getOutputResults().getOutputUri();
                                runOnUiThread(() -> {
                                    isRecording = false;
                                    recordButton.setText("REC");
                                    recordButton.setBackgroundResource(R.drawable.record_button_bg);
                                    statusText.setText("READY");
                                    Toast.makeText(MainActivity.this, "SAVED", Toast.LENGTH_LONG).show();
                                });
                            }
                        }
                    });
        }
    }

    private void stopRecording() {
        if (recording != null && isRecording) {
            recording.stop();
            recording = null;
            
            statusText.setText("SAVING");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null)
            cameraExecutor.shutdown();
    }
}