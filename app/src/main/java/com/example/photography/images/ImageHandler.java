package com.example.photography.images;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.photography.R;
import com.example.photography.activities.OwnerActivity2;
import com.example.photography.utils.Utilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageHandler {

    private static StorageReference mStorageRef;

    private static List<StorageReference> uriImages = null;

    public static void photoToOwner(String path, String dir, String filename, Activity activity){
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Uri file = Uri.fromFile(new File(path));
        StorageReference riversRef = mStorageRef.child(dir+File.separator+filename);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                        Toast.makeText(activity,"התמונה נשלחה לבעל האירוע.", Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(activity,"השליחה לבעל האירוע נכשלה,\n ודא שיש לך חיבור לאינטרנט.", Toast.LENGTH_SHORT);
                    }
                });
    }

    public static void downloadPhoto(String dir, String filename, Activity activity) throws IOException, InterruptedException {
        Log.v("Image", dir+filename);
        mStorageRef = FirebaseStorage.getInstance().getReference().child(dir+"/"+filename);
        File localFile = Utilities.createImageFileWithName(dir, filename);
        mStorageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        LayoutInflater inflater = LayoutInflater.from(builder.getContext());
                        View dialogView = inflater.inflate(R.layout.image_save_dialog,null);
                        ImageView iv = dialogView.findViewById(R.id.iv_image_save_dialog);
                        Glide.with(activity)
                                .asBitmap()
                                .load(localFile.getPath())
                                .error(R.drawable.ic_launcher_background)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(iv);
                        builder.setView(dialogView)
                                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                }).setNegativeButton("לא", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        localFile.delete();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0,5,0,15);
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
                        Toast.makeText(activity,"התמונה נשמרה בהצלחה.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity,"ההורדת התמונה נכשלה,\nודא שיש לך חיבור לאינטרנט.", Toast.LENGTH_SHORT).show();
                        Log.v("Image", exception.getMessage());
            }
        });
        mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "התמונה נמחקה מהאיחסון", Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "מחיקת התמונה מהאיחסון נכשלה,\nודא שיש לך חיבור לאינטרנט.", Toast.LENGTH_SHORT);
            }
        });
    }

    public static void downloadAllPhotos(String dir, Activity activity) throws IOException {
        Log.v("MENUS CLASS" ,"start");
        List<StorageReference> allPhotos = getStorageImage(dir, activity);
        Log.v("MENUS CLASS" ,"second");
        Log.v("MENUS CLASS" ,dir);
        if(allPhotos != null && !allPhotos.isEmpty()) {
            for (int i = 0; i < allPhotos.size(); i++) {
                try {
                    downloadPhoto(dir, allPhotos.get(i).getName(), activity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"ההורדת התמונה נכשלה,\nודא שיש לך חיבור לאינטרנט.", Toast.LENGTH_SHORT).show();
                }
                Log.v("Image...", allPhotos.get(i).getName());
            }
        }
    }

    public static List<StorageReference> getStorageImage(String dir, Activity activity){

        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference listRef = mStorageRef.child(dir);
        Log.v("Image" ,"storage image");
        Log.v("Image", dir);
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                Log.v("Image", ""+listResult.getItems().size());
                uriImages = listResult.getItems();

                /*for(int i=0;  i<listResult.getItems().size(); i++){
                    uriImages.add(listResult.getItems().get(i).getPath());
                    Log.v("Image", listResult..getItems().get(i).getPath());
                }*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity,"ודא שיש לך חיבור לאינטרנט ולחץ על רענון.", Toast.LENGTH_SHORT);
            }
        });
        return uriImages;
    }

}
