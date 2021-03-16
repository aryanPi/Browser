package com.example.browser.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.browser.activities.Activity_Player;
import com.example.browser.R;
import com.example.browser.fragment.videos_frag;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.ModelVideo;
import com.example.browser.utils.PathUtil;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class AdapterVideoList extends RecyclerView.Adapter<com.example.browser.adapter.AdapterVideoList.MyViewHolder>{

    ArrayList<ModelVideo> videosList = new ArrayList<ModelVideo>();
    Context context;
Fragment fragment;
    public AdapterVideoList(Context context, ArrayList<ModelVideo> videosList,Fragment fragment){
        this.context = context;
        this.videosList = videosList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public com.example.browser.adapter.AdapterVideoList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.browser.adapter.AdapterVideoList.MyViewHolder holder, int position) {
        final ModelVideo item = videosList.get(position);
        holder.tv_title.setText(item.getTitle());
        holder.tv_duration.setText(item.getDuration());
        Glide.with(context).load(item.getData()).into(holder.imgView_thumbnail);

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((videos_frag)fragment).menuItem(item);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Player.modelVideo = item;

                Intent intent = new Intent(v.getContext(), Activity_Player.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView_thumbnail;
        TextView tv_title, tv_duration;
        ImageView menu;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            imgView_thumbnail = itemView.findViewById(R.id.imageView_thumbnail);
            menu = itemView.findViewById(R.id.menu_Videoitem);
        }
    }
}
