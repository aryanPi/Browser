package com.example.browser.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Constants;
import com.example.browser.activities.Downloads;
import com.example.browser.adapter.BottomSheetItemsAdapter;
import com.example.browser.threads.AudioplayerService;
import com.example.browser.R;
import com.example.browser.adapter.AdapterAudioList;
import com.example.browser.adapter.AdapterAudiosAlbumList;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.ModelAudio;
import com.example.browser.utils.PathUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class audio_frag extends Fragment {

    View root;
    FrameLayout frameLayout;
    RecyclerView DynamicRecyclerViewAudio;

    ArrayList<String> albums = new ArrayList<>();
    ArrayList<Integer> counts = new ArrayList<>();

    AdapterAudioList adapterAudioList;
    ArrayList<ModelAudio> audioList = new ArrayList<>();

    RecyclerView DynamicRecyclerViewAlbumAudios;

    Activity activity;

    public static Fragment THIS;

    public audio_frag(Activity activity) {
        this.activity = activity;
    }

    public void startAudio(ModelAudio modelAudio) {
        Intent serviceIntent = new Intent(getActivity(), AudioplayerService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        AudioplayerService.lst = new ArrayList<>();
        AudioplayerService.lst.add(modelAudio);
        AudioplayerService.playIndex = 0;
        getActivity().startService(serviceIntent);

    }

    public void addToTrackAudio(ModelAudio modelAudio) {
        if (AudioplayerService.lst == null)
            startAudio(modelAudio);
        else
            AudioplayerService.lst.add(modelAudio);
    }

    public int totalDur, currentDur;
    public String titleAudio;
    public int playingState;
    public boolean isActive = false;
    public String seekTo;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_audio_frag, container, false);


        THIS = this;

        isActive = true;
        updateControlsView();
        frameLayout = null;
        frameLayout = root.findViewById(R.id.subFragAudio_frag);


        if (DynamicRecyclerViewAlbumAudios != null) DynamicRecyclerViewAlbumAudios = null;
        if (!audioList.isEmpty()) audioList.clear();
        if (!counts.isEmpty()) counts.clear();
        if (!albums.isEmpty()) albums.clear();
        if (adapterAudioList != null) adapterAudioList = null;
        if (adapter != null) adapter = null;


        DynamicRecyclerViewAudio = new RecyclerView(activity);
        DynamicRecyclerViewAudio.setId(R.id.recyclerView_audio);
        frameLayout.addView(DynamicRecyclerViewAudio, new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


        TextView albumSubBtnAudio = root.findViewById(R.id.albumSubBtnAudio);
        TextView audioSubBtn = root.findViewById(R.id.AudioSubBtn);

        extraFunctions.change(albumSubBtnAudio, audioSubBtn, activity);


        audioSubBtn.setOnClickListener(v -> {
            extraFunctions.change(albumSubBtnAudio, audioSubBtn, activity);
            audioSubBtnClick();
        });
        albumSubBtnAudio.setOnClickListener(v -> {
            extraFunctions.change(audioSubBtn, albumSubBtnAudio, activity);
            albumSubBtnClick();
        });


        initializeViews();

        checkPermissions();


        return root;
    }


    public void getAudioList(int position, ArrayList<ModelAudio> imagesListLocal) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < audioList.size(); i++) {
                    if (audioList.get(i).getAlbumIndex() == position) {
                        imagesListLocal.add(audioList.get(i));
                    }
                }
            }
        }.start();

    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            } else {
                loadAudios();
            }
        } else {
            loadAudios();
        }
    }

    private void loadAudios() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String[] projection;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    projection = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION};
                } else {
                    projection = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME};
                }


                String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";

                @SuppressLint("Recycle") Cursor cursor = activity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);
                if (cursor != null) {
                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    int durationColumn = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                    }

                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idColumn);
                        String title = cursor.getString(titleColumn);
                        int duration = 0;
                        if (durationColumn != 0) {
                            duration = cursor.getInt(durationColumn);
                        }

                        int index = 0;




    /*                    if (!albums.contains(album)) {
                            albums.add(album);
                            counts.add(1);
                        } else {
                            counts.set(albums.indexOf(album), counts.get(albums.indexOf(album)) + 1);
                        }
*/
                        //                  index = albums.indexOf(album);


                        Uri data = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                        String album;
                        try {
                            String path = PathUtil.getPath(activity, data);
                            album = path.substring(path.lastIndexOf("/", path.lastIndexOf("/") - 2) + 1, path.lastIndexOf("/"));
                            if (!albums.contains(album)) {
                                albums.add(album);
                                counts.add(1);
                            } else {
                                counts.set(albums.indexOf(album), counts.get(albums.indexOf(album)) + 1);
                            }
                            index = albums.indexOf(album);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        String duration_formatted = extraFunctions.getDuration(duration);

                        audioList.add(new ModelAudio(3773, data, title, duration_formatted, index));
                        activity.runOnUiThread(() -> {
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            adapterAudioList.notifyItemInserted(audioList.size());
                        });
                    }
                }

            }
        }.start();
    }

    AdapterAudiosAlbumList adapter;

    private void initializeViews() {
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_audio);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));
        adapterAudioList = new AdapterAudioList(activity, audioList, this);
        recyclerView.setAdapter(adapterAudioList);
    }

    private void albumSubBtnClick() {
        if (DynamicRecyclerViewAlbumAudios != null) {
            DynamicRecyclerViewAlbumAudios.setVisibility(View.VISIBLE);
            DynamicRecyclerViewAudio.setVisibility(View.GONE);
        } else {

            DynamicRecyclerViewAlbumAudios = new RecyclerView(activity);
            DynamicRecyclerViewAlbumAudios.setId(R.id.recyclerView_album_audio);

            frameLayout.addView(DynamicRecyclerViewAlbumAudios, new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


            DynamicRecyclerViewAlbumAudios.setVisibility(View.VISIBLE);
            DynamicRecyclerViewAudio.setVisibility(View.GONE);


            RecyclerView recyclerView2 = root.findViewById(R.id.recyclerView_album_audio);
            recyclerView2.setLayoutManager(new GridLayoutManager(activity, 1)); //3 = column count
            adapter = new AdapterAudiosAlbumList(activity, this, counts, albums);
            recyclerView2.setAdapter(adapter);
        }
    }


    private void audioSubBtnClick() {
        if (DynamicRecyclerViewAlbumAudios != null) {
            if (DynamicRecyclerViewAlbumAudios.getVisibility() == View.VISIBLE) {
                DynamicRecyclerViewAlbumAudios.setVisibility(View.GONE);
                DynamicRecyclerViewAudio.setVisibility(View.VISIBLE);
            }
        }

    }


    @SuppressLint("SetTextI18n")
    public void updateControlsView() {
        if (playingState == -1) {
            root.findViewById(R.id.audioControlsLayout).setVisibility(View.GONE);
            return;
        }
        if (AudioplayerService.isPlayingAudioService) {
            root.findViewById(R.id.audioControlsLayout).setVisibility(View.VISIBLE);
            TextView title = root.findViewById(R.id.audioTitleMainAudioFrag);
            ImageView playPause = root.findViewById(R.id.playPause);
            TextView currentDuration = root.findViewById(R.id.currentDurationAudioFrag);
            TextView totalDuration = root.findViewById(R.id.totalDurationAudioFrag);
            SeekBar seekBar = root.findViewById(R.id.seekBarAudioPlaying);

            Intent getInfoIntent = new Intent(getActivity(), AudioplayerService.class);
            getInfoIntent.setAction(Constants.ACTION.GET_INFO);
            PendingIntent getInfoPendingIntent = PendingIntent.getService(getContext(), 0,
                    getInfoIntent, 0);
            try {
                getInfoPendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            playPause.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), AudioplayerService.class);
                intent.setAction(Constants.ACTION.PLAY_ACTION);
                PendingIntent seekPendingIntent = PendingIntent.getService(getContext(), 0,
                        intent, 0);
                try {
                    seekPendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Intent intent = new Intent(getActivity(), AudioplayerService.class);
                    intent.setAction(Constants.ACTION.SEEK_ACTION);
                    PendingIntent seekPendingIntent = PendingIntent.getService(getContext(), 0,
                            intent, 0);
                    seekTo = seekBar.getProgress() + "";
                    try {
                        seekPendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            });

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(() -> {
                    title.setText(titleAudio);
                    totalDuration.setText("/" + extraFunctions.getDuration(totalDur) + "");
                    currentDuration.setText(extraFunctions.getDuration(currentDur) + "");
                    seekBar.setMax(totalDur);
                });
                while (playingState != -1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (((audio_frag) audio_frag.THIS).isActive) {
                        if (playingState == 1) {
                            getActivity().runOnUiThread(() -> {
                                        seekBar.setProgress(currentDur);
                                        currentDuration.setText(extraFunctions.getDuration(currentDur));
                                        playPause.setImageResource(R.drawable.ic_pause);
                                    }
                            );

                        } else if (playingState == 0) {
                            getActivity().runOnUiThread(() -> playPause.setImageResource(R.drawable.ic_play_final));
                        }
                    } else {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(() -> root.findViewById(R.id.audioControlsLayout).setVisibility(View.GONE));
                        break;
                    }

                }
            }).start();
        } else {
            root.findViewById(R.id.audioControlsLayout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        isActive = false;
        onDestroy();
        super.onDestroyView();
    }

    public void click(String toString) {
        String path = "";
        try {
            path = PathUtil.getPath(getContext(), selectedAudioModel.getData());
        } catch (URISyntaxException e) {
            Toast.makeText(getContext(), "Unable to take any Action !", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (toString) {
            case "Delete":
                    ((Downloads)getActivity()).deleteItem(path,this,-2L);
                break;
            case "Share":
                    extraFunctions.shareFile(path);
                break;
            case "Append To Track List":
                addToTrackAudio(selectedAudioModel);
                break;
            case "Location":
                ((Downloads)getActivity()).location(path,getContext());
            default:
                break;
        }
        bottomSheetDialog.dismiss();
    }

    ModelAudio selectedAudioModel;
    BottomSheetDialog bottomSheetDialog;
    public void menu(ModelAudio modelAudio) {
         bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        selectedAudioModel = modelAudio;
        View sheetView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.bottomsheet_items_custom, (LinearLayout) activity.findViewById(R.id.bottomSheetDownloadFailed));
        ((TextView) sheetView.findViewById(R.id.titleOfFileBottomsheetDownloadFailed)).setText(selectedAudioModel.getTitle());
        ArrayList<String> arrayListString = new ArrayList<>();
        arrayListString.add("Delete_de");
        arrayListString.add("Share_sh");
        arrayListString.add("Location_lo");
        arrayListString.add("Append To Track List_at");
        ((ListView) sheetView.findViewById(R.id.list_item_bottomSheet)).setAdapter(new BottomSheetItemsAdapter(getContext(), R.layout.bottomsheet_items_custom, arrayListString, this, 3));
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }


    public void removed() {
        for(int i=0;i<audioList.size();i++){
            if (audioList.get(i).getId()==selectedAudioModel.getId()){
                audioList.remove(i);
                adapterAudioList.notifyItemRemoved(i);
                break;
            }
        }
    }
}