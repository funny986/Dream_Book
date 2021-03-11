package com.dreambook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.*;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.dreambook.dataBase.App;
import com.dreambook.dataBase.MeaningDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import static com.dreambook.dataBase.Base.setDataBase;

public class MainActivity extends AppCompatActivity implements PrefSets {

    public static MeaningDatabase database;

    public static final String APP_PREFERENCE = "settings";
    public static final String APP_PREFERENCE_COUNT = "count";
    public static final String AUTOR_GENDER = "gender";
    public static final String BOX_STATE = "checkboxstate";
    public static final String THEME_DARK = "darkthemeswitch";

    public SharedPreferences preferences;
    private boolean count;
    public boolean darkTheme;

    public int autorgender, checkState;

    public BottomNavigationView bottomNavigationView;
    public FloatingActionButton fab;

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outSt) {
        super.onSaveInstanceState(outSt);
    }

    protected void onStart() {
        super.onStart();
        if (preferences.contains(APP_PREFERENCE_COUNT)) {
            count = preferences.getBoolean(APP_PREFERENCE_COUNT, false);
        }
        Thread data = new Thread(new Runnable() {
            @Override
            public void run() {
                setDataBase(database);
            }
        });
        data.start();
        if (preferences.contains(AUTOR_GENDER)) {
            autorgender = preferences.getInt(AUTOR_GENDER, 0);
        }
//        if (preferences.contains(THEME_DARK)) {
//            darkTheme = preferences.getBoolean(THEME_DARK, false);
//        }
    }

    public void setPreferences(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_PREFERENCE_COUNT, count);
        editor.putInt(AUTOR_GENDER, autorgender);
        editor.putInt(BOX_STATE, checkState);
        editor.putBoolean(THEME_DARK, darkTheme);
        editor.apply();
}
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void genderSet(int gender) {
        getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE).edit()
                .putInt(AUTOR_GENDER, gender)
                .apply();
        autorgender = gender;
    }

    @Override
    public void themeSet(boolean theme) {
        getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE).edit()
                .putBoolean(THEME_DARK, theme)
                .apply();
    }

    @Override
    public int getAutorGender() {
        return autorgender;
    }

    @Override
    public boolean getCustomTheme() {
        return darkTheme;
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }


    protected void onRestart() {
        super.onRestart();
        setPreferences();
    }

    @SuppressLint("StaticFieldLeak")
    public static NavController navController;

    public static NavHostFragment hostFragment;

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }

    public void setUpNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        hostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assert hostFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView, hostFragment.getNavController());
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.nav_notes);
    }

    public BottomNavigationView.OnNavigationItemSelectedListener listener() {
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
                        navController.navigate(R.id.nav_setting);
                        break;
                }
                return true;
            }
        };
    }

    public static BottomNavigationView.OnNavigationItemSelectedListener getListnr() {
        return listnr;
    }

    public static void setListnr(BottomNavigationView.OnNavigationItemSelectedListener listnr) {
        MainActivity.listnr = listnr;
    }

    public static BottomNavigationView.OnNavigationItemSelectedListener listnr;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
//        preferences = getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
//        darkTheme = preferences.getBoolean(THEME_DARK, false);
//        if (darkTheme) setTheme(R.style.Theme_CustomThemeDark);
//                else   setTheme(R.style.Theme_CustomTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestRecordAudioPermission();
        database = App.getInstance().getDatabase();
        preferences = getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setUpNavigation();
        setListnr(listener());
        listnr = listener();
        bottomNavigationView.setOnNavigationItemSelectedListener(listnr);
        fab = findViewById(R.id.fab);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                navController.navigate(R.id.nav_record);
            }
        });
    }
}



