package com.dreambook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dataBase.Words;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.dreambook.MainActivity.database;

public class MeansFragment extends Fragment implements View.OnClickListener
                                             , SearchView.OnQueryTextListener, MoveAddSearchItem{
    public MeansFragment() {}

    public final char[] ALPHABET = {'а','б','в','г', 'д', 'е', 'ж','з','и','к','л','м','н', 'о', 'п','р','т','у'
                            ,'ф','х','ц', 'ч','ш','щ','э','ю','я'}; //27

    private RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private RecycleViewAdptr adapter;
    private Toolbar toolbar;
    private MenuItem menuItem;
    private int hrl;
    private int min;
    private String tag;
    private TextView[] simbol;
    private final ArrayList<Character> letter = new ArrayList<>(27);

    public Activity activity;
    public View mainView, item;
    public SearchView searchView;
    public Drawable drawable;
    public AppBarLayout appBarLayout;
    private List<Words> searchList, wordList;
    private List<String> list;
    private LinearLayoutManager layoutManager;

    int firstVisiblePosition, items, lastCompleteVisiblePosition;
    public int genderForNote;

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.setQuery("", true);
        searchView.clearFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        assert getArguments() != null;
        genderForNote = MeansFragmentArgs.fromBundle(getArguments()).getGender();
        try {
            moveAdd(toolbar, item);
        }
        catch (IllegalArgumentException | IllegalStateException ignored){};
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof Activity){
            activity = (Activity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        item = toolbar.findViewById(R.id.search_in);
        toolbar.setNavigationIcon(null);
        toolbar.setTitle(null);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static float convertPixelsToDp(float px, @NotNull Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void alphabetPanel(@NotNull View view){
         LinearLayout layout = view.findViewById(R.id.alphabet);
         BottomNavigationView bottomNavigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        int tool = toolbar.getHeight();////196
        hrl =  bottomNavigationView.getHeight(); //196
         min = getScreenHeight() - hrl- tool;//2516 // 2320 минус тулбар
         float summ2 = (float) min / ALPHABET.length;//80
         float heightDisplay = convertPixelsToDp(summ2, Objects.requireNonNull(getContext())) - 6.2f;
         LinearLayout.LayoutParams params =
                 new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT);
        simbol = new TextView[ALPHABET.length];
        for (int i = 0; i < ALPHABET.length; i++) {
            tag = String.valueOf(ALPHABET[i]);
            simbol[i] = new TextView(getContext());
            simbol[i].setId(View.generateViewId());
            simbol[i].setText(tag);
            simbol[i].setTextSize(heightDisplay);
            simbol[i].setLayoutParams(params);
            simbol[i].setTag(tag);
            simbol[i].setOnClickListener(this);
            layout.addView(simbol[i]);
            letter.add(ALPHABET[i]);
        }
    }

    @Override
    public void onClick(@NotNull View view) {
        char click = view.getTag().toString().charAt(0);
        boolean exept = false;
        if (click == 'и') {
            exept = true;
            click = 'к';
        }
        int position = 0;
        for (Words words : wordList) {
            if (words.getTableName() == click) break;
            position++;
        }
        items = lastCompleteVisiblePosition - firstVisiblePosition;
        if (exept){
            exept = false;
            position -=2;
        }
        if (lastCompleteVisiblePosition < position) {
            recyclerView.scrollToPosition(position - items);
            position += items;
        }
        else  {
              recyclerView.scrollToPosition(position + items);
        }
        recyclerView.smoothScrollToPosition(position);
    }

    public void setCheckVisibleChar(char firstLetter, char lastLetter){
        int strt = letter.indexOf(firstLetter);
        int end = letter.indexOf(lastLetter);
        for (int j = 0; j < simbol.length; j++) {
            if (j >= strt && j <= end)
                simbol[j].setTextColor(Objects.requireNonNull(getActivity()).getColor(R.color.CheckAlph));
            else
                simbol[j].setTextColor(Objects.requireNonNull(getActivity()).getColor(R.color.navUnChecked));
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Words> tempString = new ArrayList<>();
        searchList.clear();
        for (Words tempCont : wordList) {
            String temp = tempCont.getWord();
            if (temp.toLowerCase().contains(newText.toLowerCase())) {
                tempString.add(tempCont);
            }
        }
        searchList = tempString;
        adapter.setmData(searchList, getParentFragment());
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.main, menu);
        searchList = new ArrayList<>();
        menuItem = menu.findItem(R.id.save_note);
        menuItem.setVisible(false);
        menuItem = menu.findItem(R.id.sorting);
        menuItem.setVisible(false);
        searchView = Objects.requireNonNull(getActivity()).findViewById(R.id.search_in);
        drawable = Objects.requireNonNull(getActivity()).getDrawable(R.drawable.search_background);
        moveAdd(toolbar, item);
        searchView.setBackground(drawable);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_means, container, false);
        alphabetPanel(mainView);
        wordList = database.wordsDao().listForFragment();
        recyclerView = mainView.findViewById(R.id.means_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NotesFragment.SpacesItemDecoration(50));
            adapter = new RecycleViewAdptr(getContext(), wordList);
            adapter.setmData(wordList, getParentFragment());
        min = getScreenHeight() - hrl;
        recyclerView.getLayoutParams().height = min;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView rv, int dx, int dy) {
                if (searchView.getQuery().length() == 0) {
                    assert layoutManager != null;
                    firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                    lastCompleteVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    char lastWord = adapter.mData.get(lastCompleteVisiblePosition).getTableName();
                    char firstWord = adapter.mData.get(firstVisiblePosition).getTableName();
                    setCheckVisibleChar(firstWord, lastWord);
                }
            }
        });
        return mainView;
    }

    @Override
    public void moveAdd(@NotNull Toolbar toolbar, View view) {
        try {
            toolbar.addView(view);
            toolbar.setNavigationIcon(null);
            toolbar.setTitle(null);
        }
        catch (IllegalStateException | IllegalArgumentException ignore){};
    }

    @Override
    public void delItemSearch(Toolbar toolbar, View view) {

    }
}
