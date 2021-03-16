package com.example.browser.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.Constants;
import com.example.browser.R;
import com.example.browser.model.ModelAudio;
import com.example.browser.model.ModelVideo;
import com.example.browser.threads.AudioplayerService;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class Activity_Player extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    long videoId;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    boolean lockScreen = false;

    private String nameOfVideo = "";

    public static ModelVideo modelVideo;

    LottieAnimationView backwardSeekAnimation;
    LottieAnimationView forwardSeekAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);


        initializeViews();
        videoId = modelVideo.getId();
        nameOfVideo = modelVideo.getTitle();
        initListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {

        ImageView back = findViewById(R.id.backwardImageClick);
        ImageView forward = findViewById(R.id.forwardImageClick);
        SeekBar brightnessSeekbar = findViewById(R.id.seekBarBrightness);
        SeekBar volumeSeekbar = findViewById(R.id.volumeSeekbar);

        allViewsControlsNotLockedbtn.add(playerView.findViewById(R.id.tempControls));
        allViewsControlsNotLockedbtn.add(playerView.findViewById(R.id.exo_position));
        allViewsControlsNotLockedbtn.add(playerView.findViewById(R.id.exo_progress));
        allViewsControlsNotLockedbtn.add(playerView.findViewById(R.id.exo_duration));
        allViewsControlsNotLockedbtn.add(playerView.findViewById(R.id.menuVideoPlayer));
        allViewsControlsNotLockedbtn.add(playerView.findViewById(R.id.muteBtn));
        allViewsControlsNotLockedbtn.add(playerView.findViewById(R.id.topLayoutNameAndBack));


        playerView.findViewById(R.id.muteBtn).setOnClickListener(v -> mute((ImageView) v));

        backwardSeekAnimation = findViewById(R.id.backSeekAnimation);
        forwardSeekAnimation = findViewById(R.id.forwardSeekAnimation);

        playerView.findViewById(R.id.menuVideoPlayer).setOnClickListener(v -> {
            playerView.setControllerHideOnTouch(false);
            menu(v);
        });

        ((TextView) playerView.findViewById(R.id.videoName)).setText(nameOfVideo);
        playerView.findViewById(R.id.backVideoPlayer).setOnClickListener(v -> back(v));

        playerView.findViewById(R.id.lockControlsScreenbtnVideoPlayer).setOnClickListener(v -> lockControls((ImageView) v));


        findViewById(R.id.rotateScreenbtnVideoPlayer).setOnClickListener(v -> rotateScreen());

        LinearLayout brighnessayout = findViewById(R.id.brightnessLayout);
        LinearLayout volumelayout = findViewById(R.id.soundTextViewLayout);
        TextView soundTextView = findViewById(R.id.soundTextView);
        TextView brightnessTextView = findViewById(R.id.brightnessTextView);

        back.setOnTouchListener(new OnSwipeTouchListener(this, back, playerView, this, forward, 2, brightnessSeekbar, backwardSeekAnimation, brighnessayout, brightnessTextView));

        forward.setOnTouchListener(new OnSwipeTouchListener(this, forward, playerView, this, back, 1, volumeSeekbar, forwardSeekAnimation, volumelayout, soundTextView));


    }

    private boolean isLockedControls = false;
    ArrayList<View> allViewsControlsNotLockedbtn = new ArrayList<>();

    private void lockControls(ImageView v) {
        if (isLockedControls) {
            isLockedControls = false;
            Glide.with(getApplicationContext()).load(R.drawable.ic_lock_inactive).into(v);
            for (View view : allViewsControlsNotLockedbtn) {
                view.setVisibility(View.VISIBLE);
            }

        } else {
            isLockedControls = true;
            Glide.with(getApplicationContext()).load(R.drawable.ic_lock_active).into(v);
            for (View view : allViewsControlsNotLockedbtn) {
                view.setVisibility(View.GONE);
            }
        }
    }

    private void back(View v) {
        player.release();
        finish();
    }

    boolean isMuted = false;

    private void mute(ImageView v) {
        if (isMuted) {
            player.setVolume(1f);
            Glide.with(getApplicationContext()).load(R.drawable.ic_unmuted).into(v);
            isMuted = false;
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.ic_muted).into(v);
            player.setVolume(0f);
            isMuted = true;
        }
    }

    private void menu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.video_player_menu);
        popupMenu.show();
    }

    private void lockscreen() {
        if (lockScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            lockScreen = false;
            Toast.makeText(this, "Unlocked Screen Rotation", Toast.LENGTH_SHORT).show();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            lockScreen = true;
            Toast.makeText(this, "Locked Screen Rotation", Toast.LENGTH_SHORT).show();
        }
    }


    private void rotateScreen() {
        setRequestedOrientation(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void initializeViews() {
        playerView = findViewById(R.id.playerView);

    }

    private void initializePlayer(Uri fileUri, boolean state, String path) {
        Uri videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);

        MediaSource mediaSource = buildMediaSource(videoUri);

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);


        if (state) {

            player = ExoPlayerFactory.newSimpleInstance(this,
                    new DefaultTrackSelector());

            playerView.setPlayer(player);

            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, getPackageName()));


            MediaSource contentMediaSource = buildMediaSource(videoUri);
            MediaSource[] mediaSources = new MediaSource[2]; //The Size must change depending on the Uris
            mediaSources[0] = contentMediaSource; // uri

            //Add subtitles
            SingleSampleMediaSource subtitleSource = new SingleSampleMediaSource(Uri.parse(path), dataSourceFactory,
                    Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, "en", null),
                    C.TIME_UNSET);

            mediaSources[1] = subtitleSource;

            mediaSource = new MergingMediaSource(mediaSources);


            playerView.getSubtitleView().setVisibility(View.VISIBLE);
            playerView.setControllerHideOnTouch(true);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);

        } else {

            player.prepare(mediaSource);
            player.setPlayWhenReady(true);

        }

