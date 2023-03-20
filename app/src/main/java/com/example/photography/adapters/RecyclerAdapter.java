package com.example.photography.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photography.activities.OwnerActivity2;
import com.example.photography.fragments.ImageViewerFragment;
import com.example.photography.R;
import com.example.photography.utils.FontAwesome;
import com.example.photography.utils.Utilities;

import java.io.File;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    //private ArrayList<String> imageNames = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private Context context;
    private String lastClicked;
    private ImageViewerFragment fr;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager manager;
    private Bundle bundle;

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    public RecyclerAdapter( Context context/*, ArrayList<String> imageNames*/, ArrayList<String> images) {
        //this.imageNames = imageNames;
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Typeface iconFont = FontAwesome.getTypeface(context, FontAwesome.FONTAWESOME);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.icon.setTypeface(iconFont);
        holder.icon.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                builder.setTitle(R.string.delete)
                        .setMessage("האם ברצונך למחוק את התמונה?")
                        .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new File(images.get(holder.getPosition())).delete();
                                images.remove(holder.getPosition());
                                Toast.makeText(context, "התמונה נמחקה", Toast.LENGTH_LONG).show();
                                notifyDataSetChanged();
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
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(params);
                LinearLayout.LayoutParams lpp = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).getLayoutParams();
                lpp.gravity = Gravity.CENTER;
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.button_selector);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setLayoutParams(params);
                LinearLayout.LayoutParams lpp1 = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).getLayoutParams();
                lpp1.gravity = Gravity.CENTER;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindviewHolder: called");
        Glide.with(context)
                .asBitmap()
                .load(images.get(position))
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.image);
        //holder.imageName.setText(imageNames.get(position));
        holder.layout.setOnClickListener(view ->  {
            lastClicked = images.get(holder.getPosition());
            //-----------------------------------------------
            manager = ((Activity)context).getFragmentManager();
            bundle = new Bundle();
            bundle.putString("img",images.get(position));
            fr = new ImageViewerFragment();
            fr.setArguments(bundle);
            fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fr).addToBackStack(null);
            fragmentTransaction.commit();
            //-----------------------------------------------
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public String toString() {
        manager.beginTransaction().remove(fr).commit();
        return lastClicked;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //TextView imageName;
        ImageView image;
        TextView icon;
        RelativeLayout layout;

        public ViewHolder(View view){
            super(view);
            this.image = view.findViewById(R.id.imageView);
            this.icon = view.findViewById(R.id.textViewIcon);
            this.layout = view.findViewById(R.id.parent_item_list);
        }

    }
}
