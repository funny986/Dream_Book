package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import dataBase.Words;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.dreambook.MainActivity.*;

public class InterpretationFragment extends Fragment {

    public InterpretationFragment(){}

    private View view;
    private AppBarLayout appBarLayout;
    private SearchView searchView;
    private Toolbar toolbar;
    private Activity activity;

    private List<Words> wordsList;

    public String note;
    private int gender;

    @Override
    public void onResume() {
        super.onResume();
        appBarLayout.setExpanded(false);
//        appBarLayout.setVisibility(View.INVISIBLE);
        searchView.setVisibility(View.INVISIBLE);
        gender = activity
                .getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE)
                .getInt(AUTOR_GENDER, 0);

    }
    @Override
    public void onPause() {
        super.onPause();
        appBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof Activity){
            activity = (Activity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        appBarLayout = requireActivity().findViewById(R.id.app_bar);
        searchView = getActivity().findViewById(R.id.search_in);
        setHasOptionsMenu(true);
        FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
//        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(null);
        wordsList = database.wordsDao().getAllWordsGender(gender);
    }

    public void interpetation(String note){
        TextView interpritate = view.findViewById(R.id.interpretation_tv);
        interpritate.setText(note);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_interpretation, container, false);
        if (getArguments() != null) {
//            note = getArguments().getString("note");
            int id = getArguments().getInt("noteID");
            note = database.notesDao()
                    .getNoteById(id)
                    .getNote();
            interpetation(note);
        }
        return view;
    }
}