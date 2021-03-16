package com.example.browser.adapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Constants;
import com.example.browser.R;
import com.example.browser.fragment.audio_frag;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.DownloadModel;
import com.example.browser.model.ModelAudio;

import com.example.browser.threads.downloadService;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.utils.PathUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
public class AdapterAudioList extends RecyclerView.Adapter<com.example.browser.adapter.AdapterAudioList.MyViewHolder>  {
    ArrayList<ModelAudio> audioList = new ArrayList<ModelAudio>();
    Context context;
    Fragment fragment;
    public AdapterAudioList(Context context, ArrayList<ModelAudio> audioList, Fragment fragment) {
        this.context = context;
        this.audioList = audioList;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public com.example.browser.adapter.AdapterAudioList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_audio, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull com.example.browser.adapter.AdapterAudioList.MyViewHolder holder, int position) {
        final ModelAudio item = audioList.get(position);
        holder.tv_title.setText(item.getTitle());
        holder.tv_duration.setText(item.getDuration());
        holder.menu.setOnClickListener(v -> openMenu(v, position));
        holder.itemView.setOnClickListener(v -> startAudio(item));
    }
    private void startAudio(ModelAudio model) {
        ((audio_frag) fragment).startAudio(model);
    }
    private void openMenu(View v, int position) {
        ((audio_frag)fragment).menu(audioList.get(position));
    }
    @Override
    public int getItemCount() {
        return audioList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_duration;
        ImageView menu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_audio);
            tv_duration = itemView.findViewById(R.id.tv_duration_audio);
            menu = itemView.findViewById(R.id.menu_Audioitem);
        }
    }
}
