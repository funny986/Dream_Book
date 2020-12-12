package com.dreambook;

import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RecordingFragment extends Fragment {

    BottomNavigationView bnView;

    public RecordingFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        AppBarLayout appBarLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recording, container, false);
        final EditText nameNote = view.findViewById(R.id.name_note);
        final EditText record = view.findViewById(R.id.record_et);
        final FloatingActionButton floatingButton = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
        SearchView searchView = getActivity().findViewById(R.id.search_in);
        searchView.setVisibility(View.INVISIBLE);

        floatingButton.setVisibility(View.VISIBLE);
        floatingButton.setImageResource(R.drawable.ic_done_24);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordingFragmentDirections.ActionRecordToReady action =
                        RecordingFragmentDirections.actionRecordToReady(
                                nameNote.getText().toString(),
                                record.getText().toString());
                NavHostFragment.findNavController(RecordingFragment.this)
                        .navigate(action);
                floatingButton.setVisibility(View.INVISIBLE);

            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bnView = getActivity().findViewById(R.id.bottom_navigation);
        bnView.getMenu()
                .getItem(0)
                .setIcon(R.drawable.ic_baseline_event_note_24)
                .setTitle(R.string.my_dreams);
        bnView.getMenu()
                .getItem(1)
                .setIcon(R.drawable.ic_translate_24)
                .setTitle(R.string.dream_means);
        bnView.getMenu()
                .getItem(2)
                .setIcon(R.drawable.ic_settings_24)
                .setTitle(R.string.setting);
        bnView.setOnNavigationItemSelectedListener(MainActivity.listnr);
    }
}