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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import dataBase.Words;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.dreambook.MainActivity.*;

public class InterpretationFragment extends Fragment implements MoveAddSearchItem{

    public InterpretationFragment(){}

    private View view, itemsearch;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private SearchView searchView;
    private Activity activity;

    private List<Words> wordsList;

    public String note, title;
    private int gender;

    @Override
    public void onResume() {
        super.onResume();
        delItemSearch(toolbar, itemsearch);
        gender = activity
                .getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE)
                .getInt(AUTOR_GENDER, 0);

    }
    @Override
    public void onPause() {
        super.onPause();
        this.onDestroyView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        moveAdd(toolbar, itemsearch);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        moveAdd(toolbar, itemsearch);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof Activity){
            activity = (Activity) context;
        }
    }

    @Override
    public void moveAdd(@NotNull Toolbar toolbar, View view) {
        try {
            toolbar.addView(view);
        }
        catch (IllegalStateException | IllegalArgumentException ignore){};
    }

    @Override
    public void delItemSearch(@NotNull Toolbar toolbar, View view) {
        try {
            toolbar.removeView(view);
        }
        catch (IllegalStateException | IllegalArgumentException ignore){};

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        wordsList = database.wordsDao().getAllWordsGender(gender);
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        itemsearch = toolbar.findViewById(R.id.search_in);
        int margin = getResources().getDimensionPixelOffset(R.dimen.margin_start_interpretation);
        toolbar.setTitleMarginStart(margin);
        if (getArguments() != null) {
            int id = getArguments().getInt("noteID");
            note = database.notesDao()
                    .getNoteById(id)
                    .getNote();
            title = database.notesDao()
                    .getNoteById(id)
                    .getNameNote();
            toolbar.setTitle(title);
        }
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
        interpetation(note);

        return view;
    }


}