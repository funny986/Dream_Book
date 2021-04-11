package com.dreambook;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dreambook.dataBase.Notes;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    public Context mContext;
    public List<Notes> mData;
    public Fragment fragment;

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setmData(List<Notes> Data) {
        this.mData = Data;
        notifyDataSetChanged();
    }

    public RecycleViewAdapter(Context context, List<Notes> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    public String note;
    private ClickInterface mClickInterface;

    public interface ClickInterface {
        void clickEventOne(Notes note);
        void onItemDeleted(Notes note, int position);
        void onItemEdit(Notes note);
    }

    public void setClickInterface(ClickInterface clickInterface) {
        mClickInterface = clickInterface;
    }

    private class MyClickListener implements View.OnClickListener{

        private int mPosition;
        private boolean mClickable;

        private void setPosition(int position) {
            mPosition = position;
        }

        private void setClickable() {
            mClickable = true;
        }

        @Override
        public void onClick(View v) {
            if (mClickable) {
                mClickInterface.clickEventOne(mData.get(mPosition));
            }
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_note, parent, false);
        return new MyViewHolder(view);
    }

    public void onItemDismiss(int position) {
        mData.remove(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        String name = mData.get(position).getNameNote();
        String dateItem = mData.get(position).getDate();
        Date date = new Date();
       DateFormat df = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        try {
            date = df.parse(dateItem);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dfy = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        assert date != null;
        String dateStr = dfy.format(date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayName = sdf.format(date);
        String label = "";
        if (!mData.get(position).getLabelNote().equals("")){
            label = "#" + mData.get(position).getLabelNote();
        }
        char[] temp = mData.get(position).getNote().toCharArray();
        StringBuilder textNote = new StringBuilder();
        int plotnW = holder.displayMetrics.widthPixels;
        int plotnH = holder.displayMetrics.heightPixels;
        float widthCard = (float) plotnH / plotnW;
        int countChar = 0;
        if (widthCard >= 1.6f && widthCard < 1.7f) countChar = 35;
        if (widthCard >= 1.7f && widthCard < 1.8f) countChar = 30;
        if (widthCard >= 1.8f && widthCard < 1.9f ) countChar = 39;
        if (widthCard >= 1.9f ) countChar = 34;
        for (int i = 0; i < countChar; i++){
            if (temp.length > i) {
                textNote.append(temp[i]);
            }
        }
        if (temp.length > countChar){
            textNote.append("...");
        }
        holder.noteName.setText(name);
        holder.noteDate.setText(dateStr);
        holder.dayOfWeek.setText(dayName);
        holder.noteLabels.setText(label);
        holder.noteText.setText(textNote.toString());
        holder.myClickListener.setClickable();
        holder.myClickListener.setPosition(position);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mClickInterface.onItemDeleted(mData.get(position), position);
            }
        });
        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickInterface.onItemEdit(mData.get(position));
            }
        });
      }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView noteName, noteDate, noteLabels, noteText;
        private final TextView dayOfWeek;
        private final MyClickListener myClickListener;
        public final ImageButton delete;
        private final ImageButton action;
        public TwoStepRightCoordinatorLayout coordinatorLayout;
        public DisplayMetrics displayMetrics;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coordinatorLayout =(TwoStepRightCoordinatorLayout) itemView;
            delete = itemView.findViewById(R.id.delete);
            action = itemView.findViewById(R.id.action);
            noteName = itemView.findViewById(R.id.card_note_name);
            noteDate = itemView.findViewById(R.id.card_note_date);
            dayOfWeek = itemView.findViewById(R.id.card_note_day);
            noteLabels = itemView.findViewById(R.id.card_note_label);
            noteText = itemView.findViewById(R.id.card_note_text);
            myClickListener = new MyClickListener();
            CardView cardView = itemView.findViewById(R.id.card_view);
            displayMetrics = Resources.getSystem().getDisplayMetrics();
            int halfscreen = displayMetrics.widthPixels / 4;
            cardView.setOnClickListener(myClickListener);
            delete.getLayoutParams().width = halfscreen;
            action.getLayoutParams().width = halfscreen;
        }
    }
}
