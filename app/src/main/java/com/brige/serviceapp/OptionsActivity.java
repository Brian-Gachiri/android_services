package com.brige.serviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

public class OptionsActivity extends AppCompatActivity {
    
    MaterialButton btnSongs, btnRecordAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        
        btnRecordAudio = findViewById(R.id.btn_record_audio);
        btnSongs = findViewById(R.id.btn_song_list);
        
        btnSongs.setOnClickListener(view -> goToSongs());

        btnRecordAudio.setOnClickListener(view -> recordAudio());
    }

    private void goToSongs() {
        Intent  intent = new Intent(OptionsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void recordAudio() {
        Intent  intent = new Intent(OptionsActivity.this, AudioRecordTest.class);
        startActivity(intent);
    }
}
