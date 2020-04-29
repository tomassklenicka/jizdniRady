package com.example.smartrady_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Profil_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_main);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

