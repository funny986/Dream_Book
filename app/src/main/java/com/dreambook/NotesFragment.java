package com.dreambook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.content.SharedPreferences;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dataBase.Notes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.dreambook.MainActivity.*;

public class NotesFragment extends Fragment implements MoveAddSearchItem, View.OnClickListener {

    public RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public RecycleViewAdapter adapter;

    private List<Notes> noteList, searchList;
    public BottomNavigationView bottomNavigation;

    int checkBoxUse;

    public SearchView getSearchView() {
        return this.searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public SearchView searchView;
    public Drawable drawable;
    public MenuItem item, sort;
    public Toolbar toolbar;
    Activity activity;
    SharedPreferences preferences;
    public int genderForNote;

    public NotesFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        genderForNote = activity
                .getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE)
                .getInt(AUTOR_GENDER, 0);
        bottomNavigation.setVisibility(View.VISIBLE);
        moveAdd(toolbar, getSearchView());
        hideSoftInput();
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
        searchView = requireActivity().findViewById(R.id.search_in);
        setSearchView(searchView);
        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        preferences = getActivity().getPreferences(MODE_PRIVATE);
        checkBoxUse = preferences.getInt(BOX_STATE, 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.main, menu);
        item = menu.findItem(R.id.save_note);
        item.setVisible(false);
        sort = menu.findItem(R.id.sorting);
        checkBoxUse = preferences.getInt(BOX_STATE, 0);
        switch (checkBoxUse) {
            case 0:
                MenuItem menuItem = menu.findItem(R.id.id_sort_datenew);
                menuItem.setChecked(true);
                checkBoxUse = menuItem.getItemId();
                break;
            case R.id.id_sort_name:
                menu.findItem(checkBoxUse).setChecked(true);
                noteList = database.notesDao().getNotesListByName();
                adapter.setmData(noteList, getParentFragment());
                break;
            case R.id.id_sort_datenew:
                menu.findItem(checkBoxUse).setChecked(true);
                noteList = database.notesDao().getNotesListByDate();
                adapter.setmData(sortNewDateFirst(noteList), getParentFragment());
                break;
            case R.id.id_sort_dateold:
                menu.findItem(checkBoxUse).setChecked(true);
                noteList = database.notesDao().getNotesListByDate();
                adapter.setmData(noteList, getParentFragment());
                break;
        }
        getSearchView().setVisibility(View.VISIBLE);
        drawable = getActivity().getDrawable(R.drawable.search_background);
        searchView.setBackground(drawable);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getActivity().getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
               View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
                if (view != null) {
                    hideSoftInput();
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
                adapter.setmData(searchList, getParentFragment());
                return true;
            }
        });
        searchView.setSubmitButtonEnabled(true);
        final int searchViewId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_go_btn", null, null);
        ImageView searchIcon = searchView.findViewById(searchViewId);
//        searchIcon.setImageResource(android.R.drawable.ic_menu_search);
         final int searchViewId2 = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchIconClose = searchView.findViewById(searchViewId2);
        searchIconClose.setOnClickListener(this);
        searchIcon.setOnClickListener(this);
   }

    @Override
    public void onClick(View v) {
        searchView.clearFocus();
        searchView.setQuery("", true);
        hideSoftInput();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchList = new ArrayList<>();
    }

    @NotNull
    private List<Notes> sortNewDateFirst(@NotNull List<Notes> list) {
        List<Notes> tempList = new ArrayList<>();
        int j = list.size() - 1;
        for (int i = j; i >= 0; i--) {
            tempList.add(list.get(i));
        }
        return tempList;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        searchView.setQuery("", true);
        searchView.clearFocus();
        hideSoftInput();
        switch (item.getItemId()) {
            case R.id.id_sort_name:
                item.setChecked(!item.isChecked());
                noteList = database.notesDao().getNotesListByName();
                adapter.setmData(noteList, getParentFragment());
                checkBoxUse = item.getItemId();
                break;
            case R.id.id_sort_datenew:
                item.setChecked(!item.isChecked());
                noteList = database.notesDao().getNotesListByDate();
                adapter.setmData(sortNewDateFirst(noteList), getParentFragment());
                checkBoxUse = item.getItemId();
                break;
            case R.id.id_sort_dateold:
                item.setChecked(!item.isChecked());
                noteList = database.notesDao().getNotesListByDate();
                adapter.setmData(noteList, getParentFragment());
                checkBoxUse = item.getItemId();
                break;
        }
        preferences.edit()
                .putInt(BOX_STATE, checkBoxUse)
                .apply();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void moveAdd(@NotNull Toolbar toolbar, @NotNull View view) {
        try {
            toolbar.addView(view);
        } catch (IllegalStateException | IllegalArgumentException ignore) {
        }
    }

    @Override
    public void delItemSearch(@NotNull Toolbar toolbar, View view) {
        toolbar.removeView(view);
    }



    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private final int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NotNull Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        noteList = database.notesDao().getNotesListByDate();
        recyclerView = view.findViewById(R.id.note_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(50));
        adapter = new RecycleViewAdapter(getContext(), noteList);
        noteList = sortNewDateFirst(noteList);
        adapter.setmData(noteList, getParentFragment());
        recyclerView.setAdapter(adapter);
        adapter.setClickInterface(new RecycleViewAdapter.ClickInterface() {
            @Override
            public void clickEventOne(Notes obj) {
                int id = obj.getId();
                String toast = database.notesDao().getNoteById(id).getNameNote();
                Toast.makeText(getContext(), "Просмотр записи: " + toast,
                        Toast.LENGTH_SHORT)
                        .show();
                delItemSearch(toolbar, searchView);
                NotesFragmentDirections.ActionNotesToInterpretation action =
                        NotesFragmentDirections.actionNotesToInterpretation(id);
                NavHostFragment.findNavController(NotesFragment.this)
                        .navigate(action);
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigation = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        bottomNavigation.getMenu()
                .getItem(0)
                .setIcon(R.drawable.ic_crescent)
                .setTitle(R.string.my_dreams);
        bottomNavigation.getMenu()
                .getItem(1)
                .setIcon(R.drawable.ic_book)
                .setTitle(R.string.dream_means);
        bottomNavigation.getMenu()
                .getItem(2)
                .setIcon(R.drawable.ic_settings_24)
                .setTitle(R.string.setting);
        bottomNavigation.setOnNavigationItemSelectedListener(MainActivity.getListnr());
        final FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_record_light);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigation.setVisibility(View.INVISIBLE);
                item.setVisible(true);
                fab.setVisibility(View.INVISIBLE);
                searchView.setVisibility(View.INVISIBLE);
                NavHostFragment.findNavController(NotesFragment.this).navigate(R.id.nav_record);
            }
        });
    }

    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(toolbar.getWindowToken(), 0);
    }
}
