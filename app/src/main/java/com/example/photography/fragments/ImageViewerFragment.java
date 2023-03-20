package com.example.photography.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;
import com.example.photography.R;

import java.io.File;
import java.util.zip.Inflater;

public class ImageViewerFragment extends Fragment {
    private ImageView imageView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.image_view);
        Bundle bundle = getArguments();
        if(bundle!=null&&bundle.getString("img")!=null){
            Glide.with(getActivity())
                    .asBitmap()
                    .load(bundle.getString("img"))
                    .into(imageView);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery,container,false);
        return view;
    }
}
