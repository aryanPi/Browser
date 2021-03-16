package com.example.browser.threads;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.Constants;
import com.example.browser.R;
import com.example.browser.activities.Downloads;
import com.example.browser.fragment.audio_frag;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.ModelAudio;
import com.example.browser.utils.PathUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class AudioplayerService extends Service {

    private static final String LOG_TAG = "AryanTT";

    public static ArrayList<ModelAudio> lst;
    public static int playIndex;

    public static boolean isPlayingAudioService;


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
            switch (intent.getAction()) {
                case Constants.ACTION.STARTFOREGROUND_ACTION:
                    try {
                        playAudio(PathUtil.getPath(this, lst.get(playIndex).getData()));
                        createChannel();
                        showNotification();
                        bigViews.setTextViewText(R.id.titleOfTheAudio, lst.get(playIndex).getTitle());
                        views.setTextViewText(R.id.titleOfTheAudioSmallNotification, lst.get(playIndex).getTitle());
                        updateNoti();
                        isPlayingAudioService = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    updateNoti();
                    break;
                case Constants.ACTION.PREV_ACTION:
                    if (playIndex > 0) {
                        playIndex--;
                        try {
                            mp.stop();
                            mp.release();
                            playAudio(PathUtil.getPath(this, lst.get(playIndex).getData()));
                            bigViews.setTextViewText(R.id.titleOfTheAudio, lst.get(playIndex).getTitle());
                            views.setTextViewText(R.id.titleOfTheAudioSmallNotification, lst.get(playIndex).getTitle());
                            updateNoti();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Constants.ACTION.PLAY_ACTION:
                    playAndPause();
                    break;
                case Constants.ACTION.NEXT_ACTION:
                    playNext();
                    break;
                case Constants.ACTION.SEEK_ACTION:
                    mp.seekTo(Integer.parseInt(((audio_frag) audio_frag.THIS).seekTo));
                    break;
                case Constants.ACTION.GET_INFO:
                    if (mp == null) {
                        ((audio_frag) audio_frag.THIS).playingState = -1;
                    } else {
                        ((audio_frag) audio_frag.THIS).playingState = mp.isPlaying() ? 1 : 0;
                        ((audio_frag) audio_frag.THIS).currentDur = mp.getCurrentPosition();
                        ((audio_frag) audio_frag.THIS).totalDur = mp.getDuration();
                        ((audio_frag) audio_frag.THIS).titleAudio = lst.get(playIndex).getTitle();
                    }

                    break;
                case Constants.ACTION.STOPFOREGROUND_ACTION:
                    if (((audio_frag) audio_frag.THIS).isActive)
                        ((audio_frag) audio_frag.THIS).playingState = -1;
                    mp = null;
                    if (((audio_frag) audio_frag.THIS).isActive)
                        ((audio_frag) audio_frag.THIS).updateControlsView();
                    stopForeground(true);
                    stopSelf();
                    break;
            }
        return START_STICKY;
    }

    private void playNext() {
        if ((lst.size() - 1) > playIndex) {
            playIndex++;
            try {
                Log.d("TAGD", "playNext: " + lst.get(playIndex).getData());
                mp.release();
                playAudio(PathUtil.getPath(this, lst.get(playIndex).getData()));
                bigViews.setTextViewText(R.id.titleOfTheAudio, lst.get(playIndex).getTitle());
                views.setTextViewText(R.id.titleOfTheAudioSmallNotification, lst.get(playIndex).getTitle());
                updateNoti();
            } catch (URISyntaxException e) {
                e.printStackTrace();

            }
        }
    }

    private void playAndPause() {
        if (mp.isPlaying()) {
            mp.pause();
            bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_final);
            views.setImageViewResource(R.id.status_bar_playSmallNotification, R.drawable.ic_play_final);
            if (((audio_frag) audio_frag.THIS).isActive)
                ((audio_frag) audio_frag.THIS).playingState = 0;
        } else {
            mp.start();
            bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause);
            views.setImageViewResource(R.id.status_bar_playSmallNotification, R.drawable.ic_pause);
            onProgressUpdate();
            if (((audio_frag) audio_frag.THIS).isActive)
                ((audio_frag) audio_frag.THIS).playingState = 1;
        }
        updateNoti();
    }

    public void updateNoti() {
        status.contentView = views;
        status.bigContentView = bigViews;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
        if (((audio_frag) audio_frag.THIS).isActive)
            ((audio_frag) audio_frag.THIS).updateControlsView();
    }

    public static MediaPlayer mp;

    private void playAudio(String path) {
        mp = new MediaPlayer();
        try {
            mp.setDataSource(path);
            mp.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "File not exist !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        mp.prepareAsync();

        mp.setOnPreparedListener(mp -> {
            mp.start();

            bigViews.setTextViewText(R.id.totalDurationAudioNotification, extraFunctions.getDuration(mp.getDuration()));
            updateNoti();
            onProgressUpdate();
        });

        mp.setOnCompletionListener(mp -> {
            if (playIndex < lst.size()) {
                playNext();
            }
        });

    }


    private void onProgressUpdate() {
        new Thread(() -> {

            while (true) {
                try {
                    if (mp != null)
                        if (mp.isPlaying()) {
                            String temp = extraFunctions.getDuration(mp.getCurrentPosition());
                            updateProgress((mp.getCurrentPosition() * 100 + (mp.getDuration() >> 1)) / mp.getDuration(), temp);
                            if (((audio_frag) audio_frag.THIS).isActive) {
                                ((audio_frag) audio_frag.THIS).currentDur = mp.getCurrentPosition();
                            }

                        } else if (mp.getDuration() == mp.getCurrentPosition()) {
                            mp.stop();
                            mp.release();
                            stopForeground(true);
                            stopSelf();
                        } else {
                            break;
                        }
                    else break;
                } catch (Exception e) {
                    break;
//                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateProgress(int i, String currentDuration) {
        bigViews.setProgressBar(R.id.progressBarAudioPlayingNotification, 100, i, false);
        bigViews.setTextViewText(R.id.currentDurationAudioNotification, currentDuration);
        status.bigContentView = bigViews;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

    RemoteViews bigViews;
    Notification status;
    RemoteViews views;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification() {

        views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);


        Intent notificationIntent = new Intent(this, Downloads.class);
        notificationIntent.putExtra("fragIndex", 3);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, AudioplayerService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, AudioplayerService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, AudioplayerService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, AudioplayerService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_playSmallNotification, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);


        views.setOnClickPendingIntent(R.id.closeNotificationAudioSmall, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.closeAudioNotificationService, pcloseIntent);

        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setTextViewText(R.id.titleOfTheAudioSmallNotification, "--");
        bigViews.setTextViewText(R.id.titleOfTheAudio, "--");


        status = new Notification.Builder(this).setChannelId("112").build();

        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_musical;
        status.contentIntent = pendingIntent;

        updateNoti();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                "112",
                "videodiscs",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        serviceChannel.setSound(null, null);
        serviceChannel.enableVibration(false);
        serviceChannel.enableLights(false);
        serviceChannel.setImportance(NotificationManager.IMPORTANCE_LOW);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isPlayingAudioService = false;
        return super.onUnbind(intent);
    }
}
