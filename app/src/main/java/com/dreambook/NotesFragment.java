package com.dreambook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dataBase.Notes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static com.dreambook.MainActivity.database;

public class NotesFragment extends Fragment implements View.OnFocusChangeListener {

    public RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public RecycleViewAdapter adapter;

    private List<Notes> noteList, searchList;
    public BottomNavigationView bottomNavigation;

    public SearchView searchView;
    public Drawable drawable;

    public NotesFragment(){}

    @Override
    public void onResume() {
        super.onResume();
        AppBarLayout appBarLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.setQuery("", true);
        searchView.clearFocus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchView = Objects.requireNonNull(getActivity()).findViewById(R.id.search_in);
        //        searchView.setFocusableInTouchMode(false);
//        searchView.setFocusable(false);

    }
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.id_sort_datenew);
        menuItem.setChecked(true);
        searchView.setVisibility(View.VISIBLE);
        drawable = getActivity().getDrawable(R.drawable.search_background);
        searchView.setBackground(drawable);
        searchView.setIconifiedByDefault(false);
        searchView.setOnFocusChangeListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String searchText) {
                List<Notes> tempString = new ArrayList<>();
                searchList.clear();
                for (Notes tempCont : noteList) {
                    String temp = tempCont.getNameNote();
                    if (temp.toLowerCase().contains(searchText.toLowerCase())) {
                        tempString.add(tempCont);
                    }
                }
                searchList = tempString;
                adapter.setmData(searchList);
                return true;
            }
                    });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView = Objects.requireNonNull(getActivity()).findViewById(R.id.search_in);
        searchList = new ArrayList<>();
    }

    @NotNull
    private List<Notes> sortNewDateFirst (@NotNull List<Notes> list){
        List<Notes> tempList = new ArrayList<>();
        int j = list.size() - 1 ;
        for (int i = j; i >= 0; i--){
            tempList.add(list.get(i));
        }
    return tempList;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_sort_name:
                item.setChecked(!item.isChecked());
                noteList = database.notesDao().getNotesListByName();
                adapter.setmData(noteList);
                break;
            case R.id.id_sort_datenew:
                item.setChecked(!item.isChecked());
                noteList = database.notesDao().getNotesListByDate();
                adapter.setmData(sortNewDateFirst(noteList));
                break;
            case R.id.id_sort_dateold:
                item.setChecked(!item.isChecked());
                noteList = database.notesDao().getNotesListByDate();
                adapter.setmData(noteList);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration{

        private final int space;

        public SpacesItemDecoration(int space){
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NotNull Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
            outRect.bottom = space;
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        view.setOnFocusChangeListener(this);
        noteList = database.notesDao().getNotesListByDate();
        recyclerView = view.findViewById(R.id.note_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(50));
        adapter = new RecycleViewAdapter(getContext(), noteList);
        noteList = sortNewDateFirst(noteList);
        adapter.setmData(noteList);
        recyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);
        return view;
    }

    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigation = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        bottomNavigation.getMenu()
                .getItem(0)
                .setIcon(R.drawable.ic_baseline_event_note_24)
                .setTitle(R.string.my_dreams);
        bottomNavigation.getMenu()
                .getItem(1)
                .setIcon(R.drawable.ic_translate_24)
                .setTitle(R.string.dream_means);
        bottomNavigation.getMenu()
                .getItem(2)
                .setIcon(R.drawable.ic_settings_24)
                .setTitle(R.string.setting);
        bottomNavigation.setOnNavigationItemSelectedListener(MainActivity.getListnr());
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_record_24);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NotesFragment.this).navigate(R.id.nav_record);
            }
        });
    }

    @Override
    public void onFocusChange(@NotNull View v, boolean hasFocus) {
        searchView.clearFocus();
        View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        }
}