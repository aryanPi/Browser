/*
package com.example.browser.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.browser.R;
import com.example.browser.activities.WhatsAppActivity;
import com.example.browser.adapter.PhotosGalleryAdapter;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.activities.videoPlayer;

import java.util.ArrayList;
import java.util.HashSet;

public class videos_frag extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.videos_frag_layout, container, false);

        ArrayList<String> allVideos = getAllMediaVideos();
        GridView videosGrid = (GridView) root.findViewById(R.id.videosGrid);
        PhotosGalleryAdapter customAdapter = new PhotosGalleryAdapter(getContext(), allVideos, this);
        videosGrid.setAdapter(customAdapter);


        return root;
    }







    public void openThis(String path) {
        GlobalVariables.setVideo(path);
        Intent intent = new Intent(getActivity(), videoPlayer.class);
        startActivity(intent);
    }
}*/

package com.example.browser.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.browser.activities.Downloads;
import com.example.browser.R;
import com.example.browser.adapter.AdapterImageAlbumList;
import com.example.browser.adapter.AdapterImagesList;
import com.example.browser.adapter.BottomSheetItemsAdapter;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.ModelImage;
import com.example.browser.utils.PathUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class photos_frag extends Fragment {

    View root;

    private static ArrayList<ModelImage> imagesList = new ArrayList<ModelImage>();
    public AdapterImagesList adapterImagesList;
    ArrayList<String> albums = new ArrayList<>();
    ArrayList<Integer> counts = new ArrayList<>();

    FrameLayout frameLayout;
    RecyclerView DynamicRecyclerViewPhotos;
    RecyclerView DynamicRecyclerViewAlbum;
    AdapterImageAlbumList adapterImageAlbumList;
    Activity activity;

    public photos_frag(Activity activity) {
        this.activity = activity;
    }


    ModelImage selectedModelImage;

    public void menuitem(ModelImage model) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        selectedModelImage = model;
        View sheetView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.bottomsheet_items_custom, (LinearLayout) activity.findViewById(R.id.bottomSheetDownloadFailed));
        try {
            ((TextView) sheetView.findViewById(R.id.titleOfFileBottomsheetDownloadFailed)).setText(PathUtil.getPath(getContext(), Uri.parse(selectedModelImage.getData())).substring(PathUtil.getPath(getContext(), Uri.parse(selectedModelImage.getData())).lastIndexOf("/")+1));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Share_sh");
        arrayList.add("Delete_de");
        arrayList.add("Rename_re");
        ((ListView) sheetView.findViewById(R.id.list_item_bottomSheet)).setAdapter(new BottomSheetItemsAdapter(getContext(), R.layout.bottomsheet_items_custom, arrayList, this, 2));
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    public void click(String toString) {
        String path;
        try {
            path = PathUtil.getPath(getContext(), Uri.parse(selectedModelImage.getData()));
        } catch (URISyntaxException e) {
            Toast.makeText(activity, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (toString) {
            case "Delete":
                ((Downloads) getActivity()).deleteItem(path, this, -1L);
                break;
            case "Rename":
                ((Downloads)getActivity()).rename(this,path.substring(0,path.lastIndexOf("/")-1),path.substring(path.lastIndexOf("/")+1),-1L);
                break;
            case "Share":
                extraFunctions.shareFile(path);
                break;
            default:
                break;
        }
    }

    public void getImagesList(int position, ArrayList<ModelImage> imagesListLocal) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < imagesList.size(); i++) {
                    if (imagesList.get(i).getIndexOfAlbum() == position) {
                        imagesListLocal.add(imagesList.get(i));
                    }
                }
            }
        }.start();

    }


    private void initializeViews() {
        imagesList.clear();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_photots);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapterImagesList = new AdapterImagesList(activity, imagesList, this);
        recyclerView.setAdapter(adapterImagesList);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            } else {
                loadPhotots();
            }
        } else {
            loadPhotots();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadPhotots();
            } else {
                Toast.makeText(activity, "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadPhotots() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
                String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";


                Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);
                if (cursor != null) {

                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

                    int albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                    while (cursor.moveToNext()) {

                        long id = cursor.getLong(idColumn);

                        String album = cursor.getString(albumIndex);
                        int index = 0;

                        if (!albums.contains(album)) {
                            albums.add(album);
                            counts.add(1);
                        } else {
                            counts.set(albums.indexOf(album), counts.get(albums.indexOf(album)) + 1);
                        }

                        index = albums.indexOf(album);

                        Uri data = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                        imagesList.add(new ModelImage(data.toString(), index));
                        activity.runOnUiThread(() -> {
                            if (adapterImageAlbumList != null) {
                                adapterImageAlbumList.notifyDataSetChanged();
                            }
                            adapterImagesList.notifyItemInserted(imagesList.size());
                        });
                    }

                }

            }
        }.start();
    }

    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_media_frag, container, false);

        final Downloads.ThreadedRequest tReq = new Downloads.ThreadedRequest();

