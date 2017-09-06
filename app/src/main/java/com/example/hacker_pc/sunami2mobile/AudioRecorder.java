package com.example.hacker_pc.sunami2mobile;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="http://www.benmccann.com">Ben McCann</a>
 */
public class AudioRecorder {

    MediaRecorder recorder = new MediaRecorder();
    String path;

    /**
     * Creates a new audio recording at the given path (relative to root of SD card).
     */
    public AudioRecorder(String path) {
        this.path = sanitizePath(path);

    }

    private String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += ".3gp";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
    }

    /**
     * Starts a new recording.
     */
    public void start(Context ctx) throws IOException {
        Toast.makeText(ctx, "recording audio", Toast.LENGTH_LONG).show();
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted.  It is " + state + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
    }

    /**
     * Stops a recording that has been previously started.
     */
    public void stop(Context ctx) {
        Toast.makeText(ctx, "done recording audio", Toast.LENGTH_LONG).show();
        try {
            recorder.stop();
            recorder.release();
        } catch (Exception k) {
            Toast.makeText(ctx, k.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