//        Uri srtUri = Uri.parse("http://www.storiesinflight.com/js_videosub/jellies.srt");


    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, getString(R.string.app_name));
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer(null, false, "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer(null, false, "");
        }
    }

    @Override
    protected void onPause() {
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
        super.onStop();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.playAsBackgroundMenuPlayer:
                playAsBackground();
                playerView.setControllerHideOnTouch(true);
                break;
            case R.id.lockRotationVideoPlayer:
                lockscreen();
                playerView.setControllerHideOnTouch(true);
                break;
            case R.id.shareVideoPlayerMenu:
                share();
                playerView.setControllerHideOnTouch(true);
                break;
            default:
                playerView.setControllerHideOnTouch(true);
                break;
        }
        return true;
    }

    private void playAsBackground() {
        ArrayList<ModelAudio> lstLocal = new ArrayList<>();
        lstLocal.add(new ModelAudio(videoId, modelVideo.getData(), modelVideo.getTitle(), modelVideo.getDuration(), 0));
        Intent serviceIntent = new Intent(this, AudioplayerService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        AudioplayerService.lst = lstLocal;
        AudioplayerService.playIndex = 0;
        this.startService(serviceIntent);
    }

    private void share() {

    }



    public static class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;
        Context context;
        PlayerView playerView;
        View v;
        View v2;
        Activity activity;
        int backorFor;
        SeekBar seekBar;
        LottieAnimationView animationView;
        LinearLayout linearLayout;
        TextView textView;


        public OnSwipeTouchListener(Context ctx, View mainView, PlayerView playerView, Activity activity, View second, int backOrFor, SeekBar seekBar, LottieAnimationView animationView, LinearLayout linearLayout, TextView textView) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
            mainView.setOnTouchListener(this);
            v = mainView;
            this.animationView = animationView;
            this.activity = activity;
            context = ctx;
            this.linearLayout = linearLayout;
            this.textView = textView;
            this.seekBar = seekBar;
            if (backOrFor == 1) {
                this.seekBar.setMax(((AudioManager) activity.getSystemService(Context.AUDIO_SERVICE)).getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            } else {
                this.seekBar.setMax(245);
            }
            this.v2 = second;
            this.backorFor = backOrFor;
            this.playerView = playerView;
            if (!hasWriteSettingsPermission(context))changeWriteSettingsPermission(context);

        }

        int brightnessValue = 100;

        float yst = 0f;
        float yTop = 0f;
        float yBottom = 0f;

        final int CHANGE_THRESOLD_TOUCH = 20;

        @Override
        public boolean onTouch(View v, MotionEvent event) {


            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (!((event.getY() > yTop) && (event.getY() < yBottom))) {
                    if (yst < event.getY()) {

                        if (backorFor == 2) {

                            if (brightnessValue >= 10) {
                                brightnessValue -= 10;
                                changeScreenBrightness(activity, brightnessValue);
                                seekBar.setProgress(brightnessValue);

                                if (seekBar.getVisibility() != View.VISIBLE) {
                                    seekBar.setVisibility(View.VISIBLE);
                                }
                                if (linearLayout.getVisibility() != View.VISIBLE) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                }

                                float percentage = (float) (brightnessValue * 100 / 245);
                                if (percentage > 100) {
                                    percentage = 100;
                                }
                                textView.setText((int) percentage + "%");
                            }
                        } else {


                            AudioManager mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                            try {
                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.round(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1), 0);
                                seekBar.setProgress(Math.round(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
                                if (seekBar.getVisibility() != View.VISIBLE) {
                                    seekBar.setVisibility(View.VISIBLE);
                                }
                                if (linearLayout.getVisibility() != View.VISIBLE) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                }
                                float percentage = (float) ((mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * 100) / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                                textView.setText((int) percentage + "%");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    } else if (yst > event.getY()) {
                        if (backorFor == 2) {
                            if (brightnessValue <= 245) {
                                brightnessValue += 10;
                                changeScreenBrightness(activity, brightnessValue);
                                seekBar.setProgress(brightnessValue);
                                if (seekBar.getVisibility() != View.VISIBLE) {
                                    seekBar.setVisibility(View.VISIBLE);
                                }
                                if (linearLayout.getVisibility() != View.VISIBLE) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                }
                                float percentage = (float) (brightnessValue * 100 / 245);
                                if (percentage > 100) {
                                    percentage = 100;
                                }
                                textView.setText((int) percentage + "%");
                            }
                        } else {
                            AudioManager mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                            try {
                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.round(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1), 0);
                                seekBar.setProgress(Math.round(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
                                if (seekBar.getVisibility() != View.VISIBLE) {
                                    seekBar.setVisibility(View.VISIBLE);
                                }
                                if (linearLayout.getVisibility() != View.VISIBLE) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                }
                                float percentage = (float) ((mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * 100) / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                                textView.setText((int) percentage + "%");
                            } catch (Exception e) {
                                Log.d("--Error", e.getMessage());
                            }

                        }

                    }

                    yst = event.getY();
                    yTop = yst - CHANGE_THRESOLD_TOUCH;
                    yBottom = yst + CHANGE_THRESOLD_TOUCH;

                }
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                yst = event.getY();
                yTop = yst - CHANGE_THRESOLD_TOUCH;
                yBottom = yst + CHANGE_THRESOLD_TOUCH;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activity.runOnUiThread(() -> {
                        linearLayout.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                    });
                }).start();
            }

            return gestureDetector.onTouchEvent(event);
        }


        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }


            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                playerView.showController();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        activity.runOnUiThread(() -> {
                            v2.setVisibility(View.GONE);
                            v.setVisibility(View.GONE);
                        });
                        while (playerView.isControllerVisible()) {

                        }
                        activity.runOnUiThread(() -> {
                            v.setVisibility(View.VISIBLE);
                            v2.setVisibility(View.VISIBLE);
                        });
                    }
                }.start();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (backorFor == 1) {
                    playerView.getPlayer().seekTo(playerView.getPlayer().getCurrentPosition() + 10000);
                } else {
                    playerView.getPlayer().seekTo(playerView.getPlayer().getCurrentPosition() - 10000);
                }

                animationView.setVisibility(View.VISIBLE);
                animationView.playAnimation();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        activity.runOnUiThread(() -> animationView.setVisibility(View.GONE));
                    }
                }.start();
                return true;
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private static  boolean hasWriteSettingsPermission(Context context) {
        return Settings.System.canWrite(context);
    }

    private static void changeWriteSettingsPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        context.startActivity(intent);
    }

    private static void changeScreenBrightness(Context context, int screenBrightnessValue) {
        Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        );
        Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue
        );
    }

    @Override
    public void onBackPressed() {
        player.release();
        super.onBackPressed();
    }
}
