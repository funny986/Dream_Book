package com.dreambook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;
import com.dreambook.dataBase.Notes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.dreambook.MainActivity.database;
import static com.dreambook.R.style.MyAlertDialogTheme;

public class RecordingFragment extends Fragment implements View.OnClickListener, RecognitionListener {

    private EditText nameNote, record, labels;
    private FloatingActionButton fab;
    private Resources resources;
    private SpeechRecognizer sr;
    private TextView recording, dateNote;
    private Button btnExit, btnSave;
    private boolean recordVoice = false;
    private Intent intentSR;
    public Calendar calendar;

    public RecordingFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
        BottomNavigationView bottomNavigation = requireActivity().findViewById(R.id.bottom_navigation);
                        bottomNavigation.setVisibility(View.INVISIBLE);
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

    public void askContinuation(final Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Продолжить запись?")
                .setIcon(R.drawable.ic_cancel_keys)
                .setCancelable(true)
                .setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            String oldStr = record.getText().toString();
                            setOld(oldStr);
                        sr.startListening(intent);
                    }
                })
                .setNegativeButton("Заново", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setOld("");
                        sr.startListening(intent);
                    }
                })
                .getContext()
                .setTheme(MyAlertDialogTheme);
        builder.create();
        builder.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()){
            case R.id.button_exit:
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_record,
                        Objects.requireNonNull(getContext()).getTheme()));
                NavHostFragment.findNavController(RecordingFragment.this)
                        .navigate(R.id.nav_notes);
                break;
            case R.id.button_save:
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_record,
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
                String dateStr = year;
                String labelStr = labels.getText().toString();
                    if (labelStr.equals("")) labelStr = "";
                Notes note = new Notes(id, name, noteOrigin, dateStr, labelStr);
                database.notesDao().insert(note);
                RecordingFragmentDirections.ActionRecordToInterpretation action =
                        RecordingFragmentDirections.actionRecordToInterpretation(id);
                NavHostFragment.findNavController(RecordingFragment.this)
                        .navigate(action);
                break;
            case R.id.fab:
                if (recordVoice){
                    onEndOfSpeech();
                }
                else {
                    recordVoice = true;
                    nameNote.clearFocus();
                    if (record.getText().length() > 0) {
                        askContinuation(intentSR);
                    }
                    else {
                        sr.startListening(intentSR);
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
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
    public View onCreateView(@NotNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_recording, container, false);
            nameNote = view.findViewById(R.id.name_note);
            record = view.findViewById(R.id.record_et);
            labels = view.findViewById(R.id.label_et);
            dateNote = view.findViewById(R.id.date_note);
        recording = view.findViewById(R.id.recording);
            recording.setVisibility(View.INVISIBLE);
            calendar = Calendar.getInstance(Locale.getDefault());
        setDateNote();
        setOld("");
        nameNote.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                           @Override
                           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                               if (actionId == EditorInfo.IME_ACTION_NEXT) {
                                   record.requestFocus();
                                   setSoftInput();
                                   return true;
                               }
                               return false;
                           }
                       });
        dateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateChange = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(@NotNull DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        view.setContentDescription(getResources().getString(R.string.disc_choise_date));
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
        btnExit = view.findViewById(R.id.button_exit);
         btnSave = view.findViewById(R.id.button_save);
        btnSave.setText("Сохранить");
        btnExit.setText("Выход");
        btnExit.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
        fab.setImageDrawable(resources.getDrawable(R.drawable.ic_mic_on, Objects.requireNonNull(getContext()).getTheme()));
        fab.setOnClickListener(this);
        intentSR = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentSR.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSR.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intentSR.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                Objects.requireNonNull(getContext()).getPackageName());
        intentSR.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        intentSR.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);//
        intentSR.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 0);
        intentSR.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intentSR.putExtra(RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT, true);
        intentSR.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        sr = SpeechRecognizer.createSpeechRecognizer(getContext());
        sr.setRecognitionListener(this);
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

    @Override
    public void onDestroy() {
        if (sr !=null) sr.destroy();
        super.onDestroy();
    }

    public void setSoftInput(){
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(nameNote, InputMethodManager.SHOW_FORCED);
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
    public void onReadyForSpeech(@NotNull Bundle params) {
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
        Log.d(TAG, "onBufferReceived " + Arrays.toString(buffer));
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech ");
            sr.stopListening();
            sr.cancel();
            changeFabIcon();
            Log.d(TAG, "onEndOfSpeech " + " startIntent");
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "error " + error);
        changeFabIcon();
    }

    @Override
    public void onResults(@NotNull Bundle results) {
        Log.d(TAG, "onResult ");
            Log.d(TAG, "onResult " + results.toString());
    }

    private String old;

    private void setOld(String old){
        this.old = old;
    }

    private String getOld(){
        return old;
    }

    @Override
    public void onPartialResults(@NotNull Bundle partialResults) {
        Log.d(TAG, "onPartialResults ");
        ArrayList<String> result = partialResults
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String  str = result.get(0);
        String temp = getOld() + " " + str;
        record.setText(temp);
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent " + eventType);
    }

    private static final String TAG = "RecognitionListener";
}