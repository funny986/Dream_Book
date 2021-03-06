package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import com.dreambook.dataBase.Notes;
import com.gainwise.linker.Linker;
import com.gainwise.linker.LinkerListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.dreambook.MainActivity.*;

public class InterpretationFragment extends Fragment {


    public InterpretationFragment() {}

    private int gender, id;
    private Activity activity;
    private Notes note;
    public String  nameStr, dateStr, labelStr;

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigation = activity.findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.INVISIBLE);
        FloatingActionButton fab = activity.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.onDestroyView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        gender = activity
                .getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE)
                .getInt(AUTOR_GENDER, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interpretation, container, false);
        TextView interpritate = view.findViewById(R.id.interpretation_tv);
        if (getArguments() != null) {
            id = getArguments().getInt("noteID");
            note = database.notesDao() //запись от пользователя
                    .getNoteById(id);
            String noteOrigin = note.getNote();
            Linker linker = new Interpretation(interpritate, noteOrigin, gender, activity.getResources()).getLinker();
           linker.setListener(new LinkerListener() {
               @Override
               public void onLinkClick(String charSequenceClicked) {
                   String mean = database.wordsDao().getMean(charSequenceClicked, gender);
                   String fullWord = database.wordsDao().getFullWord(mean);
                   InterpretationFragmentDirections.ActionInterpretationToWordmean action =
                           InterpretationFragmentDirections.actionInterpretationToWordmean(fullWord, gender);
                   NavHostFragment.findNavController(InterpretationFragment.this)
                           .navigate(action);
                   Toast.makeText(getContext(), charSequenceClicked, Toast.LENGTH_SHORT).show();
               }
           });
           linker.update();
        }
        TextView name = view.findViewById(R.id.name_note_tv);
        nameStr = note.getNameNote();
        if (nameStr.equals("")) nameStr = "Без названия";
        name.setText(nameStr);
        TextView datetv = view.findViewById(R.id.date_tv);
        dateStr = note.getDate();
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dfy = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        assert date != null;
        String dateStr = dfy.format(date);
        datetv.setText(dateStr);
        TextView label = view.findViewById(R.id.label_tv);
        labelStr = note.getLabelNote();
        label.setText(labelStr);
        Button btnexit = view.findViewById(R.id.button_exit);
        btnexit.setText("Выход");
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(InterpretationFragment.this)
                        .navigate(R.id.nav_notes);
            }
        });
        Button btnEdit = view.findViewById(R.id.button_edit);
        btnEdit.setText("Редактировать");
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterpretationFragmentDirections.ActionInterpretationToEdit action =
                        InterpretationFragmentDirections.actionInterpretationToEdit(id);
                NavHostFragment.findNavController(InterpretationFragment.this)
                        .navigate(action);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}