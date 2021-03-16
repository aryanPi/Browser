package com.example.browser.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.browser.activities.MainActivity;
import com.example.browser.R;
import com.example.browser.model.NewsModel;
import com.example.browser.utils.GlobalVariables;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<com.example.browser.adapter.NewsAdapter.MyViewHolder>{

    ArrayList<NewsModel> newsModel = new ArrayList<NewsModel>();
    Context context;

    public NewsAdapter(Context context, ArrayList<NewsModel> newsModel){
        this.context = context;
        this.newsModel = newsModel;
    }

    @NonNull
    @Override
    public com.example.browser.adapter.NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
        return new com.example.browser.adapter.NewsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.browser.adapter.NewsAdapter.MyViewHolder holder, int position) {
        final NewsModel item = newsModel.get(position);

        Glide.with(context).load(item.getUrlToImage()).into(holder.image);
        holder.tv_author.setText(item.getAuthor());
        holder.tv_title.setText(item.getTitle());

        holder.bodyNewsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) GlobalVariables.getmActivity()).searQuery(item.getUrl());
            }
        });
        Log.d("aryantomar", "onBindViewHolder: ");
    }

    private void startAudio(Long id) {

    }


    @Override
    public int getItemCount() {
        return newsModel.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_author;
        ImageView image;
        LinearLayout bodyNewsItem;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.titleNews);
            tv_author = itemView.findViewById(R.id.author_news);
            image = itemView.findViewById(R.id.newsImage);
            bodyNewsItem = itemView.findViewById(R.id.bodyNewsItem);
        }
    }
}
