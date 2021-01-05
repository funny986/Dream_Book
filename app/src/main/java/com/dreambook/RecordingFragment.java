package com.dreambook;

import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.appbar.AppBarLayout;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RecordingFragment extends Fragment {

    private View view;
    private EditText nameNote, record;
    private String dateStr;

    public RecordingFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        AppBarLayout appBarLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.sorting);
        item.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:

                        RecordingFragmentDirections.ActionRecordToReady action =
                RecordingFragmentDirections.actionRecordToReady(
                        nameNote.getText().toString(),
                        record.getText().toString(),
                        dateStr);
                NavHostFragment.findNavController(RecordingFragment.this)
                                .navigate(action);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recording, container, false);
        nameNote = view.findViewById(R.id.name_note);
        record = view.findViewById(R.id.record_et);
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        dateStr = df.format(date);
        TextView tvDate = view.findViewById(R.id.date_tv);
        tvDate.setText(dateStr);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}