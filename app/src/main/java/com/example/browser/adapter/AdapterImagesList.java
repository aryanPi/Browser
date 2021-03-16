package com.example.browser.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.browser.activities.ImageSliderGallery;
import com.example.browser.R;
import com.example.browser.fragment.photos_frag;
import com.example.browser.model.ModelImage;

import java.util.ArrayList;

public class AdapterImagesList extends RecyclerView.Adapter<AdapterImagesList.MyViewHolder> {

    ArrayList<ModelImage> ImageList = new ArrayList<ModelImage>();
    Context context;
Fragment fragment;
    public AdapterImagesList(Context context, ArrayList<ModelImage> Images, Fragment fragment) {
        this.context = context;
        this.ImageList = Images;
        this.fragment = fragment;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @NonNull
    @Override
    public AdapterImagesList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelImage item = ImageList.get(position);

        Glide.with(context).load(Uri.parse(item.getData())).fitCenter().centerInside().into(holder.imgView_thumbnail);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((photos_frag)fragment).menuitem(item);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSliderGallery.arrayList = ImageList;
                Intent intent = new Intent(v.getContext(), ImageSliderGallery.class);
                intent.putExtra("itemId", item.getData());
                v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return ImageList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView_thumbnail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView_thumbnail = itemView.findViewById(R.id.singleImage_Image);
        }
    }
}

