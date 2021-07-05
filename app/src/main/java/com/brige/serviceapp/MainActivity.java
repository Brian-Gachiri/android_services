package com.brige.serviceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private static final int REQUEST_CODE = 4;
    Button buttonStart, buttonStop, buttonNext;
    public ArrayList<Song> songlist = new ArrayList<>();
    RecyclerView songsRecyclerview;
    DisplaySongsAdapter displaySongsAdapter;
    public static String CHANNEL_ID = "com.brige.serviceapp.CHANNEL_ID";
    private String CHANNEL_NAME = "com.brige.serviceapp.CHANNEL_NAME";
    public static String CHANNEL_DESCRIPTION = "com.brige.serviceapp.CHANNEL_DESCRIPTION";


    @Override
    protected void onStart() {
        super.onStart();
        contentProvider();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        buttonStart = findViewById(R.id.buttonStart);
//        buttonStop = findViewById(R.id.buttonStop);
//        buttonNext =  findViewById(R.id.buttonNext);
//
//        buttonStart.setOnClickListener(this);
//        buttonStop.setOnClickListener(this);
//        buttonNext.setOnClickListener(this);
        createNotificationChannel();

        songsRecyclerview = findViewById(R.id.songs_recylcerView);
        songsRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        displaySongsAdapter = new DisplaySongsAdapter(MainActivity.this, songlist);
        songsRecyclerview.setAdapter(displaySongsAdapter);


        verifyPermissions();


    }
//    public void onClick(View src) {
//        switch (src.getId()) {
//            case R.id.buttonStart:
//
//                contentProvider();
//
////                                startService(new Intent(this, MyService.class));
//                break;
//            case R.id.buttonStop:
//                stopService(new Intent(this, MyService.class));
//                break;
//            case R.id.buttonNext:
//                Intent intent=new Intent(this,NextPage.class);
//                startActivity(intent);
//                break;
//        }
//    }

    private void verifyPermissions(){
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0])== PackageManager.PERMISSION_GRANTED
               ){
            Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityCompat.requestPermissions(this,
                    permissions,REQUEST_CODE);
        }
    }

    public void contentProvider(){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
            Toast.makeText(this, "Query Failed", Toast.LENGTH_SHORT).show();
        } else if (!cursor.moveToFirst()) {
            // no media on the device
            Toast.makeText(this, "No media on this device", Toast.LENGTH_SHORT).show();

        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            Toast.makeText(this, "We're here", Toast.LENGTH_SHORT).show();

            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                Song song = new Song();
                song.id = thisId;
                song.name = thisTitle;

                songlist.add(song);
                displaySongsAdapter.notifyDataSetChanged();
                // ...process entry...
            } while (cursor.moveToNext());
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}