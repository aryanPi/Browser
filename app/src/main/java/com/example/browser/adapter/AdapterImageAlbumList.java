package com.example.browser.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.browser.R;
import com.example.browser.fragment.photos_frag;
import com.example.browser.model.ModelImage;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterImageAlbumList extends RecyclerView.Adapter<AdapterImageAlbumList.MyViewHolder> {


    ArrayList<String> albumName = new ArrayList<>();
    ArrayList<Integer> ItemCount = new ArrayList<>();
    HashMap<String, Integer> data;
    Context context;
    Fragment fragment;

    public AdapterImageAlbumList(Context context, Fragment fragment, ArrayList<Integer> counts, ArrayList<String> names) {
        setHasStableIds(true);
        this.context = context;
        this.albumName = names;
        this.ItemCount = counts;
        this.fragment = fragment;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public AdapterImageAlbumList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_album_layout, parent, false);
        Log.d("TAG", "AdapterImageAlbumList: 2");

        return new AdapterImageAlbumList.MyViewHolder(itemView);
    }

    private static final String TAG = "aryantomar";

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.albumNameImages.setText(albumName.get(position));
        holder.howManyItemInThis.setText("(" + String.valueOf(ItemCount.get(position)) + ")");


        holder.tem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v, position, holder.frameLayoutInsideSingleAlbumLayout, holder.arrow);
            }
        });
    }

    private void click(View v, int position, FrameLayout frameLayoutInsideSingleAlbumLayout, ImageView arrow) {
        if (frameLayoutInsideSingleAlbumLayout.getVisibility() == View.VISIBLE) {
            frameLayoutInsideSingleAlbumLayout.removeAllViews();
            arrow.setRotation(0f);
            frameLayoutInsideSingleAlbumLayout.setVisibility(View.GONE);
        } else if (frameLayoutInsideSingleAlbumLayout.getVisibility() == View.GONE) {
            frameLayoutInsideSingleAlbumLayout.setVisibility(View.VISIBLE);
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            arrow.setRotation(90f);

            frameLayoutInsideSingleAlbumLayout.addView(recyclerView, new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            AdapterImagesList adapterImagesList;
            ArrayList<ModelImage> t = new ArrayList<ModelImage>();
            adapterImagesList = new AdapterImagesList(context, t,fragment);
            ((photos_frag) fragment).getImagesList(position, t);
            recyclerView.setAdapter(adapterImagesList);
        }
    }


    @Override
    public int getItemCount() {
        return albumName.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView howManyItemInThis;
        TextView albumNameImages;
        ImageView arrow;
        FrameLayout frameLayoutInsideSingleAlbumLayout;

        RelativeLayout tem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tem = itemView.findViewById(R.id.tem);
            albumNameImages = itemView.findViewById(R.id.albumNameImages);
            howManyItemInThis = itemView.findViewById(R.id.howManyItemInThis);
            arrow = itemView.findViewById(R.id.imageExpandIconRight);
            frameLayoutInsideSingleAlbumLayout = itemView.findViewById(R.id.frameLayoutInsideSingleAlbumLayout);
        }
    }
}