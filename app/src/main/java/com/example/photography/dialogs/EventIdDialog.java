package com.example.photography.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.photography.R;
import com.example.photography.activities.GuestActivity2;
import com.example.photography.database.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Event id dialog class (dialog)
public class EventIdDialog extends DialogFragment {

    private DatabaseReference myRef;
    Event event;

    //onCreateDialog function that create the dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.event_id_dialog,null);

        EditText editText = dialogView.findViewById(R.id.dialogEditText);
        myRef = FirebaseDatabase.getInstance().getReference().child("Event");

        //connect the view xml with the builder and set the buttons
        builder.setView(dialogView)
                //set continue button
                .setPositiveButton(R.string.enter_event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.child(editText.getText().toString()).exists()){
                                    editText.setError("לא נמצא אירוע.");
                                    Toast.makeText(getActivity(), "לא נמצא אירוע.", Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    event = (Event)(dataSnapshot.child(editText.getText().toString()).getValue());
                                    Intent intent = new Intent(getContext(), GuestActivity2.class);
                                    //intent.putExtra("event", event);
                                    startActivity(intent);
                                    dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //move to GuestActivity2 by click on event
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
