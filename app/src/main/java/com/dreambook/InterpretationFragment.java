package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import com.gainwise.linker.Linker;
import com.gainwise.linker.LinkerListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.dreambook.MainActivity.*;
import static dataBase.Base.*;

@SuppressWarnings("rawtypes")
public class InterpretationFragment extends Fragment implements MoveAddSearchItem, ExecutorService {

    final Queue<Runnable> tasks = new ArrayDeque<>();
    ExecutorService executor;
    Runnable active;
    Future<ArrayMap<Integer, String>> findExept, findFull;

    public synchronized void execute(final Runnable r) {
        tasks.add(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (active == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executor.execute(active);
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return null;
    }

    @Override
    public Future<?> submit(Runnable task) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return null;
    }

    public InterpretationFragment() {}

    public InterpretationFragment(int textsize) {
        this.textsize = textsize;
    }

    public ArrayList<String> listFromNote;

    public ArrayMap<Integer,String> exeptFindFromText; // string - пара слов
    public ArrayMap<Integer, String> fullFindFromText;

    public SparseArrayCompat<String> wordslink;

    private View itemsearch;
    private Toolbar toolbar;
    private Activity activity;
    private List<String> exeptList;
    private List<String> fullWordList;

    public String note, title;

    private int gender;
    private int textsize;

    private Set setExept;
    private Set setFull;

    @Override
    public void onResume() {
        super.onResume();
        delItemSearch(toolbar, itemsearch);
        gender = activity
                .getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE)
                .getInt(AUTOR_GENDER, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.onDestroyView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        moveAdd(toolbar, itemsearch);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        moveAdd(toolbar, itemsearch);
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void moveAdd(@NotNull Toolbar toolbar, View view) {
        try {
            toolbar.addView(view);
        } catch (IllegalStateException | IllegalArgumentException ignore) {}
    }

    @Override
    public void delItemSearch(@NotNull Toolbar toolbar, View view) {
        try {
            toolbar.removeView(view);
        } catch (IllegalStateException | IllegalArgumentException ignore) {}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        itemsearch = toolbar.findViewById(R.id.search_in);
    }

    public void createTextClick(TextView interpritate){
        ArrayList<String> stringText = new ArrayList<>(listFromNote);
        wordslink = new SparseArrayCompat<>();
        try {
            setExept = findExept.get().keySet();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            setFull = findFull.get().keySet();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < textsize; i++){
            String value = stringText.get(i);
            if (setExept != null && setExept.contains(i)){
                String combination = exeptFindFromText.get(i);
                wordslink.put(i, combination);
                i++;
            }
            else {
                if (setFull != null && setFull.contains(i)){
                    wordslink.put(i, fullFindFromText.get(i));
                }
            }
        }
        Linker linker = new Linker(interpritate);
        ArrayList<String> listOfLinks = new ArrayList<>();
        for (int i = 0; i < textsize; i++){
            if (wordslink.get(i) != null)
                listOfLinks.add(wordslink.get(i));
        }
        linker.addStrings(listOfLinks);
        linker.setAllLinkColors(Color.BLUE);
        linker.setAllLinkUnderline(false);
        linker.setListener(new LinkerListener() {
            @Override
            public void onLinkClick(String charSequenceClicked) {
                InterpretationFragmentDirections.ActionInterpretationToWordmean action =
                        InterpretationFragmentDirections.actionInterpretationToWordmean(charSequenceClicked);
                NavHostFragment.findNavController(InterpretationFragment.this)
                        .navigate(action);
                Toast.makeText(getContext(), charSequenceClicked, Toast.LENGTH_SHORT).show();
            }
        });
        linker.update();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interpretation, container, false);
        TextView interpritate = view.findViewById(R.id.interpretation_tv);

        int margin = getResources().getDimensionPixelOffset(R.dimen.margin_start_interpretation);
        toolbar.setTitleMarginStart(margin);
        if (getArguments() != null) {
            int id = getArguments().getInt("noteID");
            note = database.notesDao() //запись от пользователя
                    .getNoteById(id)
                    .getNote();
            title = database.notesDao()
                    .getNoteById(id)
                    .getNameNote();
            toolbar.setTitle(title);
        }
        note = note.toLowerCase(Locale.ROOT);
        exeptList = database.wordsDao().getWordsByType(EXEPT_WORD, gender);

        fullWordList = database.wordsDao().getWordsByType(FULL_WORD, gender);
        listFromNote = new ArrayList<>();
        executor = Executors.newFixedThreadPool(2);
        Pattern pattern = Pattern.compile("[а-я]+\\b");
        Matcher matcher = pattern.matcher(note);
        while (matcher.find()) listFromNote.add(note.substring(matcher.start(), matcher.end()));
        textsize = listFromNote.size();
        interpritate.setText(note);
        /*    главный блок поиска сравнений слов */
        exeptFindFromText = new ArrayMap<>();
        fullFindFromText = new ArrayMap<>();
        findExept = executor.submit(new Callable<ArrayMap<Integer, String>>() {
            @Override
            public ArrayMap<Integer, String> call() throws Exception {
                ArrayList<String> tempListFromNote = new ArrayList<>(listFromNote);
                while (tempListFromNote.size() != 0) {
                    boolean match = true;
                    String couple = tempListFromNote.get(0) + " " + tempListFromNote.get(1);
                    for (String s : exeptList) {
                        if (s.equals(couple)) {
                            int pos = textsize - tempListFromNote.size();
                            exeptFindFromText.put(pos, couple); //совпадения по сочетаниям (первая позиция в тексте и + 1, само сочетание)
                            tempListFromNote.remove(1);
                            tempListFromNote.remove(0);
                            match = false;
                            break;
                        }
                    }
                    if (tempListFromNote.size() > 0) {
                        if (match) {
                            tempListFromNote.remove(0);
                        }
                    }
                }
                return exeptFindFromText;
            }
        });
        /* Поиск совпадений слов по полному совпадению */
        findFull = executor.submit(new Callable<ArrayMap<Integer, String>>() {
            @Override
            public ArrayMap<Integer, String> call() throws Exception {
                ArrayList<String> tempListFromNote = new ArrayList<>(listFromNote);
                if (exeptFindFromText.size() != 0) {
                    ArrayMap<Integer, String> listExept = new ArrayMap<>(exeptFindFromText);
                    Set set = listExept.keySet();
                    for (int i = 0; i < textsize; i++) {
                        String value = tempListFromNote.get(i);
                        int l = value.toCharArray().length;
                        if (l > 2) {
                            if (set.contains(i)) {
                                i += 1;
                            } else {
                                if (fullWordList.contains(value)) {
                                    fullFindFromText.put(i, value);
                                }
                            }
                        }
                    }
                }
                else {
                    for (int i = 0; i < textsize; i++) {
                        String value = tempListFromNote.get(i);
                        int l = value.toCharArray().length;
                        if (l > 2) {
                            if (fullWordList.contains(value)) {
                                fullFindFromText.put(i, value);
                            }
                        }
                    }
                }
                return fullFindFromText;
            }
        });
        executor.shutdown();
        /*  Конец  блока поиска сравнений слов */
        createTextClick(interpritate);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}