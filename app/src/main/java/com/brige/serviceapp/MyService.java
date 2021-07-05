package com.brige.serviceapp;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;

public class MyService extends Service {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

//        mediaPlayer =  MediaPlayer.create(this, R.raw.miracles);
//        mediaPlayer.setLooping(false);




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        if(intent.hasExtra("longID")){
            long id = (long) intent.getExtras().get("longID");
            Uri contentUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);


            try {
                mediaPlayer.setDataSource(getApplicationContext(), contentUri);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        }
        else{
            mediaPlayer.stop();
        }



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        mediaPlayer.stop();

    }

    public void stop(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
