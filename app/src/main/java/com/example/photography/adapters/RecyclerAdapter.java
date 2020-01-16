package com.example.photography.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photography.ImageViewerFragment;
import com.example.photography.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    //private ArrayList<String> imageNames = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private Context context;
    private String lastClicked;

    public RecyclerAdapter( Context context/*, ArrayList<String> imageNames*/, ArrayList<String> images) {
        //this.imageNames = imageNames;
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindviewHolder: called");
        Glide.with(context)
                .asBitmap()
                .load(images.get(position))
                .into(holder.image);
        //holder.imageName.setText(imageNames.get(position));
        holder.layout.setOnClickListener(view ->  {
            //Log.d(TAG, "clicked on: " + imageNames.get(position));
            Toast.makeText(context, images.get(holder.getPosition()), Toast.LENGTH_SHORT).show();
            lastClicked = images.get(holder.getPosition());
            //-----------------------------------------------
            FragmentManager manager = ((Activity)context).getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString("img",images.get(position));
            ImageViewerFragment fr = new ImageViewerFragment();
            fr.setArguments(bundle);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fr).addToBackStack(null);
            fragmentTransaction.commit();
            //-----------------------------------------------
        });
    }

    @NonNull
    @Override
    public String toString() {
        return lastClicked;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //TextView imageName;
        ImageView image;
        LinearLayout layout;

        public ViewHolder(View view){
            super(view);
            this.image = view.findViewById(R.id.imageView);
            this.layout = view.findViewById(R.id.parent_item_list);
        }

    }
}
