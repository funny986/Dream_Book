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
import android.view.animation.Animation;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Consumer;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dataBase.Words;
import org.jetbrains.annotations.NotNull;

import javax.xml.datatype.Duration;
import java.util.*;

import static android.content.Context.*;
import static com.dreambook.MainActivity.database;
//import static com.dreambook.MainActivity.nestedScrollView;

public class MeansFragment extends Fragment implements View.OnClickListener
                                             ,SearchView.OnQueryTextListener{
    public MeansFragment() {}

    public final char[] ALPHABET = {'а','б','в','г', 'д', 'е','ё','ж','з','и','й','к','л','м','н', 'о', 'п','р','т','у'
                            ,'ф','х','ц', 'ч','ш','щ','э','ю','я'};

    private RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private RecycleViewAdptr adapter;
    private Toolbar toolbar;

    public SearchView searchView;
    public Drawable drawable;
    private List<Words> searchList, wordList;


    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
//                outState.putStringArrayList("words_list", );
    }

    @Override
    public void onPause() {
        super.onPause();
//        nestedScrollView.setNestedScrollingEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppBarLayout appBarLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false);
        appBarLayout.setVisibility(View.VISIBLE);
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
         int hrl =  bottomNavigationView.getHeight(); //196
         int min = getScreenHeight() - hrl- tool;//2516 // 2320 минус тулбар
         float summ2 = (float) min / ALPHABET.length;//80
         float heightDisplay = convertPixelsToDp(summ2, Objects.requireNonNull(getContext())) - 5f;
         ConstraintLayout.LayoutParams params =
                 new ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(2);
        TextView[] simbol = new TextView[ALPHABET.length];
        for (int i = 0; i < ALPHABET.length; i++){
            simbol[i] = new TextView(getContext());
            simbol[i].setId(View.generateViewId());
            simbol[i].setText(String.valueOf(ALPHABET[i]));
            simbol[i].setTextColor(Color.BLACK);
            simbol[i].setTextSize(heightDisplay);
            simbol[i].setLayoutParams(params);
            simbol[i].setOnClickListener(this);
            layout.addView(simbol[i]);
        }
     }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
                FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
                fab.setVisibility(View.INVISIBLE);
        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        searchList = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        searchList = new ArrayList<>();
        searchView = Objects.requireNonNull(getActivity()).findViewById(R.id.search_in);
        searchView.setVisibility(View.VISIBLE);
        drawable = Objects.requireNonNull(getActivity()).getDrawable(R.drawable.search_background_means);
        searchView.setBackground(drawable);
        searchView.setIconifiedByDefault(false);
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_button:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_means, container, false);
        alphabetPanel(view);
        wordList = database.wordsDao().getAllWords();
        recyclerView = view.findViewById(R.id.means_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new NotesFragment.SpacesItemDecoration(50));
        adapter = new RecycleViewAdptr(getContext(), wordList);
        adapter.setmData(wordList, this);
        recyclerView.setAdapter(adapter);

        String from = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            from = bundle.getString("fromSearch");
            if (from != null){}
//                textView.setText(from);
        }
        return view;
    }
}
