package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.dreambook.dataBase.Notes;
import static com.dreambook.MainActivity.*;
import org.jetbrains.annotations.NotNull;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FragmentEdit extends Fragment implements View.OnClickListener {

    public FragmentEdit() {}

    private Notes note;
    public BottomNavigationView bottomNavigation;
    public String name, recNote, noteOrigin, dateStr;
    private Activity activity;
    private int gender;
    private Resources resources;
    private EditText nameNote, record, date, label;

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
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

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations
                              .Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public int id;

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()){
            case R.id.button_exit:
                FragmentEditDirections.ActionEditToInterpretation action =
                        FragmentEditDirections.actionEditToInterpretation(id);
                NavHostFragment.findNavController(FragmentEdit.this)
                        .navigate(action);
                break;
            case R.id.button_save:
                 id = FragmentEditArgs.fromBundle(getArguments()).getNoteId();
                name = nameNote.getText().toString();
                if (name.equals("")) name = "Без названия";
                noteOrigin = "  " + record.getText().toString();
                String dateStr = date.getText().toString();
                if (dateStr.equals("")){
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
                    dateStr = df.format(date);
                }
                String labelStr = "";
                labelStr = label.getText().toString();
                note = new Notes(id, name, noteOrigin, dateStr, labelStr);
                database.notesDao().update(note);
                FragmentEditDirections.ActionEditToInterpretation action2 =
                        FragmentEditDirections.actionEditToInterpretation(id);
                NavHostFragment.findNavController(FragmentEdit.this)
                        .navigate(action2);
                break;
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
         nameNote = view.findViewById(R.id.name_note_et);
        record = view.findViewById(R.id.record_edit_et);
        label = view.findViewById(R.id.label_edit_et);
        date = view.findViewById(R.id.date_note_et);
        assert getArguments() != null;
       id = FragmentEditArgs.fromBundle(getArguments()).getNoteId();
       Notes notes = database.notesDao().getNoteById(id);
        name = notes.getNameNote();
        noteOrigin = "  " + notes.getNote();
        label.setText(notes.getLabelNote());
        recNote = noteOrigin;
        dateStr = notes.getDate();
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("__.__.__");
        FormatWatcher formatWatcher = new MaskFormatWatcher( MaskImpl.createTerminated(slots));
        formatWatcher.installOn(date);
       if (!name.equals("Без названия")) nameNote.setText(name);
            record.setText(recNote);
            date.setText(dateStr);
        bottomNavigation = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.INVISIBLE);
        Button btnExit = view.findViewById(R.id.button_exit);
        btnExit.setText("Выход");
        btnExit.setOnClickListener(this);
        Button btnSave = view.findViewById(R.id.button_save);
        btnSave.setText("Сохранить");
        btnSave.setOnClickListener(this);
        return view;
    }
}