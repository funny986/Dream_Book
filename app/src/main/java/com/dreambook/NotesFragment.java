package com.dreambook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.SharedPreferences;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.dreambook.dataBase.Notes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.dreambook.MainActivity.*;

public class NotesFragment extends Fragment implements View.OnClickListener {

    public RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public RecycleViewAdapter adapter;

    private List<Notes> noteList, searchList;
    public BottomNavigationView bottomNavigation;
    int checkBoxUse;
    private FloatingActionButton fab;
    public SearchView searchView;
    public Drawable drawable;
    Activity activity;
    SharedPreferences preferences;
    public int genderForNote;

    public NotesFragment() {
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onResume() {
        super.onResume();
        genderForNote = activity
                .getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE)
                .getInt(AUTOR_GENDER, 0);
        bottomNavigation.setVisibility(View.VISIBLE);
        searchList = new ArrayList<>();
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_record_dark,
                Objects.requireNonNull(getContext()).getTheme()));

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
        preferences = requireActivity().getPreferences(MODE_PRIVATE);
        checkBoxUse = preferences.getInt(BOX_STATE, 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.main, menu);
//        item = menu.findItem(R.id.save_note);
//        item.setVisible(false);
//        recVoice = menu.findItem(R.id.record_voice);
//        recVoice.setVisible(false);
//        sort = menu.findItem(R.id.sorting);
//        checkBoxUse = preferences.getInt(BOX_STATE, 0);
//        switch (checkBoxUse) {
//            case 0:
//                MenuItem menuItem = menu.findItem(R.id.id_sort_datenew);
//                menuItem.setChecked(true);
//                checkBoxUse = menuItem.getItemId();
//                break;
//            case R.id.id_sort_name:
//                menu.findItem(checkBoxUse).setChecked(true);
//                noteList = database.notesDao().getNotesListByName();
//                adapter.setmData(noteList, getParentFragment());
//                break;
//            case R.id.id_sort_datenew:
//                menu.findItem(checkBoxUse).setChecked(true);
//                noteList = database.notesDao().getNotesListByDate();
//                adapter.setmData(sortNewDateFirst(noteList), getParentFragment());
//                break;
//            case R.id.id_sort_dateold:
//                menu.findItem(checkBoxUse).setChecked(true);
//                noteList = database.notesDao().getNotesListByDate();
//                adapter.setmData(noteList, getParentFragment());
//                break;
//        }

    @Override
    public void onClick(View v) {
        searchView.clearFocus();
        searchView.setQuery("", true);
        hideSoftInput();
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

//        switch (item.getItemId()) {
//            case R.id.id_sort_name:
//                item.setChecked(!item.isChecked());
//                noteList = database.notesDao().getNotesListByName();
//                adapter.setmData(noteList, getParentFragment());
//                checkBoxUse = item.getItemId();
//                break;
//            case R.id.id_sort_datenew:
//                item.setChecked(!item.isChecked());
//                noteList = database.notesDao().getNotesListByDate();
//                adapter.setmData(sortNewDateFirst(noteList), getParentFragment());
//                checkBoxUse = item.getItemId();
//                break;
//            case R.id.id_sort_dateold:
//                item.setChecked(!item.isChecked());
//                noteList = database.notesDao().getNotesListByDate();
//                adapter.setmData(noteList, getParentFragment());
//                checkBoxUse = item.getItemId();
//                break;
//        }
//        preferences.edit()
//                .putInt(BOX_STATE, checkBoxUse)
//                .apply();
//        return super.onOptionsItemSelected(item);
//    }


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
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
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
                NotesFragmentDirections.ActionNotesToInterpretation action =
                        NotesFragmentDirections.actionNotesToInterpretation(id);
                NavHostFragment.findNavController(NotesFragment.this)
                        .navigate(action);
            }
        });
        setHasOptionsMenu(true);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = ItemTouchHelper.START ;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.onItemDismiss(viewHolder.getAdapterPosition());
            }
        };
       mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        searchView =view.findViewById(R.id.search_in);
        drawable = Objects.requireNonNull(getActivity()).getDrawable(R.drawable.search_background);
        searchView.setBackground(drawable);
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
                    temp += tempCont.getLabelNote();
                    if (temp.toLowerCase().contains(searchText.toLowerCase())) {
                        tempString.add(tempCont);
                    }
                }
                searchList = tempString;
                adapter.setmData(searchList, getParentFragment());
                return true;
            }
        });
         final int searchViewId2 = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchIconClose = searchView.findViewById(searchViewId2);
        searchIconClose.setOnClickListener(this);
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
        return view;
    }

    ItemTouchHelper mItemTouchHelper;

    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigation = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(MainActivity.getListnr());
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigation.setVisibility(View.INVISIBLE);
//                fab.setVisibility(View.INVISIBLE);
                searchView.setVisibility(View.INVISIBLE);
                NavHostFragment.findNavController(NotesFragment.this).navigate(R.id.nav_record);
            }
        });
    }

    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }
}
