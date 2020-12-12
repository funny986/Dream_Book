package com.dreambook;

import android.app.Dialog;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dataBase.Words;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class RecycleViewAdptr extends RecyclerView.Adapter<RecycleViewAdptr.MyViewHolder> {

    public Context mContext;
    public List<Words> mData;
    Dialog myDialog;

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setmData(List<Words> Data) {
        this.mData = Data;
//        notifyDataSetChanged();
    }

    public RecycleViewAdptr(Context context, List<Words> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_note, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
//
//            final MyViewHolder myViewHolder = new MyViewHolder(view);
//
//            myDialog = new Dialog(parent.getContext());
//            myDialog.setContentView(R.layout.note_dialog);
//            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//            myViewHolder.item_note.setOnClickListener(new View.OnClickListener() {
//                @SuppressLint("SetTextI18n")
//                @Override
//                public void onClick(View v) {

//                    TextView dialog_lastname_tv = myDialog.findViewById(R.id.dialod_lastname_id);
//                    TextView dialog_firstname_tv = myDialog.findViewById(R.id.dialod_firstname_id);
//                    TextView dialog_middlename_tv = myDialog.findViewById(R.id.dialod_middlename_id);
//                    TextView dialog_position_tv = myDialog.findViewById(R.id.dialod_positionname_id);
//                    final TextView dialog_phone_tv = myDialog.findViewById(R.id.dialog_phone_id);
//                    TextView dialog_inner_phone = myDialog.findViewById(R.id.dialog_inner_phone_id);
//
//                    String mobile = mData.get(myViewHolder.getAdapterPosition()).getMobile();
//                    Button phone = myDialog.findViewById(R.id.dialog_button_name);
//                    Button msg = myDialog.findViewById(R.id.dialog_button_message);

//                    if (mobile != null){
//                        dialog_phone_tv.setVisibility(View.VISIBLE);
////                        inputNumberPhoneMask(dialog_phone_tv, true);
//                        dialog_phone_tv.setText(mobile);
//                        phone.setVisibility(View.VISIBLE);
//                        msg.setVisibility(View.VISIBLE);
//                    }
//                    else {
//                        dialog_phone_tv.setVisibility(View.INVISIBLE);
//                        phone.setVisibility(View.INVISIBLE);
//                        msg.setVisibility(View.INVISIBLE);
//                    }
//                    inputNumberPhoneMask(dialog_inner_phone, false);
//
//                    dialog_lastname_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getLastName());
//                    dialog_firstname_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getFirstName());
//                    dialog_middlename_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getMiddleName());
//                    dialog_position_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getPositionName());
//                    dialog_inner_phone.setText(mData.get(myViewHolder.getAdapterPosition()).getInnerPhone());
//                    Toast.makeText(mContext, "" + mData.get(myViewHolder.getAdapterPosition()).getLastName(),
//                            Toast.LENGTH_SHORT)
//                            .show();
//                    myDialog.show();

//                    phone.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(Intent.ACTION_DIAL);
//                            intent.setData(Uri.parse("tel:" + dialog_phone_tv.getText()));
//                            mContext.startActivity(intent);
//                        }
//                    });
//                    msg.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent sms = new Intent(Intent.ACTION_SENDTO);
//                            sms.setData(Uri.parse("smsto:" + dialog_phone_tv.getText()));
//                            mContext.startActivity(sms);
//                        }
//                    });
//                }
//            });
//            return myViewHolder;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        String name = mData.get(position).getWord();
        holder.word.setText(name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView word;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.card_note_name);
        }

        @Override
        public void onCreateContextMenu(@NonNull ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.add(this.getAdapterPosition(), R.id.edit_context , 0, "Править данные");
//            menu.add(this.getAdapterPosition(), R.id.delete_context, 0, "Удалить контакт");
        }
    }
}
