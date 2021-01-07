package com.dreambook;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import dataBase.Notes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dreambook.MainActivity.*;

public class RecordingFragmentReady extends Fragment implements MoveAddSearchItem{

    public RecordingFragmentReady() {}

    private MenuItem item;
    private View view, itemSearch;
    private Notes note;
    private TextView nameNote, record;
    public BottomNavigationView bottomNavigation;
    private Toolbar toolbar;
    public String name, recNote;

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        itemSearch = toolbar.getRootView().findViewById(R.id.search_in);
        itemSearch = MainActivity.itemSearch;
    }


    @Override
    public void onResume() {
        super.onResume();
        int margin = getResources().getDimensionPixelOffset(R.dimen.margin_start_recordingready);
        toolbar.setTitleMarginStart(margin);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.main, menu);
        item = menu.findItem(R.id.sorting);
        item.setVisible(false);
        item = menu.findItem(R.id.save_note);
        item.setIcon(R.drawable.ic_record_light);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                moveAdd(toolbar, itemSearch);
                database.notesDao().insert(note);
                        item.setIcon(R.drawable.ic_check);
                NavHostFragment.findNavController(RecordingFragmentReady.this)
                                .navigate(R.id.nav_notes);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations
                              .Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recording_ready, container, false);
        nameNote = view.findViewById(R.id.name_note_tv);
        record = view.findViewById(R.id.record_tv);
        toolbar.setTitle("Сохранить");
        assert getArguments() != null;
        name = RecordingFragmentReadyArgs.fromBundle(getArguments()).getNameNote();
        recNote = RecordingFragmentReadyArgs.fromBundle(getArguments()).getNote();
            if (name.equals("")) name = "Без названия";
            String dateStr = RecordingFragmentReadyArgs.fromBundle(getArguments()).getDateNote();
        int id;
        nameNote.setText(name);
        record.setText(recNote);
        TextView datetv = view.findViewById(R.id.date_tv);
        datetv.setText(dateStr);
        List<Notes> list = database.notesDao().getIdList();
        if (list.size() == 0) id = 1;
        else {
            int n = list.size() - 1;
            id = list.get(n).getId() + 1;
        }
        note = new Notes(id, name, recNote, dateStr);
        bottomNavigation = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.INVISIBLE);
        return view;
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
}
