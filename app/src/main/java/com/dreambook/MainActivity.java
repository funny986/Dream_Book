package com.dreambook;

import android.annotation.SuppressLint;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import dataBase.*;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dataBase.Base.setDataBase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static MeaningDatabase database;
//    public static NestedScrollView nestedScrollView;


    public static final String APP_PREFERENCE = "launch";
    public static final String APP_PREFERENCE_COUNT = "count";
    private SharedPreferences preferences;
    private boolean count;
    private final int VERSION = 1;

    public BottomNavigationView bottomNavigationView;
    public FloatingActionButton fab;
    public Toolbar toolbar;

    @Override
    public void onSaveInstanceState(Bundle outSt) {
        super.onSaveInstanceState(outSt);
    }

    protected void onStart(){
        super.onStart();
        if (preferences.contains(APP_PREFERENCE_COUNT)) {
            count = preferences.getBoolean(APP_PREFERENCE_COUNT, false);
        }
        if (!count ) {
            setDataBase(database);
            count = true;
        }
    }

//    protected void onResume(){
//        super.onResume();
//        if (preferences.contains(APP_PREFERENCE_COUNT)) {
//            count = preferences.getBoolean(APP_PREFERENCE_COUNT, false);
//        }
//        if (!count ) {
//            setDataBase(database);
//            count = true;
//        }
//    }

    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_PREFERENCE_COUNT, count);
        editor.apply();
    }

    protected void onDestroy(){
        super.onDestroy();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                removeActiveFragments();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public static NavController navController;

    public static NavHostFragment hostFragment;

    public void setUpNavigation(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        hostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assert hostFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView, hostFragment.getNavController());
        NavigationUI.setupWithNavController(toolbar, hostFragment.getNavController());
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.nav_notes);
    }

    @Override
    public void onClick(View view) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Записать ");
        navController.navigate(R.id.nav_record);
    }

    public BottomNavigationView.OnNavigationItemSelectedListener listener(){
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          switch (item.getItemId()) {
            case R.id.action_notes:
                navController.navigate(R.id.nav_notes);
                fab.setVisibility(View.VISIBLE);
                break;
            case R.id.action_means:
                navController.navigate(R.id.nav_means);
                break;
            case R.id.action_setting:
                break;
        }
        return true;
    }};
    }

    public  static BottomNavigationView.OnNavigationItemSelectedListener getListnr() {
        return listnr;
    }

    public static void setListnr(BottomNavigationView.OnNavigationItemSelectedListener listnr) {
        MainActivity.listnr = listnr;
    }

    public static BottomNavigationView.OnNavigationItemSelectedListener listnr;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = App.getInstance().getDatabase();
        preferences = getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        nestedScrollView = findViewById(R.id.scrolling_content);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setUpNavigation();
             setListnr(listener());
             listnr = listener();
             bottomNavigationView.setOnNavigationItemSelectedListener(listnr);
        fab = findViewById(R.id.fab);

//        setDataBase(database);///////////////////////////////////////////
        fab.setOnClickListener(this);
    }

}

