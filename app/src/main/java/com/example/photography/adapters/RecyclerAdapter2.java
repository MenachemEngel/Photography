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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.photography.R;
import com.example.photography.fragments.ImageViewerFragment;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    //private ArrayList<String> imageNames = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private Context context;
    private String lastClicked;
    private ImageViewerFragment fr;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager manager;
    private Bundle bundle;

    public RecyclerAdapter2(Context context/*, ArrayList<String> imageNames*/, ArrayList<String> images) {
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
                .load(images.get(position))
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
        LinearLayout layout;

        public ViewHolder(View view){
            super(view);
            this.image = view.findViewById(R.id.imageView);
            this.layout = view.findViewById(R.id.parent_item_list);
        }

    }
}
