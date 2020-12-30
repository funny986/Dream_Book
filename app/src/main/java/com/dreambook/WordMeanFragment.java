package com.dreambook;

import android.os.Bundle;
import android.view.*;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import static com.dreambook.MainActivity.database;

public class WordMeanFragment extends Fragment {

    public WordMeanFragment() {}
    public View view;
    private AppBarLayout appBarLayout;

    @Override
    public void onResume() {
        super.onResume();
        appBarLayout.setExpanded(false);
        appBarLayout.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onPause() {
        super.onPause();
        appBarLayout.setVisibility(View.VISIBLE);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        appBarLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.app_bar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_word_mean, container, false);
        TextView interpretation = view.findViewById(R.id.word_tv);
        assert getArguments() != null;
        String word = getArguments().getString("word_mean");
        word = word.toLowerCase();
        String mean = database.wordsDao().getWordMean(word).getMean();
        interpretation.setText(mean);
        SearchView searchView = getActivity().findViewById(R.id.search_in);
        searchView.setVisibility(View.INVISIBLE);
    return view;
    }
}