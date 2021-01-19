package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.gainwise.linker.Linker;
import com.gainwise.linker.LinkerListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.dreambook.dataBase.Notes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static com.dreambook.MainActivity.*;

public class RecordingFragmentReady extends Fragment{

    public RecordingFragmentReady() {}

    private Notes note;
    public BottomNavigationView bottomNavigation;
    public String name, recNote, noteOrigin;
    private Activity activity;
    private int gender;

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        gender = activity
                .getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE)
                .getInt(AUTOR_GENDER, 0);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        requireActivity().getMenuInflater().inflate(R.menu.main, menu);
//        MenuItem item = menu.findItem(R.id.sorting);
//        item.setVisible(false);
//        item = menu.findItem(R.id.save_note);
//        item.setIcon(R.drawable.ic_record_light);
//        item = menu.findItem(R.id.record_voice);
//        item.setVisible(false);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.save_note:
//                database.notesDao().insert(note);
//                        item.setIcon(R.drawable.ic_check);
//                NavHostFragment.findNavController(RecordingFragmentReady.this)
//                                .navigate(R.id.nav_notes);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations
                              .Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recording_ready, container, false);
        TextView nameNote = view.findViewById(R.id.name_note_tv);
        TextView record = view.findViewById(R.id.record_tv);
        TextView label = view.findViewById(R.id.label_tv);
        assert getArguments() != null;
        name = RecordingFragmentReadyArgs.fromBundle(getArguments()).getNameNote();
        noteOrigin = "  " +  RecordingFragmentReadyArgs.fromBundle(getArguments()).getNote();
        label.setText(RecordingFragmentReadyArgs
                    .fromBundle(getArguments())
                    .getLabelNote());
        recNote = noteOrigin;
            if (name.equals("")) name = "Без названия";
            String dateStr = RecordingFragmentReadyArgs.fromBundle(getArguments()).getDateNote();
        int id;
        nameNote.setText(name);
        record.setText(recNote);
        Linker linker = new Interpretation(record, recNote, gender).getLinker();
        linker.setListener(new LinkerListener() {
            @Override
            public void onLinkClick(String charSequenceClicked) {
                RecordingFragmentReadyDirections.ActionReadyToWordmean action =
                        RecordingFragmentReadyDirections.actionReadyToWordmean(charSequenceClicked);
                NavHostFragment.findNavController(RecordingFragmentReady.this)
                        .navigate(action);
                Toast.makeText(getContext(), charSequenceClicked, Toast.LENGTH_SHORT).show();
            }
        });
        linker.update();
        TextView datetv = view.findViewById(R.id.date_tv);
        datetv.setText(dateStr);
        List<Notes> list = database.notesDao().getIdList();
        if (list.size() == 0) id = 1;
        else {
            int n = list.size() - 1;
            id = list.get(n).getId() + 1;
        }
        note = new Notes(id, name, noteOrigin, dateStr, label.getText().toString());
        bottomNavigation = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.INVISIBLE);
        return view;
    }

}
