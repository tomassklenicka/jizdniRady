package com.example.smartrady_final;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.smartrady_final.dummy.DummyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class GUI extends AppCompatActivity implements FavouritesFragment.OnListFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener{

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    final Fragment mapFrag = new MapFragment();

    final FragmentManager fragManager = getSupportFragmentManager();
    Fragment selected = mapFrag;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_u_i);

        //horni toolbar
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        //nastaveni udalosti bottom navigation baru


        fragManager.beginTransaction().replace(R.id.fragment_container, selected).commit();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //vyber moznosti z horniho toolbaru
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //vyber moznosti z bottom navigation baru
        switch(item.getItemId()){
            case R.id.action_map:
                selected = mapFrag;
                break;

        }
        fragManager.beginTransaction().replace(R.id.fragment_container, selected).commit();
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}