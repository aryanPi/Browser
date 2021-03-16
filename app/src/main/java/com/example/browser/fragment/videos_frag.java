package com.example.browser.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Constants;
import com.example.browser.R;
import com.example.browser.activities.Downloads;
import com.example.browser.adapter.AdapterImageAlbumList;
import com.example.browser.adapter.AdapterVideoList;
import com.example.browser.adapter.AdapterVideosAlbumList;
import com.example.browser.adapter.BottomSheetItemsAdapter;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.ModelAudio;
import com.example.browser.model.ModelImage;
import com.example.browser.model.ModelVideo;
import com.example.browser.threads.AudioplayerService;
import com.example.browser.utils.PathUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

public class videos_frag extends Fragment {


    View root;
    ArrayList<String> albums = new ArrayList<>();
    ArrayList<Integer> counts = new ArrayList<>();
    RecyclerView DynamicRecyclerViewAlbumVideos;
    RecyclerView DynamicRecyclerViewVideos;

    AdapterVideosAlbumList adapterVideoListAlbum;

    private ArrayList<ModelVideo> videosList = new ArrayList<ModelVideo>();
    public AdapterVideoList adapterVideoList;
    Activity activity;

    public videos_frag(Activity activity) {
        this.activity = activity;
    }

    public void getVideosList(int position, ArrayList<ModelVideo> imagesListLocal) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < videosList.size(); i++) {
                    if (videosList.get(i).getAlbumIndex() == position) {
                        imagesListLocal.add(videosList.get(i));
                    }
                }
            }
        }.start();

    }


    private void initializeViews() {
        videosList.clear();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_videos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapterVideoList = new AdapterVideoList(activity, videosList, this);
        recyclerView.setAdapter(adapterVideoList);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            } else {
                loadVideos();
            }
        } else {
            loadVideos();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadVideos();
            } else {
                Toast.makeText(activity, "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadVideos() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
                String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";

                Cursor cursor = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);
                if (cursor != null) {
                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                    int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                    int albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);


                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idColumn);
                        String title = cursor.getString(titleColumn);
                        int duration = cursor.getInt(durationColumn);

                        String album = cursor.getString(albumIndex);
                        int index = 0;

                        if (!albums.contains(album)) {
                            albums.add(album);
                            counts.add(1);
                        } else {
                            counts.set(albums.indexOf(album), counts.get(albums.indexOf(album)) + 1);
                        }

                        index = albums.indexOf(album);


                        Uri data = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                        String duration_formatted;
                        int sec = (duration / 1000) % 60;
                        int min = (duration / (1000 * 60)) % 60;
                        int hrs = duration / (1000 * 60 * 60);

                        if (hrs == 0) {
                            duration_formatted = String.valueOf(min).concat(":".concat(String.format(Locale.UK, "%02d", sec)));
                        } else {
                            duration_formatted = String.valueOf(hrs).concat(":".concat(String.format(Locale.UK, "%02d", min).concat(":".concat(String.format(Locale.UK, "%02d", sec)))));
                        }

                        videosList.add(new ModelVideo(id, data, title, duration_formatted, index));
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapterVideoListAlbum != null)
                                    adapterVideoListAlbum.notifyDataSetChanged();
                                adapterVideoList.notifyItemInserted(videosList.size() - 1);
                            }
                        });
                    }
                }

            }
        }.start();
    }

    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_videos, container, false);

        frameLayout = null;
        frameLayout = root.findViewById(R.id.subFragVideos_frag);


        if (DynamicRecyclerViewAlbumVideos != null) DynamicRecyclerViewAlbumVideos = null;
        if (!videosList.isEmpty()) videosList.clear();
        if (!counts.isEmpty()) counts.clear();
        if (!albums.isEmpty()) albums.clear();
        if (adapterVideoList != null) adapterVideoList = null;
        if (adapterVideoListAlbum != null) adapterVideoListAlbum = null;


        DynamicRecyclerViewVideos = new RecyclerView(getContext());
        DynamicRecyclerViewVideos.setId(R.id.recyclerView_videos);
        frameLayout.addView(DynamicRecyclerViewVideos, new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


        TextView albumSubBtnVideos = root.findViewById(R.id.albumSubBtnVideos);
        TextView VideosSubBtn = root.findViewById(R.id.VideosSubBtn);

        extraFunctions.change(albumSubBtnVideos, VideosSubBtn, getContext());

        VideosSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraFunctions.change(albumSubBtnVideos, VideosSubBtn, getContext());
                videosSubBtnClick();
            }
        });
        albumSubBtnVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraFunctions.change(VideosSubBtn, albumSubBtnVideos, getContext());
                albumSubBtnClick();
            }
        });

        initializeViews();
        checkPermissions();


        return root;
    }


    private void videosSubBtnClick() {
        if (DynamicRecyclerViewAlbumVideos != null) {
            if (DynamicRecyclerViewAlbumVideos.getVisibility() == View.VISIBLE) {
                DynamicRecyclerViewAlbumVideos.setVisibility(View.GONE);
                DynamicRecyclerViewVideos.setVisibility(View.VISIBLE);
            }
        }
    }

    private void albumSubBtnClick() {
        if (DynamicRecyclerViewAlbumVideos != null) {
            DynamicRecyclerViewAlbumVideos.setVisibility(View.VISIBLE);
            DynamicRecyclerViewVideos.setVisibility(View.GONE);
        } else {
            DynamicRecyclerViewAlbumVideos = new RecyclerView(getContext());
            DynamicRecyclerViewAlbumVideos.setId(R.id.recyclerView_album_videos);
            frameLayout.addView(DynamicRecyclerViewAlbumVideos, new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


            DynamicRecyclerViewAlbumVideos.setVisibility(View.VISIBLE);
            DynamicRecyclerViewVideos.setVisibility(View.GONE);

            RecyclerView recyclerView2 = root.findViewById(R.id.recyclerView_album_videos);
            recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 1)); //3 = column count
            adapterVideoListAlbum = new AdapterVideosAlbumList(activity, this, counts, albums);
            recyclerView2.setAdapter(adapterVideoListAlbum);

        }

    }

    public void click(String toString) {
        String path;
        try {
            path = PathUtil.getPath(getContext(), selectedModelVideo.getData());
        } catch (URISyntaxException e) {
            Toast.makeText(activity, "File path error !", Toast.LENGTH_SHORT).show();
            return;
        }


        switch (toString) {
            case "Share":
                extraFunctions.shareFile(path);
                break;
            case "Delete":
                ((Downloads) getActivity()).deleteItem(path, this, -1L);
                break;
            case "Play In Background":
                ArrayList<ModelAudio> lstLocal = new ArrayList<>();
                lstLocal.add(new ModelAudio(selectedModelVideo.getId(), selectedModelVideo.getData(), selectedModelVideo.getTitle(), selectedModelVideo.getDuration(), 0));
                Intent serviceIntent = new Intent(getActivity(), AudioplayerService.class);
                serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                AudioplayerService.lst = lstLocal;
                AudioplayerService.playIndex = 0;
                getActivity().startService(serviceIntent);
                break;
            case "Location":
                ((Downloads)getActivity()).location(path,getContext());
                break;
            default:
                break;
        }
        bottomSheetDialog.dismiss();
    }

    ModelVideo selectedModelVideo;
    BottomSheetDialog bottomSheetDialog;
    public void menuItem(ModelVideo model) {
        bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        selectedModelVideo = model;
        View sheetView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.bottomsheet_items_custom, (LinearLayout) activity.findViewById(R.id.bottomSheetDownloadFailed));
        ((TextView) sheetView.findViewById(R.id.titleOfFileBottomsheetDownloadFailed)).setText(model.getTitle());
        ArrayList<String> arrayListString = new ArrayList<>();
        arrayListString.add("Share_sh");
        arrayListString.add("Delete_de");
        arrayListString.add("Location_lo");
        arrayListString.add("Play In Background_pag");
        //arrayListString.add("");
        ((ListView) sheetView.findViewById(R.id.list_item_bottomSheet)).setAdapter(new BottomSheetItemsAdapter(getContext(), R.layout.bottomsheet_items_custom, arrayListString, this, 1));
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
}
