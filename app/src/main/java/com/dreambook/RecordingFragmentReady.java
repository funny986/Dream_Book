package com.dreambook;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import dataBase.Notes;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.dreambook.MainActivity.*;

public class RecordingFragmentReady extends Fragment {

    public RecordingFragmentReady() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view;
    private Notes note;
    private TextView nameNote, record;
    public BottomNavigationView bottomNavigation;
    public FloatingActionButton floatingAction;

    public String name, recNote;

    @Override
    public void onResume() {
        super.onResume();
        AppBarLayout appBarLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
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
        assert getArguments() != null;
        name = RecordingFragmentReadyArgs.fromBundle(getArguments()).getNameNote();
        recNote = RecordingFragmentReadyArgs.fromBundle(getArguments()).getNote();
            if (name.equals("")) name = "Без названия";
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String dateStr = df.format(date);
        int id;
        nameNote.setText(name);
        record.setText(recNote);
        List<Notes> list = database.notesDao().getIdList();
        if (list.size() == 0) id = 1;
        else {
            int n = list.size() - 1;
            id = list.get(n).getId() + 1;
        }
        note = new Notes(id, name, recNote, dateStr);
        bottomNavigation = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.getMenu()
                .getItem(0)
                .setIcon(R.drawable.ic_delete_24)
                .setTitle("Удалить");
        bottomNavigation.getMenu()
                .getItem(1)
                .setIcon(R.drawable.ic_record_24)
                .setTitle("Править");
        bottomNavigation.getMenu()
                .getItem(2)
                .setIcon(R.drawable.ic_done_24)
                .setTitle("Записать");
        floatingAction = Objects.requireNonNull(getActivity())
                .findViewById(R.id.fab);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_notes:
                                floatingAction.setVisibility(View.VISIBLE);
                                NavHostFragment.findNavController(RecordingFragmentReady.this)
                                        .navigate(R.id.nav_notes);
                                break;
                            case R.id.action_means:
                                NavHostFragment.findNavController(RecordingFragmentReady.this)
                                        .popBackStack();
                                break;
                            case R.id.action_setting:
                                database.notesDao().insert(note);
                                floatingAction.setVisibility(View.VISIBLE);
                                NavHostFragment.findNavController(RecordingFragmentReady.this)
                                        .navigate(R.id.nav_notes);
                                break;
                        }
                        return true;
                    }
                });
        return view;
    }
}
