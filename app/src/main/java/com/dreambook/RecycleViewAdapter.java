package com.dreambook;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import dataBase.Notes;

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
//            final MyViewHolder myviewholder = new MyViewHolder(view);
//            myviewholder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////                    int posit = myviewholder.getAdapterPosition();
//                    String arg1 = myviewholder.noteName.getText().toString();
//                    String arg2 = myviewholder.noteDate.getText().toString();
//
////                   String arg = database.notesDao().getNoteById(posit);
//                        Toast.makeText(mContext, "Просмотр записи: " + myviewholder.noteName.getText().toString(),
//                                Toast.LENGTH_SHORT)
//                                .show();
//                NotesFragmentDirections.ActionNotesToInterpretation action =
//                        NotesFragmentDirections.actionNotesToInterpretation(arg1, arg2);
//                NavHostFragment.findNavController(fragment)
//                        .navigate(action);
//                }
//            });
           return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        String name = mData.get(position).getNameNote();
        String date = mData.get(position).getDate();
            holder.noteName.setText(name);
            holder.noteDate.setText(date);
            holder.myClickListener.setClickable(true);
            holder.myClickListener.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


    protected class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
             {

        private final TextView noteName, noteDate;
        MyClickListener myClickListener;
        View v1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteName = itemView.findViewById(R.id.card_note_name);
            noteDate = itemView.findViewById(R.id.card_note_date);
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
