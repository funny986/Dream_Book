package com.dreambook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

public class MeansFragment extends Fragment implements SearchView.OnQueryTextListener{
    public MeansFragment() {}

    private int genderForNote;
    private InputMethodManager imm;
    public Activity activity;
    public View mainView, item;
    public SearchView searchView;
    public Drawable drawable;
    private List<Words> searchList, wordList;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;

    @SuppressLint("StaticFieldLeak")
    private RecycleViewAdptr adapter;

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
        genderForNote = activity
                .getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE)
                .getInt(AUTOR_GENDER, 0);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = activity.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
        if (view != null) {
           hideSoftInput();
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
        bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
            searchView = mainView.findViewById(R.id.search_in);
            searchView.setQueryHint(Objects.requireNonNull(getActivity()).getResources().getString(R.string.search_hint));
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
        RecyclerView recyclerView = mainView.findViewById(R.id.means_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NotesFragment.SpacesItemDecoration(1));
            adapter = new RecycleViewAdptr(getContext(), wordList);
            adapter.setmData(wordList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
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