//        StartLoading(root.findViewById(R.id.bodyPhotos), root.findViewById(R.id.loadingPhotos));
        tReq.start(new Runnable() {
            public void run() {

                frameLayout = null;
                frameLayout = root.findViewById(R.id.subFragPhotos_frag);
                mHandler = new Handler();


                if (DynamicRecyclerViewAlbum != null) DynamicRecyclerViewAlbum = null;
                if (!imagesList.isEmpty()) imagesList.clear();
                if (!counts.isEmpty()) counts.clear();
                if (!albums.isEmpty()) albums.clear();
                if (adapterImagesList != null) adapterImagesList = null;
                if (adapterImageAlbumList != null) adapterImageAlbumList = null;


                DynamicRecyclerViewPhotos = new RecyclerView(getContext());

                DynamicRecyclerViewPhotos.setId(R.id.recyclerView_photots);

                frameLayout.addView(DynamicRecyclerViewPhotos, new LinearLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

                TextView albumSubBtn = root.findViewById(R.id.albumSubBtn);
                TextView photosSubBtn = root.findViewById(R.id.photosSubBtn);

                extraFunctions.change(albumSubBtn, photosSubBtn, getContext());


                initializeViews();
                checkPermissions();

                photosSubBtn.setOnClickListener(v -> {
                    extraFunctions.change(albumSubBtn, photosSubBtn, getContext());
                    photosSubBtnClick();
                });
                albumSubBtn.setOnClickListener(v -> {
                    extraFunctions.change(photosSubBtn, albumSubBtn, getContext());
                    albumSubBtnClick();
                });


                //     StopLoading(root.findViewById(R.id.bodyPhotos), root.findViewById(R.id.loadingPhotos));
            }
        });


//This method would start the animation/notification that a request is happening


        return root;

    }

    private void StopLoading(View v, View v2) {
        v.setVisibility(View.VISIBLE);
        v2.setVisibility(View.GONE);
    }

    private void StartLoading(View v, View v2) {
        v.setVisibility(View.GONE);
        v2.setVisibility(View.VISIBLE);
    }

    private void albumSubBtnClick() {
        if (DynamicRecyclerViewAlbum != null) {
            DynamicRecyclerViewAlbum.setVisibility(View.VISIBLE);
            DynamicRecyclerViewPhotos.setVisibility(View.GONE);
        } else {
            DynamicRecyclerViewAlbum = new RecyclerView(getContext());
            DynamicRecyclerViewAlbum.setId(R.id.recyclerView_album_photots);
            DynamicRecyclerViewAlbum.setVisibility(View.VISIBLE);
            DynamicRecyclerViewPhotos.setVisibility(View.GONE);

            frameLayout.addView(DynamicRecyclerViewAlbum, new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

            RecyclerView recyclerView2 = root.findViewById(R.id.recyclerView_album_photots);
            recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 1)); //3 = column count
            adapterImageAlbumList = new AdapterImageAlbumList(activity, this, counts, albums);
            recyclerView2.setAdapter(adapterImageAlbumList);

        }

    }

    private void photosSubBtnClick() {
        if (DynamicRecyclerViewAlbum != null) {
            if (DynamicRecyclerViewAlbum.getVisibility() == View.VISIBLE) {
                DynamicRecyclerViewAlbum.setVisibility(View.GONE);
                DynamicRecyclerViewPhotos.setVisibility(View.VISIBLE);
            }
        }
    }


}
