package com.dreambook.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.dreambook.R;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.dreambook.MainActivity.database;

public class ExitDialog extends DialogFragment {


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Закрыть Сонник?")
                .setIcon(R.drawable.ic_cancel_keys)
                .setCancelable(true)
                .setPositiveButton("Выйти из приложения", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.close();
                        Objects.requireNonNull(getActivity()).finishAffinity();
                    }
                });
        return builder.create();
            }
}
