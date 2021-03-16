package com.example.browser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.browser.fragment.audio_frag;
import com.example.browser.model.ModelAudio;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterAudiosAlbumList extends RecyclerView.Adapter<AdapterAudiosAlbumList.MyViewHolder> {


    ArrayList<String> albumName = new ArrayList<>();
    ArrayList<Integer> ItemCount = new ArrayList<>();
    HashMap<String, Integer> data;
    Context context;
    Fragment fragment;

    public AdapterAudiosAlbumList(Context context, Fragment fragment, ArrayList<Integer> counts, ArrayList<String> names) {
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
    public AdapterAudiosAlbumList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_album_layout, parent, false);


        return new AdapterAudiosAlbumList.MyViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.albumNameVideo.setText(albumName.get(position));
        holder.howManyItemInThis.setText("(" + String.valueOf(ItemCount.get(position)) + ")");


        holder.tem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v, position, holder.frameLayoutInsideSingleAlbumLayout,holder.arrow);
            }
        });

    }

    private void click(View v, int position, FrameLayout frameLayoutInsideSingleAlbumLayout,ImageView arrow) {
        if (frameLayoutInsideSingleAlbumLayout.getVisibility() == View.VISIBLE) {
            frameLayoutInsideSingleAlbumLayout.removeAllViews();
            frameLayoutInsideSingleAlbumLayout.setVisibility(View.GONE);
            arrow.setRotation(0f);
        } else if (frameLayoutInsideSingleAlbumLayout.getVisibility() == View.GONE) {
            frameLayoutInsideSingleAlbumLayout.setVisibility(View.VISIBLE);
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
            arrow.setRotation(90f);

            frameLayoutInsideSingleAlbumLayout.addView(recyclerView, new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            ArrayList<ModelAudio> t = new ArrayList<ModelAudio>();
            AdapterAudioList adapterAudioList = new AdapterAudioList(context, t,null);
            ((audio_frag) fragment).getAudioList(position, t);
            recyclerView.setAdapter(adapterAudioList);
        }
    }


    @Override
    public int getItemCount() {
        return albumName.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView howManyItemInThis;
        TextView albumNameVideo;
        ImageView arrow;
        FrameLayout frameLayoutInsideSingleAlbumLayout;
        RelativeLayout tem;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tem = itemView.findViewById(R.id.tem);
            albumNameVideo = itemView.findViewById(R.id.albumNameImages);
            howManyItemInThis = itemView.findViewById(R.id.howManyItemInThis);
            arrow = itemView.findViewById(R.id.imageExpandIconRight);
            frameLayoutInsideSingleAlbumLayout = itemView.findViewById(R.id.frameLayoutInsideSingleAlbumLayout);

        }
    }
}