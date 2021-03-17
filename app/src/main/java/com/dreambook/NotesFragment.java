package com.dreambook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.SharedPreferences;
import android.widget.*;

import com.xenione.libs.swipemaker.SwipeLayout;
import org.jetbrains.annotations.NotNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.dreambook.dataBase.Notes;
import static com.dreambook.MainActivity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesFragment extends Fragment implements View.OnClickListener, RecyclerView.OnItemTouchListener,
                                   TwoStepRightCoordinatorLayout.CloseSwipe {

    public RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public RecycleViewAdapter adapter;
    private List<Notes> noteList, searchList;
    private BottomNavigationView bottomNavigation;
    int checkBoxUse;
    int genderForNote;
    private int position;

    private FloatingActionButton fab;
    public SearchView searchView;
    private Drawable drawable;
    private Activity activity;
    public SharedPreferences preferences;
    private PopupMenu popup;

    public NotesFragment() {}

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
        Menu menu = bottomNavigation.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
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

    public void onCreatePopupMenu(View view) {
       popup = new PopupMenu(getContext(), view);
        popup.inflate(R.menu.menu_sort);
        Menu menu = popup.getMenu();
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
                adapter.setmData(noteList);
                break;
            case R.id.id_sort_datenew:
                menu.findItem(checkBoxUse).setChecked(true);
                noteList = database.notesDao().getNotesListByDate();
                adapter.setmData(sortNewDateFirst(noteList));
                break;
            case R.id.id_sort_dateold:
                menu.findItem(checkBoxUse).setChecked(true);
                noteList = database.notesDao().getNotesListByDate();
                adapter.setmData(noteList);
                break;
        }
    }

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

    private void showMenu(View v) {
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_sort_name:
                        item.setChecked(!item.isChecked());
                        noteList = database.notesDao().getNotesListByName();
                        adapter.setmData(noteList);
                        checkBoxUse = item.getItemId();
                        break;
                    case R.id.id_sort_datenew:
                        item.setChecked(!item.isChecked());
                        noteList = database.notesDao().getNotesListByDate();
                        adapter.setmData(sortNewDateFirst(noteList));
                        checkBoxUse = item.getItemId();
                        break;
                    case R.id.id_sort_dateold:
                        item.setChecked(!item.isChecked());
                        noteList = database.notesDao().getNotesListByDate();
                        adapter.setmData(noteList);
                        checkBoxUse = item.getItemId();
                        break;
            }
                 preferences.edit()
                .putInt(BOX_STATE, checkBoxUse)
                .apply();
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void closeSwipe(@NotNull SwipeLayout layout) {
        layout.translateTo(1);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        assert childView != null;
        position = rv.getChildLayoutPosition(childView);
        int itemCount = rv.getChildCount();
        View notFocusView;
        ImageButton del = null;
        try {
            del = childView.findViewById(R.id.delete);
        }
        catch (NullPointerException ignored){
        }
        for (int i = 0; i < itemCount; i++){
            SwipeLayout swl;
            if (i != position){
                notFocusView = rv.getChildAt(i);
                swl = notFocusView.findViewById(R.id.foregroundView);
                closeSwipe(swl);
            }
            else {
                assert del != null;
                if (del.isPressed()){
                    notFocusView = rv.getChildAt(position);
                    swl = notFocusView.findViewById(R.id.foregroundView);
                    closeSwipe(swl);
                }
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
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
        final View view = inflater.inflate(R.layout.fragment_notes, container, false);
        noteList = database.notesDao().getNotesListByDate();
        recyclerView = view.findViewById(R.id.note_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(3));
        adapter = new RecycleViewAdapter(getContext(), noteList);
        noteList = sortNewDateFirst(noteList);
        adapter.setmData(noteList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(this);
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
            @Override
            public void onItemDeleted(final Notes note, final int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Удалить?")
                        .setIcon(R.drawable.ic_cancel_keys)
                        .setCancelable(true)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String toast = note.getNameNote();
                                Toast.makeText(getContext(), "Удаление записи: " + toast,
                                        Toast.LENGTH_SHORT)
                                        .show();
                                database.notesDao().delete(note);
                                NotesFragment.this.position = pos;
                                adapter.onItemDismiss(pos);
                                adapter.notifyDataSetChanged();
                                Log.i("Closeswipe", "Delpress OK: "  + " itemCount:" + adapter.getItemCount());
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Closeswipe", "Delpress Cancel: "  + " itemCount:" + adapter.getItemCount());
                            }
                        });
                builder.create();
                builder.show();
            }
            @Override
            public void onItemEdit(Notes note) {
                int id = note.getId();
                fab = requireActivity().findViewById(R.id.fab);
                fab.setVisibility(View.INVISIBLE);
                NotesFragmentDirections.ActionNotesToEdit action =
                        NotesFragmentDirections.actionNotesToEdit(id);
                NavHostFragment.findNavController(NotesFragment.this)
                        .navigate(action);
            }
        });
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
                for (int i = 0; i < noteList.size(); i++) {
                    Notes tempCont = noteList.get(i);
                    String temp = tempCont.getNameNote();
                    temp += tempCont.getLabelNote();
                    if (temp.toLowerCase().contains(searchText.toLowerCase())) {
                        tempString.add(tempCont);
                    }
                    else {
                        adapter.notifyItemRemoved(i);
                    }
                }
                searchList = tempString;
                adapter.setmData(searchList);
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

        ImageButton btnsort = view.findViewById(R.id.button_sort);
        onCreatePopupMenu(btnsort);
        btnsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
        return view;
    }

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
