package com.example.noyaactual;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.media.AudioManager;
import android.media.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

public class Recording {

    private MediaRecorder recorder;
    public Recording() {
        recorder = new MediaRecorder();
    }

    public void startRecording() {

        File file = new File(Environment.getExternalStorageDirectory() + "/stt.mp3");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setAudioSamplingRate(44100);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioEncodingBitRate(16);
            recorder.prepare();
            recorder.start();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;
                Thread.sleep(3000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilePath(){
        return Environment.getExternalStorageDirectory() + "/stt.mp3";
    }
}

