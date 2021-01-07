package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.dreambook.MainActivity.AUTOR_GENDER;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, GenderSet, MoveAddSearchItem {

    public View itemSearch;
    public Toolbar toolbar;

    @Override
    public void genderSet(int gender) {
    }

    @Override
    public int getAutorGender() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
//        itemSearch = toolbar.findViewById(R.id.search_in);
        delItemSearch(toolbar, itemSearch);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            moveAdd(toolbar, itemSearch);
        }
        catch (IllegalArgumentException ignored){};
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        try {
            moveAdd(toolbar, itemSearch);
        }
        catch (IllegalArgumentException ignored){};
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
          toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        itemSearch = toolbar.findViewById(R.id.search_in);
        delItemSearch(toolbar, itemSearch);
        toolbar.setTitle("Настройки");
        int margin = getResources().getDimensionPixelOffset(R.dimen.margin_start_setting);
        toolbar.setTitleMarginStart(margin);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        ListPreference listPreference = getPreferenceScreen().findPreference(AUTOR_GENDER);
        int ind = ((GenderSet)activity).getAutorGender();
        if (listPreference != null) {
            listPreference.setValueIndex(ind);
        }
    }

    Activity activity;

    @Override
    public void onAttach(@NotNull Context context){
        super.onAttach(context);
        if (context instanceof Activity){
            activity = (Activity) context;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @NotNull String key) {
        if (key.equals("gender")) {
            Resources res = getResources();
            String[] strings = res.getStringArray(R.array.gender_style);
            String g = sharedPreferences.getString(key, strings[0]);
            if (g.equals(strings[0]))
                ((GenderSet)activity).genderSet(0);
            if (g.equals(strings[1]))
                ((GenderSet)activity).genderSet(1);
            if (g.equals(strings[2]))
                ((GenderSet)activity).genderSet(2);
        }
    }

    @Override
    public void moveAdd(@NotNull Toolbar toolbar, View view) {
        toolbar.addView(view);
    }

    @Override
    public void delItemSearch(Toolbar toolbar, View view) {
        toolbar.removeView(view);
    }
}