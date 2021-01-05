package com.dreambook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import dataBase.Words;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class RecycleViewAdptr extends RecyclerView.Adapter<RecycleViewAdptr.MyViewHolder> {

    public Context mContext;
    public List<Words> mData;
    public Fragment myFragment;

    public void setmData(List<Words> Data, Fragment fragment) {
        this.mData = Data;
        this.myFragment = fragment;
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
        final MyViewHolder myviewholder = new MyViewHolder(view);
        myviewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String arg = myviewholder.word.getText().toString();
                Toast.makeText(mContext, "Толкование  " + arg,
                        Toast.LENGTH_SHORT)
                        .show();
                MeansFragmentDirections.ActionWordToMean action =
                        MeansFragmentDirections.actionWordToMean(arg);
                NavHostFragment.findNavController(myFragment)
                        .navigate(action);
            }
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        String name = mData.get(position).getWord();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        holder.word.setText(name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView word;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.word_tv);
        }
    }
}
