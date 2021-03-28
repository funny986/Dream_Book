package com.dreambook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.dreambook.dataBase.Words;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static android.content.Context.MODE_PRIVATE;
import static com.dreambook.MainActivity.*;

public class MeansFragment extends Fragment implements View.OnClickListener
                                             , SearchView.OnQueryTextListener{
    public MeansFragment() {}

    public final char[] ALPHABET = {'а','б','в','г', 'д', 'е', 'ж','з','и','к','л','м','н', 'о', 'п','р','с','т','у'
                            ,'ф','х','ц', 'ч','ш','щ','э','ю','я'}; //28

    private int hrl;
    private int min;
    private int firstVisiblePosition;
    private int lastCompleteVisiblePosition;
    private int genderForNote;
    private final ArrayList<Character> letter = new ArrayList<>(28);
    boolean skipMark = false;

    private InputMethodManager imm;
    public Activity activity;
    public View mainView, item;
    public SearchView searchView;
    public Drawable drawable;
    private List<Words> searchList, wordList;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;

    @SuppressLint("StaticFieldLeak")
    private RecycleViewAdptr adapter;
    private TextView[] simbol;

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
        skipMark = false;
        setCheckVisibleChar('а', 'а');
        imm = (InputMethodManager) requireActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        searchList = new ArrayList<>();
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(1);
        item.setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(@NotNull Context context){
        super.onAttach(context);
        if (context instanceof Activity){
            activity = (Activity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fab = activity.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        genderForNote = activity
                .getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE)
                .getInt(AUTOR_GENDER, 0);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static float convertPixelsToDp(float px, @NotNull Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    float heightSimbol;

    private void alphabetPanel(@NotNull View view) {
        LinearLayout layout = view.findViewById(R.id.alphabet);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        int tool = 0;
        hrl = bottomNavigationView.getHeight(); //196
        int heightFull = Resources.getSystem().getDisplayMetrics().heightPixels;
        min = heightFull - hrl - tool;//2516 // 2320 минус тулбар
        float summ2 = (float) min / ALPHABET.length;//80
        heightSimbol = convertPixelsToDp(summ2, requireContext());
        params.setMarginStart(20);
        if (heightFull <= 1920)
            heightSimbol -= 7.62f;
        if (heightFull > 1920 && heightFull <= 2280) {
            heightSimbol -= 6.6f;
            params.setMarginStart(150);
        }
        if (heightFull > 2280 && heightFull <= 2880)
            heightSimbol -= 8.5f;

        simbol = new TextView[ALPHABET.length];
        for (int i = 0; i < ALPHABET.length; i++) {
            String tag = String.valueOf(ALPHABET[i]);
            simbol[i] = new TextView(getContext());
            simbol[i].setId(View.generateViewId());
            simbol[i].setText(tag);
            simbol[i].setTextSize(heightSimbol);
            simbol[i].setTag(tag);
            simbol[i].setOnClickListener(this);
            layout.addView(simbol[i], params);
            letter.add(ALPHABET[i]);
        }
    }

    @Override
    public void onClick(@NotNull View view) {
        searchView.clearFocus();
        searchView.setQuery("", true);
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
        int items = lastCompleteVisiblePosition - firstVisiblePosition;
        if (exept){
            exept = false;
            position -=2;
        }
            hideSoftInput();
            skipMark = false;
            setCheckVisibleChar(adapter.mData.get(firstVisiblePosition).getTableName(),
                    adapter.mData.get(lastCompleteVisiblePosition).getTableName());
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
            if (!skipMark) {
                if (j >= strt && j <= end)
                    simbol[j].setTextColor(Objects.requireNonNull(getActivity()).getColor(R.color.colorPrimary));
                else
                    simbol[j].setTextColor(Objects.requireNonNull(getActivity()).getColor(R.color.navUnChecked));
            }
            else {
                simbol[j].setTextColor(Objects.requireNonNull(getActivity()).getColor(R.color.navUnChecked));
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
        if (view != null) {
           hideSoftInput();
        }
        skipMark = false;
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Words> tempString = new ArrayList<>();
        searchList.clear();
        skipMark = true;
        setCheckVisibleChar('а', 'а');
        for (int i = 0; i < wordList.size(); i++) {
            Words tempCont = wordList.get(i);
            String temp = tempCont.getWord();
            if (temp.toLowerCase().contains(newText.toLowerCase())) {
                tempString.add(tempCont);
                            }
            adapter.notifyItemChanged(i);
        }
        searchList = tempString;
        adapter.setmData(searchList);
        return true;
    }

    public void hideSoftInput() {
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_means, container, false);
        alphabetPanel(mainView);
            searchView = mainView.findViewById(R.id.search_in);
            drawable = Objects.requireNonNull(getActivity()).getDrawable(R.drawable.search_background);
            searchView.setBackground(drawable);
            searchView.setQueryHint(getActivity().getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(this);
                final int searchViewId2 = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchIconClear = searchView.findViewById(searchViewId2);
        searchIconClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
                searchView.setQuery("", true);
                hideSoftInput();
                skipMark =false;
                setCheckVisibleChar('а', 'а');
            }
        });
        final int searchEditId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchEdit = searchView.findViewById(searchEditId);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchView.clearFocus();
                    hideSoftInput();
                    return true;
                }
                return false;
            }
        });

        wordList = database.wordsDao().listForFragment(genderForNote);
        recyclerView = mainView.findViewById(R.id.means_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NotesFragment.SpacesItemDecoration(1));
            adapter = new RecycleViewAdptr(getContext(), wordList);
            adapter.setmData(wordList);
        min = getScreenHeight() - hrl;
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
        adapter.setClickInterfaceWord(new RecycleViewAdptr.ClickInterfaceWord() {
            @Override
            public void clickEventOne(Words word) {
                String arg = word.getWord();
                Toast.makeText(getContext(), "Толкование  " + arg,
                        Toast.LENGTH_SHORT)
                        .show();
                bottomNavigationView.setVisibility(View.INVISIBLE);
                MeansFragmentDirections.ActionWordToMean action =
                        MeansFragmentDirections.actionWordToMean(arg, genderForNote);
                NavHostFragment.findNavController(MeansFragment.this)
                        .navigate(action);
            }
        });
        return mainView;
    }
}
