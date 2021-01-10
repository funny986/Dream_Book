package com.dreambook;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;
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
    private Toolbar toolbar;
    private View itemSearch;

    public RecordingFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        int margin = getResources().getDimensionPixelOffset(R.dimen.margin_start_recording);
        toolbar.setTitleMarginStart(margin);
        nameNote.requestFocus();
        setSoftInput();
    }

    @Override
    public void onPause() {
        super.onPause();
        nameNote.clearFocus();
        hideSoftInput();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.main, menu);
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
            case android.R.id.home:
                 toolbar.addView(itemSearch);
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
        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitleMarginStart(380);
        itemSearch = toolbar.findViewById(R.id.search_in);
        toolbar.removeView(itemSearch);
        toolbar.setTitle("Записать сон");
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nameNote.getWindowToken(), 0);
    }

    public void setSoftInput(){
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
               imm.toggleSoftInput( InputMethodManager.SHOW_FORCED, 0 );
    }
}