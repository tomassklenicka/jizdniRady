package com.example.smartrady_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    RecyclerView mVysledky;
    ArrayList<String> listVysledku;
    LinearLayoutManager layoutManagerSkupina;
    GroupAdp adapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setTitle("Výsledky");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mVysledky = findViewById(R.id.vysledky);

        listVysledku = new ArrayList<>();

        for (int i = 1; i <=10;i++){

            listVysledku.add("Group");

            adapterGroup = new GroupAdp(Main2Activity.this,listVysledku);

            layoutManagerSkupina = new LinearLayoutManager(this);

            mVysledky.setLayoutManager(layoutManagerSkupina);
            mVysledky.setAdapter(adapterGroup);

        }




    }
}
