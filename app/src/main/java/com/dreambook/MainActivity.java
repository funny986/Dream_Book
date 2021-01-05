package com.dreambook;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.*;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.preference.ListPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Objects;

import dataBase.*;
import org.jetbrains.annotations.NotNull;

import static dataBase.Base.setDataBase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
                                                    GenderSet{

    public static MeaningDatabase database;

    public static final String APP_PREFERENCE = "settings";
    public static final String APP_PREFERENCE_COUNT = "count";
    public static final String AUTOR_GENDER = "gender";

    public SharedPreferences preferences;
    private boolean count;

    public int autorgender;

    private final int VERSION = 1;

    public Bundle args;
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
        if (preferences.contains(AUTOR_GENDER)) {
            autorgender = preferences.getInt(AUTOR_GENDER, 0);
        }
    }
    protected void onResume(){
        super.onResume();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_PREFERENCE_COUNT, count);
        editor.putInt(AUTOR_GENDER, autorgender);
        args.putInt(AUTOR_GENDER, autorgender);
        editor.apply();
    }

    @Override
    public void genderSet(int gender) {
        getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE).edit().putInt(AUTOR_GENDER, gender)
                .apply();
    }

    @Override
    public int getAutorGender() {
        return autorgender;
    }

    protected void onPause(){
        super.onPause();
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(APP_PREFERENCE_COUNT, count);
//        editor.putInt(AUTOR_GENDER, autorgender);
//        editor.apply();
    }

    protected void onDestroy(){
        super.onDestroy();
        database.close();
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
                navController.navigate(R.id.nav_means, args);
                break;
            case R.id.action_setting:
                navController.navigate(R.id.nav_setting);
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
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setUpNavigation();
             setListnr(listener());
             listnr = listener();
             bottomNavigationView.setOnNavigationItemSelectedListener(listnr);
        fab = findViewById(R.id.fab);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        fab.setOnClickListener(this);
        args = new Bundle();
    }
}

