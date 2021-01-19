package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RecordingFragment extends Fragment {

    private static final int RESULT_OK = 1;
    private View view;
    private EditText nameNote, record, labels;
    private String dateStr;
    private Activity activity;

    public RecordingFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        int margin = getResources().getDimensionPixelOffset(R.dimen.margin_start_recording);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        requireActivity().getMenuInflater().inflate(R.menu.main, menu);
//        MenuItem item = menu.findItem(R.id.sorting);
//        item.setVisible(false);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.save_note:
//                        RecordingFragmentDirections.ActionRecordToReady action =
//                RecordingFragmentDirections.actionRecordToReady(
//                        nameNote.getText().toString(),
//                        record.getText().toString(),
//                        dateStr,
//                        labels.getText().toString());
//                NavHostFragment.findNavController(RecordingFragment.this)
//                                .navigate(action);
//                break;
//            case android.R.id.home:
//                break;
//            case R.id.record_voice:
//                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
////                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
//                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
//                        Objects.requireNonNull(getContext()).getPackageName());
//                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 25000);
//                intent.putExtra(RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT, true);
//                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
//
//                SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(getContext());
//                CustomRecognitionListener listener = new CustomRecognitionListener();
//               sr.setRecognitionListener(listener);
//                sr.startListening(intent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recording, container, false);
        nameNote = view.findViewById(R.id.name_note);
        record = view.findViewById(R.id.record_et);
        labels = view.findViewById(R.id.label_et);
        nameNote.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                           @Override
                           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                               if (actionId == EditorInfo.IME_ACTION_NEXT) {
                                   record.requestFocus();
                                   return true;
                               }
                               return false;
                           }
                       });
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

    class CustomRecognitionListener implements RecognitionListener {
        private static final String TAG = "RecognitionListener";

        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
            Toast.makeText(getContext(), "Говорите", Toast.LENGTH_SHORT).show();
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged " + rmsdB);
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived " + buffer);
        }

        public void onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech");
            Toast.makeText(getContext(), "Речевая запись закончена", Toast.LENGTH_SHORT).show();
        }

        public void onError(int error) {
            Log.e(TAG, "error " + error);
        }

        public void onResults(@NotNull Bundle results) {
        }

        public void onPartialResults(@NotNull Bundle partialResults) {
            ArrayList<String> result = partialResults
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String  str = result.get(0);
            record.setText(str);
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }
    }
}