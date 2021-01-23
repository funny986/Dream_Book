package com.dreambook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;
import com.dreambook.dataBase.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.dreambook.MainActivity.database;

public class RecordingFragment extends Fragment implements View.OnClickListener, RecognitionListener {

    private EditText nameNote, dateNote, record, labels;
    private FloatingActionButton fab;
    private Resources resources;
    private SpeechRecognizer sr;
    public TextView recording;
    public Button btnExit, btnSave;
    private boolean recordVoice = false;

    public RecordingFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
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
        resources = getResources();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()){
            case R.id.button_exit:
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_record_dark,
                        Objects.requireNonNull(getContext()).getTheme()));
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
            case R.id.button_save:
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_record_dark,
                        Objects.requireNonNull(getContext()).getTheme()));
                List<Notes> list = database.notesDao().getIdList();
                int id;
                if (list.size() == 0) id = 1;
                else {
                    int n = list.size() - 1;
                    id = list.get(n).getId() + 1;
                }
                String name = nameNote.getText().toString();
                if (name.equals("")) name = "Без названия";
                String noteOrigin = record.getText().toString();
                String dateStr = dateNote.getText().toString();
                    if (dateStr.equals("")){
                        Date date = new Date();
                        DateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
                        dateStr = df.format(date);
                    }
                String labelStr = labels.getText().toString();
                    if (labelStr.equals("")) labelStr = " ";
                Notes note = new Notes(id, name, noteOrigin, dateStr, labelStr);
                database.notesDao().insert(note);
                NavHostFragment.findNavController(RecordingFragment.this)
                        .navigate(R.id.nav_notes);
                break;
            case R.id.fab:
                if (recordVoice){
                    onEndOfSpeech();
                }
                else {
                    recordVoice = true;
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                            Objects.requireNonNull(getContext()).getPackageName());
                    intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 25000);
                    intent.putExtra(RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT, true);
                    intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                    sr = SpeechRecognizer.createSpeechRecognizer(getContext());
                    sr.setRecognitionListener(this);
                    sr.startListening(intent);
                }
                break;
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_recording, container, false);
            nameNote = view.findViewById(R.id.name_note);
            record = view.findViewById(R.id.record_et);
            labels = view.findViewById(R.id.label_et);
            dateNote = view.findViewById(R.id.date_note);
            recording = view.findViewById(R.id.recording);
            recording.setVisibility(View.INVISIBLE);
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("__.__.__");
        FormatWatcher formatWatcher = new MaskFormatWatcher( MaskImpl.createTerminated(slots));
        formatWatcher.installOn(dateNote);
        nameNote.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                           @Override
                           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                               if (actionId == EditorInfo.IME_ACTION_NEXT) {
                                   dateNote.requestFocus();
                                   return true;
                               }
                               return false;
                           }
                       });
        dateNote.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    record.requestFocus();
                    return true;
                }
                return false;
            }
        });
        btnExit = view.findViewById(R.id.button_exit);
        btnExit.setText("Выход");
        btnExit.setOnClickListener(this);
         btnSave = view.findViewById(R.id.button_save);
        btnSave.setText("Сохранить");
        btnSave.setOnClickListener(this);
        fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
        fab.setImageDrawable(resources.getDrawable(R.drawable.ic_mic_on, Objects.requireNonNull(getContext()).getTheme()));
        fab.setOnClickListener(this);
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

    public void changeFabIcon(){
        recordVoice = false;
        Toast.makeText(getContext(), "Речевая запись закончена", Toast.LENGTH_SHORT).show();
        btnExit.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        recording.setVisibility(View.INVISIBLE);
        fab.setImageDrawable(resources.getDrawable(R.drawable.ic_mic_on, Objects.requireNonNull(getContext()).getTheme()));

    }
    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
        Toast.makeText(getContext(), "Говорите", Toast.LENGTH_SHORT).show();
        btnExit.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.INVISIBLE);
        recording.setVisibility(View.VISIBLE);
        fab.setImageDrawable(resources.getDrawable(R.drawable.ic_stop,
                Objects.requireNonNull(getContext()).getTheme()));
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged " + rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        sr.stopListening();
        sr.cancel();
        changeFabIcon();
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "error " + error);
        changeFabIcon();
    }

    @Override
    public void onResults(Bundle results) {

    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> result = partialResults
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String  str = result.get(0);
        record.setText(str);
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent " + eventType);
    }
    private static final String TAG = "RecognitionListener";
}