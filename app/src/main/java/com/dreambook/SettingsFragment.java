package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.dreambook.MainActivity.AUTOR_GENDER;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, GenderSet {

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
        AppBarLayout appBarLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        Toolbar toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        SearchView searchView = getActivity().findViewById(R.id.search_in);
        searchView.setVisibility(View.INVISIBLE);
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
}