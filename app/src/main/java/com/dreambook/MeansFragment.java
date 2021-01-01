package com.dreambook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.GnssAntennaInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.style.LineHeightSpan;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
                                             , SearchView.OnQueryTextListener{
    public MeansFragment() {}

    public final char[] ALPHABET = {'а','б','в','г', 'д', 'е','ё','ж','з','и','й','к','л','м','н', 'о', 'п','р','т','у'
                            ,'ф','х','ц', 'ч','ш','щ','э','ю','я'};

    private RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private RecycleViewAdptr adapter;
    private Toolbar toolbar;
    private int hrl;
    private int min;
    private TextView textView;

    public View mainView;
    public SearchView searchView;
    public Drawable drawable;
    private List<Words> searchList, wordList;
    private List<String> list;
    private LinearLayoutManager layoutManager;

    int firstVisiblePosition, lastVisiblePosition, lastCompleteVisiblePosition;

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
        AppBarLayout appBarLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false);
        appBarLayout.setVisibility(View.VISIBLE);
            }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
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
         float heightDisplay = convertPixelsToDp(summ2, Objects.requireNonNull(getContext())) - 5f;
         LinearLayout.LayoutParams params =
                 new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMarginEnd(2);
        for (char c : ALPHABET) {
            textView = new TextView(getContext());
            textView.setLayoutParams(params);
            textView.setId(View.generateViewId());
            textView.setText(String.valueOf(c));
            textView.setTextSize(heightDisplay);
            textView.setOnClickListener(this);
            textView.setSelected(false);
            textView.setTextColor(getActivity().getColor(R.color.alph_selector));
            textView.setTag(String.valueOf(c));
            layout.addView(textView);
        }

    }


    @Override
    public void onClick(View view) {

    }
//    int findFirstVisibleItemPosition();
//    int findFirstCompletelyVisibleItemPosition();
//    int findLastVisibleItemPosition();
//    int findLastCompletelyVisibleItemPosition();

    public void setCheckVisibleChar(char firstLetter, char lastLetter){
//        simbol
        String tag = String.valueOf(firstLetter);
        textView = mainView.findViewWithTag(tag);
        textView.isSelected();
//        textView.setSelected(true);
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
        searchList = new ArrayList<>();
        searchView = Objects.requireNonNull(getActivity()).findViewById(R.id.search_in);
        searchView.setVisibility(View.VISIBLE);
        drawable = Objects.requireNonNull(getActivity()).getDrawable(R.drawable.search_background);
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
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NotesFragment.SpacesItemDecoration(50));
        adapter = new RecycleViewAdptr(getContext(), wordList);
        adapter.setmData(wordList, getParentFragment());
        min = getScreenHeight() - hrl;
        recyclerView.getLayoutParams().height = min;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

//        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView rv, int dx, int dy) {
                assert layoutManager != null;
                firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                lastCompleteVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                char lastWord =  adapter.mData.get(lastCompleteVisiblePosition).getTableName();
                char firstWord = adapter.mData.get(firstVisiblePosition).getTableName();
                setCheckVisibleChar(firstWord, lastWord);
            }
        });
        return mainView;
    }
}
