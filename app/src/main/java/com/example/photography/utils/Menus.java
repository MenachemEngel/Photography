package com.example.photography.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.photography.activities.EmailActivity;
import com.example.photography.activities.LoginActivity;
import com.example.photography.activities.MainActivity;
import com.example.photography.R;
import com.example.photography.activities.OwnerActivity;
import com.example.photography.activities.RegisterActivity;
import com.example.photography.database.Event;
import com.example.photography.images.ImageHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class Menus {

    private static LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    LinearLayout.LayoutParams.WRAP_CONTENT,
    LinearLayout.LayoutParams.WRAP_CONTENT
    );

    public static void registerMenu(Activity activity, MenuItem item, String email){
        params.setMargins(0,5,0,15);
        switch (item.getItemId()){
            case R.id.mr_about:
                about(activity);
                break;
            case R.id.mr_help:
                registerHelp(activity, email);
                break;
            case R.id.m_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                break;
        }
    }

    public static void ownerEventMenu(Activity activity, MenuItem item, FirebaseUser user, String dir, Event event) throws IOException {
        params.setMargins(0,5,0,15);
        File photographyAppDir = new File(dir);
        File[] imgFiles = photographyAppDir.listFiles();
        switch (item.getItemId()){
            case R.id.moe_about:
                about(activity);
                break;
            case R.id.moe_help:
                registerHelp(activity, user.getEmail());
                break;
            case R.id.moe_poster:
                int i;
                for(i=0; i < imgFiles.length; i++){
                    if(imgFiles[i].getPath().contains("Poster")){
                        Toast.makeText(activity, "הפוסטר כבר קיים.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(i<imgFiles.length && imgFiles[i].getPath().contains("Poster")){
                    break;
                }
                Utilities.createPoster(activity,dir,event.getName()+"\n\n"+"מזהה אירוע:"+"\n"+event.getId());
                Toast.makeText(activity, "הפוסטר נוצר, רענן כדי לראות אותו.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.moe_delete_event:
                delete_event(activity, event, imgFiles, user);
                break;
            case R.id.moe_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                break;
        }
    }

    public static void unregisterMenu(Activity activity, MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.mg_about:
                about(activity);
                break;
            case R.id.mg_help:
                unregisterHelp(activity);
                break;
            case R.id.mg_login:
                intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.mg_reg:
                intent = new Intent(activity, RegisterActivity.class);
                activity.startActivity(intent);
                break;
        }
    }

    private static void about(Activity activity){
        params.setMargins(0,5,0,15);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ImageView img = new ImageView(activity);
        img.setImageResource(R.drawable.hac_and_cs);
        builder.setTitle(R.string.about)
                .setMessage(R.string.about_text)
                .setPositiveButton("חזור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setView(img);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.button_selector);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.black));
        LinearLayout.LayoutParams lpp = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).getLayoutParams();
        lpp.gravity = Gravity.CENTER;
    }

    private static void registerHelp(Activity activity, String email){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String [] help = {"בקשה לאיפוס סיסמה","שליחת הודעה למערכת"};
        builder.setTitle(R.string.help)
                .setItems(help, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(activity, "נשלחה אליך הודעה לאימייל לאיפוס סיסמה", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else{
                            Intent intent = new Intent(activity, EmailActivity.class);
                            intent.putExtra("email", email);
                            activity.startActivity(intent);
                        }
                    }
                });
        AlertDialog alertDialog1 = builder.create();
        alertDialog1.show();
    }

    private static void unregisterHelp(Activity activity){
        params.setMargins(0,5,0,15);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(builder.getContext());
        View dialogView = inflater.inflate(R.layout.forgot_pass_dialog,null);
        EditText det = dialogView.findViewById(R.id.et_fpd);
        builder.setView(dialogView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(det.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(activity, "נשלחה אליך הודעה לאימייל לאיפוס סיסמה", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
        AlertDialog alertDialog1 = builder.create();
        alertDialog1.show();
        alertDialog1.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.button_selector);
        alertDialog1.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.black));
        LinearLayout.LayoutParams lpp = (LinearLayout.LayoutParams) alertDialog1.getButton(AlertDialog.BUTTON_POSITIVE).getLayoutParams();
        lpp.gravity = Gravity.CENTER;
    }

    private static void delete_event(Activity activity, Event event, File[] imgArr, FirebaseUser user){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Event");
        Intent intent = new Intent(activity, OwnerActivity.class);
        params.setMargins(0,5,0,15);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.delete_event)
                .setMessage("האם ברצונך למחוק את האירוע עם התמונות?")
                .setPositiveButton("מחק עם התמונות", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j = 0; j < imgArr.length; j++){
                            imgArr[j].delete();
                        }
                        ref.child(String.valueOf(event.getId())).removeValue();
                        intent.putExtra("user", user);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }).setNegativeButton("מחק בלי התמונות", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ref.child(String.valueOf(event.getId())).removeValue();
                        intent.putExtra("user", user);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }).setNeutralButton("חזור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.button_selector);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(params);
        LinearLayout.LayoutParams lpp = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).getLayoutParams();
        lpp.gravity = Gravity.CENTER;
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.button_selector);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setLayoutParams(params);
        LinearLayout.LayoutParams lpp1 = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).getLayoutParams();
        lpp1.gravity = Gravity.CENTER;
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundResource(R.drawable.button_selector);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(activity.getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setLayoutParams(params);
        LinearLayout.LayoutParams lpp2 = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).getLayoutParams();
        lpp2.gravity = Gravity.CENTER;
    }
}
