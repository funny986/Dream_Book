package com.dreambook;

import android.content.Context;
import android.content.res.Resources;
import android.view.*;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dreambook.dataBase.Notes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

        private void setClickable(boolean clickable) {
            mClickable = clickable;
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
        String date = mData.get(position).getDate();
        String label = mData.get(position).getLabelNote();
        holder.noteName.setText(name);
        holder.noteDate.setText(date);
        holder.noteLabels.setText(label);
        holder.myClickListener.setClickable(true);
        holder.myClickListener.setPosition(position);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mClickInterface.onItemDeleted(mData.get(position), position);
//            onItemDismiss(position);
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

        private final TextView noteName, noteDate, noteLabels;
        MyClickListener myClickListener;
        public final ImageButton delete;
        private final ImageButton action;
        private final CardView cardView;
        public TwoStepRightCoordinatorLayout coordinatorLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coordinatorLayout =(TwoStepRightCoordinatorLayout) itemView;
            delete = itemView.findViewById(R.id.delete);
            action = itemView.findViewById(R.id.action);
            noteName = itemView.findViewById(R.id.card_note_name);
            noteDate = itemView.findViewById(R.id.card_note_date);
            noteLabels = itemView.findViewById(R.id.card_note_label);
            myClickListener = new MyClickListener();
            cardView = itemView.findViewById(R.id.card_view);
            int halfscreen = Resources.getSystem().getDisplayMetrics().widthPixels / 4;
            cardView.setOnClickListener(myClickListener);
            delete.getLayoutParams().width = halfscreen;
            action.getLayoutParams().width = halfscreen;
        }
    }
}
