package com.dreambook;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.dreambook.dataBase.Notes;
import static com.dreambook.MainActivity.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FragmentEdit extends Fragment implements View.OnClickListener {

    public FragmentEdit() {}

    public BottomNavigationView bottomNavigation;
    public String name, recNote, noteOrigin, dateStr;
    private EditText nameNote, record, label;
    private TextView dateNote;
    public Calendar calendar;
    public int id;

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigation.setVisibility(View.INVISIBLE);
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations
                              .Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private  String year;

    private void setDateNote(){
        String dateStr = DateUtils.formatDateTime(getContext(), calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        year = dateStr;
        Date date = new Date();
        date = calendar.getTime();
        final DateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        DateFormat dfYear = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        dateStr = df.format(date);
        year = dfYear.format(date);
        dateNote.setText(dateStr);
    }

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()){
            case R.id.button_exit:
                NavHostFragment.findNavController(FragmentEdit.this)
                        .navigate(R.id.nav_notes);
                break;
            case R.id.button_save:
                assert getArguments() != null;
                id = FragmentEditArgs.fromBundle(getArguments()).getNoteId();
                name = nameNote.getText().toString();
                if (name.equals("")) name = "Без названия";
                noteOrigin = record.getText().toString();
                String labelStr = "";
                labelStr = label.getText().toString();
                String datestring = dateNote.getText().toString();
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
                try {
                    date = df.parse(datestring);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat dfy = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
                assert date != null;
                datestring = dfy.format(date);

                Notes note = new Notes(id, name, noteOrigin, datestring, labelStr);
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
        dateNote = view.findViewById(R.id.date_note_edit);
        calendar = Calendar.getInstance(Locale.getDefault());
        assert getArguments() != null;
       id = FragmentEditArgs.fromBundle(getArguments()).getNoteId();
       Notes notes = database.notesDao().getNoteById(id);
        name = notes.getNameNote();
        noteOrigin = notes.getNote();
        label.setText(notes.getLabelNote());
        recNote = noteOrigin;
        dateStr = notes.getDate();
       Date date = new Date();
        DateFormat dfy = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        try {
            date = dfy.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final DateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        assert date != null;
        dateStr = df.format(date);
        dateNote.setText(dateStr);
       if (!name.equals("Без названия")) nameNote.setText(name);
            record.setText(recNote);
        dateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateChange = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(@NotNull DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setDateNote();
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateChange,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getContext().setTheme(R.style.MyDatePicker);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                        .setText("Отмена");
            }
        });
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
