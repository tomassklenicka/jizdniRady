package com.example.smartrady_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Prihlaseni_main extends AppCompatActivity {

    Button registrace_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prihlaseni_main);
        getSupportActionBar().setTitle("Přihášení");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registrace_btn = findViewById(R.id.registrace_button);

        registrace_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oteriRegistraci();
            }
        });

    }


    public void oteriRegistraci(){
        Intent intent = new Intent(this,Registrace_main.class);
        startActivity(intent);

    }
}

