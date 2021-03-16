package com.dreambook;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.Fade;

import java.util.Objects;

import static com.dreambook.MainActivity.*;

public class WordMeanFragment extends Fragment {

    public WordMeanFragment() {
    }

    public View view;

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
        this.setSharedElementEnterTransition(new DetailsTransition());
        this.setEnterTransition(new Fade());
        setExitTransition(new Fade());
        this.setSharedElementReturnTransition(new DetailsTransition());
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
        int gender = getArguments().getInt("gender");
        TextView title = view.findViewById(R.id.meanword_tv);
        title.setText(word);
        word = word.toLowerCase();
        String mean = database.wordsDao().getMean(word, gender);
        interpretation.setText(mean);
        Button btnBack = view.findViewById(R.id.button_exit);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            }
        });
    return view;
    }

}