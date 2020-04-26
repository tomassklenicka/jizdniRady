package com.example.jizdnirady;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.jizdnirady.dummy.DummyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class GUI extends AppCompatActivity implements FavouritesFragment.OnListFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener{

    final Fragment mapFrag = new MapFragment();
    final Fragment favFrag  = new FavouritesFragment();
    final Fragment searchFrag = new SearchFragment();
    final Fragment moreFrag = new MoreFragment();
    final FragmentManager fragManager = getSupportFragmentManager();
    Fragment selected = searchFrag;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //horni toolbar
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        //nastaveni udalosti bottom navigation baru
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_menu);
        bottomNav.setOnNavigationItemSelectedListener(this);

        fragManager.beginTransaction().replace(R.id.fragment_container, selected).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //nastaveni menu horniho toolbaru
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.topbar, menu);
        return true;
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
            case R.id.action_favorites:
                selected = favFrag;
                break;
            case R.id.action_search:
                selected = searchFrag;
                break;
            case R.id.action_more:
                selected = moreFrag;
                break;
        }
        fragManager.beginTransaction().replace(R.id.fragment_container, selected).commit();
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}