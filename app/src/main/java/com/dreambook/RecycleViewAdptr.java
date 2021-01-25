package com.dreambook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dreambook.dataBase.Words;

import java.util.List;

public class RecycleViewAdptr extends RecyclerView.Adapter<RecycleViewAdptr.MyViewHolder> {

    public Context mContext;
    public List<Words> mData;
    private ClickInterfaceWord mClickInterface;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickInterfaceWord {
        void clickEventOne(Words word);
    }

    public void setClickInterfaceWord(ClickInterfaceWord clickInterface) {
        mClickInterface = clickInterface;
    }

    private class WordClickListener implements View.OnClickListener {

        private int mPosition;
        private boolean mClickable;

        void setPosition(int position) {
            mPosition = position;
        }

        int getmPosition(){
            return mPosition;
        }

        void setClickable(boolean clickable) {
            mClickable = clickable;
        }

        @Override
        public void onClick(View v) {
            if (mClickable) {
                mClickInterface.clickEventOne(mData.get(mPosition));
            }
        }
    }

    public void setmData(List<Words> Data) {
        this.mData = Data;
        notifyDataSetChanged();
    }

    public RecycleViewAdptr(Context context, List<Words> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_word, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        String name = mData.get(position).getWord();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        holder.word.setText(name);
        holder.myClickListener.setClickable(true);
        holder.myClickListener.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        WordClickListener myClickListener;
        private final TextView word;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.word_tv);
            myClickListener = new WordClickListener();
            itemView.setOnClickListener(myClickListener);
        }
    }
}
