package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.dreambook.MainActivity.database;

public class WordMeanFragment extends Fragment {

    public WordMeanFragment() {
    }

    public View view, itemSearch;
    public Toolbar toolbar;

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_word_mean, container, false);
        TextView interpretation = view.findViewById(R.id.mean_tv);
        assert getArguments() != null;
        String word = getArguments().getString("word_mean");
        word = word.toLowerCase();
        String mean = database.wordsDao().getMean(word);
        interpretation.setText(mean);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        itemSearch = toolbar.findViewById(R.id.search_in);
        toolbar.removeView(itemSearch);
        int margin = getResources().getDimensionPixelOffset(R.dimen.margin_start_wordmean);
        toolbar.setTitleMarginStart(margin);
        toolbar.setTitle(word);
    return view;
    }

}