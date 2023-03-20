package com.example.photography.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.example.photography.adapters.RecyclerAdapter2;
import com.example.photography.database.Event;
import com.example.photography.images.ImageHandler;
import com.example.photography.utils.FontAwesome;
import com.example.photography.utils.Menus;
import com.example.photography.utils.Utilities;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OwnerActivity2 extends AppCompatActivity {

    private final String TAG = OwnerActivity2.class.getName();
    //request image capture code
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //array list for bitmap
    private ArrayList<Bitmap> images= new ArrayList<>();

    //file for (jpg) image
    File currentFile = null;

    //------------------------
    private ArrayList<String> imageUrls = new ArrayList<>();
    //private ArrayList<String> imageStorageUrls = new ArrayList<>();
    private RecyclerView recyclerView;//, recyclerView2;
    private RecyclerAdapter adapter;
    //private RecyclerAdapter2 adapter2;
    private ArrayList<String> imgToDelete = new ArrayList<>();
    //------------------------

    private Event event;

    private FirebaseUser user;

    private Toolbar toolbar;

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

     private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner2);

        Gson gson = new Gson();
        event =  gson.fromJson(getIntent().getStringExtra("event"), Event.class);

        user = getIntent().getParcelableExtra("user");

        params.setMargins(0,5,0,15);

        toolbar = findViewById(R.id.ao2_toolbar);
        toolbar.setLogo(R.drawable.logo_toolbar);
        toolbar.setTitle(event.getName());
        setSupportActionBar(toolbar);

        //for font awesome
        Typeface iconFont = FontAwesome.getTypeface(getApplicationContext(), FontAwesome.FONTAWESOME);

        Button cb = findViewById(R.id.cameraButton_owner2);
        cb.setTypeface(iconFont);
        Button delB = findViewById(R.id.deleteButton_owner2);
        delB.setTypeface(iconFont);
        Button colB = findViewById(R.id.chooseButton_owner2);
        colB.setTypeface(iconFont);
        Button refB = findViewById(R.id.refreshButton_owner2);
        refB.setTypeface(iconFont);

        recyclerView = findViewById(R.id.recycler_view_owner);
        //recyclerView2 = findViewById(R.id.recycler_view_owner2);
        adapter = new RecyclerAdapter(this, imageUrls);
        //adapter2 = new RecyclerAdapter2(this, imageStorageUrls);
        recyclerView.setAdapter(adapter);
        //recyclerView.setAdapter(adapter2);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //recyclerView2.setLayoutManager(new GridLayoutManager(this, 3));

        pd = new ProgressDialog(OwnerActivity2.this);
        pd.setMessage("טוען...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setIndeterminate(true);

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
        findViewById(R.id.cameraButton_owner2).setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });

        findViewById(R.id.deleteButton_owner2).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(OwnerActivity2.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
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

        findViewById(R.id.chooseButton_owner2).setOnClickListener(v -> {
            Log.v(TAG, "in choose button");

            if(!pd.isShowing())
                pd.show();
            Thread dThread = new Thread(){
                @Override
                public void run() {
                    try {
                        ImageHandler.downloadAllPhotos(event.getName()+"-"+event.getId(),OwnerActivity2.this);
                    } catch (IOException e) {
                        Toast.makeText(OwnerActivity2.this, "ההורדת התמונות נכשלה,\n ודא שיש לך חיבור לאינטרנט.", Toast.LENGTH_LONG).show();
                    }
                    if(pd.isShowing())
                        pd.dismiss();
                }
            };
            dThread.start();

            /*lertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("הורדת ושמירה")
                    .setPositiveButton("הורד את התמונה", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                String filePath = recyclerView.getAdapter().toString();
                                String [] s = filePath.split("/");
                                Log.v("ImageHandler", s.toString());
                                ImageHandler.downloadPhoto(event.getName(), s[s.length-1],OwnerActivity2.this);
                            } catch (IOException e) {
                                Toast.makeText(OwnerActivity2.this, "ההורדת התמונה נכשלה,\n ודא שיש לך חיבור לאינטרנט.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setNegativeButton("הורד את כל התמונות", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                ImageHandler.downloadAllPhotos(event.getName(),OwnerActivity2.this);
                            } catch (IOException e) {
                                Toast.makeText(OwnerActivity2.this, "ההורדת התמונות נכשלה,\n ודא שיש לך חיבור לאינטרנט.", Toast.LENGTH_LONG).show();
                            }
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
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(params);
            LinearLayout.LayoutParams lpp = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).getLayoutParams();
            lpp.gravity = Gravity.CENTER;
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.button_selector);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setLayoutParams(params);
            LinearLayout.LayoutParams lpp1 = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).getLayoutParams();
            lpp1.gravity = Gravity.CENTER;
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundResource(R.drawable.button_selector);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setLayoutParams(params);
            LinearLayout.LayoutParams lpp2 = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).getLayoutParams();
            lpp2.gravity = Gravity.CENTER;*/
        });

        findViewById(R.id.refreshButton_owner2).setOnClickListener(v -> {
            imageUrls.clear();
            initImageBitmap();
        });

        initImageBitmap();
    }

    private void initImageBitmap(){
        if(!pd.isShowing())
            pd.show();
        Log.v(TAG, event.getName());
        File photographyAppDir = new File(Utilities.getOrCreateFolder(event.getName()+"-"+event.getId()));
        //List<StorageReference> imgList = ImageHandler.getStorageImage(event.getName()+"-"+event.getId(), this);
        //String[] photographyAppFiles = photographyAppDir.list();
        File[] imgFiles = photographyAppDir.listFiles();
        for(int i = 0; i < imgFiles.length; i++){
            imageUrls.add(imgFiles[i].getPath());
        }
        for(int i = 0; i < imageUrls.size(); i++)
            Log.v(TAG, imageUrls.get(i));
        adapter.notifyDataSetChanged();
        //adapter2.notifyDataSetChanged();
        if(pd.isShowing())
            pd.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = Utilities.getBitmapFromFile(currentFile.getPath());
            images.add(bitmap);
            imageUrls.add(currentFile.getPath());
            adapter.notifyDataSetChanged();
        }else{
            currentFile.delete();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_owner_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            Menus.ownerEventMenu(this,item, user,Utilities.getOrCreateFolder(event.getName()+"-"+event.getId()),event);
        } catch (IOException e) {
            Toast.makeText(this, "לא הייתה אפשרות לטעון את התפריט.", Toast.LENGTH_SHORT);
        }
        return true;
    }

}
