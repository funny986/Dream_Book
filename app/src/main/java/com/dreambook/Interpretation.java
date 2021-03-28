package com.dreambook;

import android.widget.TextView;
import androidx.collection.ArrayMap;
import androidx.collection.SparseArrayCompat;
import com.gainwise.linker.Linker;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dreambook.MainActivity.database;
import static com.dreambook.dataBase.Base.*;

public class Interpretation implements ExecutorService{

    final Queue<Runnable> tasks = new ArrayDeque<>();
    public ExecutorService executor;
    public Runnable active, exept, full;
    public ArrayList<String> listFromNote;

    public ArrayMap<Integer,String> exeptFindFromText;
    public ArrayMap<Integer, String> fullFindFromText;

    public SparseArrayCompat<String> wordslink;

    private final List<String> exeptList;
    private final List<String> fullWordList;

    public String note;
    public Linker linker;

    private final int textsize;

    public Linker getLinker() {
        return linker;
    }

    public void setLinker(Linker linker) {
        this.linker = linker;
    }

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
    public boolean awaitTermination(long timeout, TimeUnit unit) {
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
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        return null;
    }

    public void createTextClick(TextView interpritate) {
        wordslink = new SparseArrayCompat<>();
        for (int i = 0; i < textsize; i++){
                if (exeptFindFromText !=null && exeptFindFromText.containsKey(i)){
                    String combination = exeptFindFromText.get(i);
                wordslink.put(i, combination);
                i++;
            }
            else {
                    if (fullFindFromText !=null && fullFindFromText.containsKey(i)){
                        wordslink.put(i, fullFindFromText.get(i));
                }
            }
        }
        linker = new Linker(interpritate);
        ArrayList<String> listOfLinks = new ArrayList<>();
        for (int i = 0; i < textsize; i++){
            if (wordslink.get(i) != null)
                listOfLinks.add(wordslink.get(i));
        }
        linker.addStrings(listOfLinks);
        linker.setAllLinkColors(R.color.linkText);
        linker.setAllLinkUnderline(false);
        setLinker(linker);
        linker.update();
    }

    public void findWords(){
        /*    главный блок поиска сравнений слов */
        exeptFindFromText = new ArrayMap<>();
        fullFindFromText = new ArrayMap<>();
        exept = new Runnable() {
            @Override
            public void run() {
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
            }
        };
        /* Поиск совпадений слов по полному совпадению */
        full = new Runnable() {
            @Override
            public void run() {
                ArrayList<String> tempListFromNote = new ArrayList<>(listFromNote);
                if (exeptFindFromText.size() != 0) {
                    ArrayMap<Integer, String> listExept = new ArrayMap<>(exeptFindFromText);
                    Set<Integer> set = listExept.keySet();
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
            }
        };
        try {
            exeptFindFromText = executor.submit(exept, exeptFindFromText).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            fullFindFromText = executor.submit(full, fullFindFromText).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        /*  Конец  блока поиска сравнений слов */
    }

    public Interpretation(TextView interpritate, String note, int gender) {
            String originNote = note;
            note = note.toLowerCase(Locale.ROOT);
            exeptList = database.wordsDao().getWordsByType(EXEPT_WORD, gender);
            exeptList.addAll(database.wordsDao().getWordsByType(DECLENCION_EXEPT_WORD, gender));
            fullWordList = database.wordsDao().getWordsByType(FULL_WORD, gender);
            fullWordList.addAll(database.wordsDao().getWordsByType(DECLENCION_WORD, gender));
            listFromNote = new ArrayList<>();
            executor = Executors.newFixedThreadPool(2);
            Pattern pattern = Pattern.compile("[а-яё]+\\b");
            Matcher matcher = pattern.matcher(note);
            while (matcher.find()) listFromNote.add(note.substring(matcher.start(), matcher.end()));
            textsize = listFromNote.size();
            interpritate.setText(originNote);
        findWords();
        createTextClick(interpritate);
    }

}
