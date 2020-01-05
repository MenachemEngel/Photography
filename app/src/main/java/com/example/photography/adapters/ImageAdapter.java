package com.example.photography.adapters;

import android.content.Context;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
   private Context mContext;
   private ArrayList<Bitmap> imagesUris;
   // Constructor
   public ImageAdapter(Context c,ArrayList<Bitmap>imagesUris) {
      mContext = c;
      this.imagesUris = imagesUris;
   }
   
   public int getCount() {
      return imagesUris.size();
   }

   public Object getItem(int position) {
      return null;
   }

   public long getItemId(int position) {
      return 0;
   }
   
   // create a new ImageView for each item referenced by the Adapter
   public View getView(int position, View convertView, ViewGroup parent) {
      ImageView imageView;
      
      if (convertView == null) {
         imageView = new ImageView(mContext);
         imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
         imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
         imageView.setPadding(8, 8, 8, 8);
      } 
      else 
      {
         imageView = (ImageView) convertView;
      }
      imageView.setImageBitmap(imagesUris.get(position));
      return imageView;
   }
   

}