package com.dreambook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.transition.Fade;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InfoFragment extends Fragment {

    public InfoFragment() {
    }
    @Override
    public void onSaveInstanceState(@NotNull Bundle outSt) {
        super.onSaveInstanceState(outSt);
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity()
                .findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(2);
        item.setChecked(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private Activity activity;
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
        this.setSharedElementEnterTransition(new DetailsTransition());
        this.setEnterTransition(new Fade());
        setExitTransition(new Fade());
        this.setSharedElementReturnTransition(new DetailsTransition());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        FloatingActionButton fab = activity.findViewById(R.id.fab);
        if (fab != null) fab.setVisibility(View.INVISIBLE);
        TextView title = view.findViewById(R.id.meanword_tv);
        title.setText(" О Приложении");
        Button btnBack = view.findViewById(R.id.button_exit);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }
}