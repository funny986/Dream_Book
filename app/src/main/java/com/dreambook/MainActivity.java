package com.dreambook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.*;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.dreambook.dataBase.App;
import com.dreambook.dataBase.MeaningDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

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
    private boolean firstStart = true;
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
    public void onSaveInstanceState(@NotNull Bundle outSt) {
        super.onSaveInstanceState(outSt);
    }

    public void startSetData(){
        data = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setDataBase(database);
                }
                catch (IllegalStateException ignored){}
            }
        });
        data.start();
    }

    protected void onStart() {
        super.onStart();
        firstStart = preferences.getBoolean(APP_PREFERENCE_COUNT, true);
        if (firstStart) {
            firstStart = false;
            setPreferences();
            Intent intent = new Intent(MainActivity.this, Splash.class);
            MainActivity.this.startActivity(intent);
            requestRecordAudioPermission();
        }
        try {
            Class.forName("dalvik.system.CloseGuard")
                    .getMethod("setEnabled", boolean.class)
                    .invoke(null, true);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPreferences(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_PREFERENCE_COUNT, firstStart);
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
        darkTheme = theme;
        getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE).edit()
                .putBoolean(THEME_DARK, theme)
                .apply();
    }

    @Override
    public int getAutorGender() {
        return autorgender;
    }


    protected void onPause() {
        super.onPause();
        setPreferences();
    }

    public Thread data;

    protected void onDestroy() {
        super.onDestroy();
    }


    protected void onRestart() {
        super.onRestart();
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
        preferences = getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (preferences.contains(AUTOR_GENDER)) {
            autorgender = preferences.getInt(AUTOR_GENDER, 0);
        }
        if (preferences.contains(THEME_DARK)) {
            darkTheme = preferences.getBoolean(THEME_DARK, false);
        }
        if (savedInstanceState == null) {
//                    // Ночной режим не активен
                    if (darkTheme) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        recreate();
                    }
//                    // Ночной режим
                    if (!darkTheme) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        recreate();
                    }
            database = App.getInstance().getDatabase();
            startSetData();
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setUpNavigation();
        setListnr(listener());
        listnr = listener();
        bottomNavigationView.setOnNavigationItemSelectedListener(listnr);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                navController.navigate(R.id.nav_record);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == 1){
            data.interrupt();
            database.close();
            setPreferences();
            this.finish();
            Intent intent2 = new Intent(Intent.ACTION_MAIN);
            intent2.addCategory(Intent.CATEGORY_HOME);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        }
    }
}



