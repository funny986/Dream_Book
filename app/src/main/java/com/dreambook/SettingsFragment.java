package com.dreambook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.*;
import androidx.transition.Fade;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.dreambook.MainActivity.*;
import static com.dreambook.R.style.MyAlertDialogTheme;

public class SettingsFragment extends Fragment {

    public SettingsFragment(){}

    @Override
    public void onSaveInstanceState(@NotNull Bundle outSt) {
        super.onSaveInstanceState(outSt);
    }

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

        private Activity activity;

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
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
            BottomNavigationView bottomNavigationView = Objects.requireNonNull(getActivity())
                    .findViewById(R.id.bottom_navigation);
            Menu menu = bottomNavigationView.getMenu();
            MenuItem item = menu.getItem(2);
            item.setChecked(true);
            FloatingActionButton fab = activity.findViewById(R.id.fab);
            fab.setVisibility(View.INVISIBLE);
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
            Preference exit = getPreferenceScreen().findPreference("exit");
            assert exit != null;
            exit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                        leaveApp();
                    return false;
                }
            });
        }

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
            if (key.equals("darktheme")){
                SwitchPreferenceCompat switchPreference = getPreferenceScreen().findPreference("darktheme");
                assert switchPreference != null;
                boolean switchDark = switchPreference.isChecked();
                    ((PrefSets) activity).themeSet(switchDark);
                if (switchPreference.isChecked()){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                   else {
                       AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                   }
                activity.recreate();
            }
        }

        private void leaveApp(){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder
                    .setTitle("Выйти из приложения")
                    .setIcon(R.drawable.ic_exit_to_app)
                    .setCancelable(true)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(intent, 1);
                        }
                    })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .getContext()
                    .setTheme(MyAlertDialogTheme);
            builder.create();
            builder.show();
        }
    }
}