package com.example.photography.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.photography.R;
import com.example.photography.adapters.RecyclerAdapter;
import com.example.photography.database.Event;
import com.example.photography.images.ImageHandler;
import com.example.photography.utils.FontAwesome;
import com.example.photography.utils.Menus;
import com.example.photography.utils.Utilities;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

//Guest activity2 class (activity)
public class GuestActivity2 extends AppCompatActivity {

    private final String TAG = GuestActivity2.class.getName();
    //request image capture code
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //array list for bitmap
    private ArrayList<Bitmap> images= new ArrayList<>();
    //file for (jpg) image
    File currentFile = null;

    private Event event;

    private Toolbar toolbar;

    private FirebaseUser user;

    //------------------------
    private ArrayList<String> imageUrls = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<String> imgToDelete = new ArrayList<>();
    //------------------------

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    //onCreate function that create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        //call parent constructor
        super.onCreate(savedInstanceState);
        //load the xml file
        setContentView(R.layout.activity_guest2);

        user = getIntent().getParcelableExtra("user");

        //Intent intentDate = getIntent();
        Gson gson = new Gson();
        event =  gson.fromJson(getIntent().getStringExtra("event"), Event.class);
        Log.v(TAG, event.getName());

        toolbar = findViewById(R.id.ag2_toolbar);
        toolbar.setLogo(R.drawable.logo_toolbar);
        toolbar.setTitle(event.getName());
        setSupportActionBar(toolbar);

        //for font awesome
        Typeface iconFont = FontAwesome.getTypeface(getApplicationContext(), FontAwesome.FONTAWESOME);
        Button cb = findViewById(R.id.cameraButton);
        cb.setTypeface(iconFont);
        Button delB = findViewById(R.id.deleteButton);
        delB.setTypeface(iconFont);

        recyclerView = findViewById(R.id.recycler_view_guest);
        adapter = new RecyclerAdapter(this, imageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        params.setMargins(0,5,0,15);

        //check the version to avoid from the common bug
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        //move to camera by click on camera button
        findViewById(R.id.cameraButton).setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });

        findViewById(R.id.deleteButton).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(GuestActivity2.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
            builder.setTitle(R.string.delete)
                    .setMessage("האם ברצונך למחוק את כל התמונות?")
                    .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            File photographyAppDir = new File(Utilities.getOrCreateFolder(event.getName()+"-"+event.getId()));
                            File[] imgFiles = photographyAppDir.listFiles();
                            for(int j = 0; j < imgFiles.length; j++){
                                imgFiles[j].delete();
                            }
                            imageUrls.clear();
                            adapter.notifyDataSetChanged();
                            //Toast.makeText(OwnerActivity2.this, "התמונות נמחקו.", Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton("לא", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            params.setMargins(0,5,0,15);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.button_selector);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(params);
            LinearLayout.LayoutParams lpp = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).getLayoutParams();
            lpp.gravity = Gravity.CENTER;
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.button_selector);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setLayoutParams(params);
            LinearLayout.LayoutParams lpp1 = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).getLayoutParams();
            lpp1.gravity = Gravity.CENTER;
        });

        initImageBitmap();
    }

    private void initImageBitmap(){
        File photographyAppDir = new File(Utilities.getOrCreateFolder(event.getName()+"-"+event.getId()));
        //String[] photographyAppFiles = photographyAppDir.list();
        File[] imgFiles = photographyAppDir.listFiles();
        for(int i = 0; i < imgFiles.length; i++){
            imageUrls.add(imgFiles[i].getPath());
        }
        adapter.notifyDataSetChanged();
    }



    //Take a photo with a camera app
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Continue only if the File was successfully created
            try {
                currentFile =  Utilities.createImageFile(event.getName()+"-"+event.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //put the file on the intent
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentFile));
            //go to camera
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    //get the image from the camera and 1)show it on the grid view
    //                                  2)and save it on the list
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bitmap bitmap = Utilities.getBitmapFromFile(currentFile.getPath());
                images.add(bitmap);
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(currentFile));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageHandler.photoToOwner(currentFile.getPath() ,event.getName()+"-"+event.getId() ,currentFile.getName(), this);
            imageUrls.add(currentFile.getPath());
            adapter.notifyDataSetChanged();
        }else{
            currentFile.delete();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if(user != null){
            menuInflater.inflate(R.menu.toolbar_regester_menu, menu);
        }else {
            menuInflater.inflate(R.menu.toolbar_guest_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(user != null){
            Menus.registerMenu(this,item,user.getEmail());
        }else {
            Menus.unregisterMenu(this, item);
        }
        return true;
    }

}
