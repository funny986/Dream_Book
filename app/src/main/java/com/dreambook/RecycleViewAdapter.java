package com.dreambook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dreambook.dataBase.Notes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>
        {

    public Context mContext;
    public List<Notes> mData;
    public Fragment fragment;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setmData(List<Notes> Data, Fragment fragment) {
        this.mData = Data;
        this.fragment = fragment;
        notifyDataSetChanged();
    }

    public RecycleViewAdapter(Context context, List<Notes> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    public String note;
    private ClickInterface mClickInterface;

            public interface ClickInterface {
                void clickEventOne(Notes obj);
            }

            public void setClickInterface(ClickInterface clickInterface) {
                mClickInterface = clickInterface;
            }

            private class MyClickListener implements View.OnClickListener {

                private int mPosition;
                private boolean mClickable;

                void setPosition(int position) {
                    mPosition = position;
                }

                void setClickable(boolean clickable) {
                    mClickable = clickable;
                }

                @Override
                public void onClick(View v) {
                    if(mClickable) {
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
                notifyItemRemoved(position);
            }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        String name = mData.get(position).getNameNote();
        String date = mData.get(position).getDate();
        String label = mData.get(position).getLabelNote();
            holder.noteName.setText(name);
            holder.noteDate.setText(date);
            holder.noteLabels.setText(label);
            holder.myClickListener.setClickable(true);
            holder.myClickListener.setPosition(position);
//            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//                @SuppressLint("ClickableViewAccessibility")
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
////                        fragment.
//                    }
//                    return false;
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


    protected class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
             {

        private final TextView noteName, noteDate, noteLabels;
        MyClickListener myClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteName = itemView.findViewById(R.id.card_note_name);
            noteDate = itemView.findViewById(R.id.card_note_date);
            noteLabels = itemView.findViewById(R.id.card_note_label);
            myClickListener = new MyClickListener();
            itemView.setOnClickListener(myClickListener);
        }

        @Override
        public void onCreateContextMenu(@NonNull ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.add(this.getAdapterPosition(), R.id.edit_context , 0, "Править данные");
//            menu.add(this.getAdapterPosition(), R.id.delete_context, 0, "Удалить контакт");
        }
    }
}
