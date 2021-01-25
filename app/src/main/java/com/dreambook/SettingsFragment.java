package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.*;
import androidx.transition.Fade;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dreambook.MainActivity.*;

public class SettingsFragment extends Fragment {

    public SettingsFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setSharedElementEnterTransition(new DetailsTransition());
        this.setEnterTransition(new Fade());
        setExitTransition(new Fade());
        this.setSharedElementReturnTransition(new DetailsTransition());

        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    public static class PreferenceFragment extends PreferenceFragmentCompat implements
            SharedPreferences.OnSharedPreferenceChangeListener, PrefSets{

        @Override
        public void genderSet(int gender) {
                    }
        @Override
        public void themeSet(boolean theme) {
        }

        @Override
        public int getAutorGender() {
            return 0;
        }

        @Override
        public boolean getCustomTheme() {
            return false;
        }

        @Override
        public void onResume() {
            super.onResume();
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
            FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
            fab.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            ListPreference listPreference = getPreferenceScreen().findPreference(AUTOR_GENDER);
            int ind = ((PrefSets) activity).getAutorGender();
            if (listPreference != null) {
                listPreference.setValueIndex(ind);
                listPreference.setNegativeButtonText("Отмена");
            }
            Preference info = getPreferenceScreen().findPreference("info");
            assert info != null;
            info.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    assert getParentFragment() != null;
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.nav_info);
                    return false;
                }
            });
        }

        Activity activity;

        @Override
        public void onAttach(@NotNull Context context) {
            super.onAttach(context);
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
        }

        public PreferenceFragment(){
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @NotNull String key) {
            if (key.equals("gender")) {
                Resources res = getResources();
                String[] strings = res.getStringArray(R.array.gender_style);
                String g = sharedPreferences.getString(key, strings[0]);
                if (g.equals(strings[0]))
                    ((PrefSets) activity).genderSet(0);
                if (g.equals(strings[1]))
                    ((PrefSets) activity).genderSet(1);
                if (g.equals(strings[2]))
                    ((PrefSets) activity).genderSet(2);
            }
//            if (key.equals("darktheme")){
//                SwitchPreferenceCompat switchPreference = getPreferenceScreen().findPreference("darktheme");
//                assert switchPreference != null;
//                boolean switchDark = switchPreference.isChecked();
//                    ((PrefSets) activity).themeSet(switchDark);
//                    activity.finish();
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                    activity.startActivity(intent);
//            }
        }

    }
}