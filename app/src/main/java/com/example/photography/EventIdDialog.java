package com.example.photography;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import java.util.LongSummaryStatistics;
import java.util.zip.Inflater;
//Event id dialog class (dialog)
public class EventIdDialog extends DialogFragment {

    //onCreateDialog function that create the dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        //connect the view xml with the builder and set the buttons
        builder.setView(inflater.inflate(R.layout.event_id_dialog,null))
                //set continue button
                .setPositiveButton(R.string.enter_event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                //set cancel button
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        //return the builder and create dialog
        return builder.create();
    }
}
