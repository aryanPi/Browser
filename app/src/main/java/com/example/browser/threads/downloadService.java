package com.example.browser.threads;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.Constants;
import com.example.browser.activities.Downloads;
import com.example.browser.activities.MainActivity;
import com.example.browser.R;
import com.example.browser.fragment.downloading_frag;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.DownloadModel;
import com.example.browser.model.extra;
import com.example.browser.utils.GlobalVariables;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import alirezat775.lib.downloader.Downloader;
import alirezat775.lib.downloader.core.OnDownloadListener;
import io.realm.Realm;

public class downloadService extends Service {


    private static final String TAG = "aryantomarService";

    NotificationCompat.Builder notification;

    RemoteViews longnoti;

    @SuppressLint("StaticFieldLeak")
    public static Service THIS;

    static ArrayList<Downloader> downloaderArrayList = new ArrayList<>();

    static HashMap<Long, Integer> idAndIndex = new HashMap<Long, Integer>();

    public static DownloadModel downloadModel;

    public static boolean isResume = false;


    long lenghtOfFile;


    public static void cancelDownload(long id) {
        if (idAndIndex.get(id) != null)
            if (downloaderArrayList.get(idAndIndex.get(id)) != null)
                downloaderArrayList.get(idAndIndex.get(id)).cancelDownload();
    }

    public static void pauseDownload(long id) {
        if (idAndIndex.get(id) != null)
            if (downloaderArrayList.get(idAndIndex.get(id)) != null)
                downloaderArrayList.get(idAndIndex.get(id)).pauseDownload();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        THIS = this;

        startDownload();

        return START_NOT_STICKY;
    }

    private void startDownload() {

        longnoti = new RemoteViews(getPackageName(), R.layout.longnoti);

        createNotificationChannel(getPackageName() + downloadModel.getId(), getPackageName() + downloadModel.getId() + "N");

        notification = new NotificationCompat.Builder(this, getPackageName() + downloadModel.getId())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setCustomBigContentView(longnoti)
                .setPriority(NotificationCompat.PRIORITY_LOW);


        startForeground(downloadModel.getId().intValue(), notification.build());


        new Thread() {
            @Override
            public void run() {
                super.run();

                ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
                ActivityManager.getMyMemoryState(myProcess);

                startDownloading(downloadModel.getTitle(), downloadModel.getUrl(), downloadModel.getFile_path(), downloadModel.getId());

            }
        }.start();
    }


    private void createNotificationChannel(String CH_id, String Name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CH_id,
                    Name,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setSound(null, null);
            serviceChannel.enableVibration(false);
            serviceChannel.enableLights(false);
            serviceChannel.setImportance(NotificationManager.IMPORTANCE_LOW);

            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(serviceChannel);
        }
    }


    @SuppressLint("CheckResult")
    private void startDownloading(String fileName, String downloadURL, String filePath, long downloadId) {

        if (!checkThePermissionAndTesting()) {

            new Handler(Looper.getMainLooper()).post(() -> {
                Realm.getDefaultInstance().executeTransaction(realm -> realm.where(DownloadModel.class).equalTo("id", downloadId).findFirst().setStatus(Constants.DOWNLOAD_STATUS.FAILED));
                ((MainActivity)GlobalVariables.getmActivity()).failedDialoge(downloadModel);
            });
            faildedDownload(downloadId);
            stopForeground(true);
            stopSelf();
            return;
        }

        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);

        NotificationCompat.Builder builder = null;


        NotificationManagerCompat manager = null;


        longnoti = new RemoteViews(getPackageName(), R.layout.longnoti);

        Intent intent = new Intent(getApplicationContext(), Downloads.class);
        Random generator = new Random();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), generator.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);


        longnoti.setOnClickPendingIntent(R.id.fullNotification, pendingIntent);

        manager = NotificationManagerCompat.from(this);
        createNotificationChannel(getPackageName() + downloadId, getPackageName() + downloadId + "Name");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this, getPackageName() + downloadId)
                    .setCustomBigContentView(longnoti)
                    .setSmallIcon(R.drawable.ic_launcher_background);
        }

        manager.notify((int) downloadId, builder.build());


        final long[] startingTime = {System.currentTimeMillis(), System.currentTimeMillis()};
        int timeLimit = 1000;
        final long[] PreDownloadedBytes = {0};
        RemoteViews finalLongnoti = longnoti;

        NotificationCompat.Builder finalBuilder = builder;
        NotificationManagerCompat finalManager = manager;

        Downloader.Builder downloader = new Downloader.Builder(getApplicationContext(), downloadURL)
                .downloadDirectory(filePath)
                .fileName(fileName.substring(0, fileName.lastIndexOf(".") - 1), fileName.substring(fileName.lastIndexOf(".") + 1))
                .downloadListener(new OnDownloadListener() {
                    @Override
                    public void onStart() {
                        new Handler(Looper.getMainLooper()).post(() -> Realm.getDefaultInstance().executeTransaction(realm -> {
                        }));
                        new Handler(Looper.getMainLooper()).post(() -> Realm.getDefaultInstance().executeTransaction(realm -> {
                            realm.where(DownloadModel.class).equalTo("id", downloadId).findFirst().setStatus(Constants.DOWNLOAD_STATUS.RUNNING);
                        }));
                        finalLongnoti.setTextViewText(R.id.titleFileNotification, fileName);
                        finalLongnoti.setTextViewText(R.id.totalFileSizeNotification, android.text.format.Formatter.formatFileSize(getApplication(), lenghtOfFile) + "");

                        upadteNotification(finalBuilder, finalLongnoti, finalManager, downloadId);
                    }

                    @Override
                    public void onPause() {
                        downloadModel = null;
                        new Handler(Looper.getMainLooper()).post(() -> Realm.getDefaultInstance().executeTransaction(realm -> {
                            realm.where(DownloadModel.class).equalTo("id", downloadId).findFirst().setStatus(Constants.DOWNLOAD_STATUS.PAUSE);
                        }));
                        stopForeground(true);
                        stopSelf();

                    }

                    @Override
                    public void onResume() {

                    }

                    @Override
                    public void onProgressUpdate(int i, int i1, int i2) {
                        if ((System.currentTimeMillis() - startingTime[0]) >= timeLimit) {
                            String speed = android.text.format.Formatter.formatFileSize(getApplication(), (int) (i1 - PreDownloadedBytes[0]));
                            updateSpeed(speed, downloadId);
                            finalLongnoti.setTextViewText(R.id.speedNotification, speed + "/s");
                            finalLongnoti.setProgressBar(R.id.progress_horizontal2, 100, i, false);
                            finalLongnoti.setTextViewText(R.id.downloadedSizeNotification, android.text.format.Formatter.formatFileSize(getApplication(), i1) + "");
                            finalLongnoti.setTextViewText(R.id.totalFileSizeNotification, android.text.format.Formatter.formatFileSize(getApplication(), i2) + "");

                            upadteNotification(finalBuilder, finalLongnoti, finalManager, downloadId);

                            PreDownloadedBytes[0] = i1;
                            startingTime[0] = System.currentTimeMillis();
                        }

                        new Handler(Looper.getMainLooper()).post(() -> Realm.getDefaultInstance().executeTransaction(realm -> realm.where(DownloadModel.class).equalTo("id", downloadId).findFirst().setProgress(i)));


                        ActivityManager.getMyMemoryState(myProcess);
                        boolean isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;

                        if (!isInBackground) {
                            if ((System.currentTimeMillis() - startingTime[1]) >= timeLimit) {
                                updateProgress(i, i1, downloadId);
                                startingTime[1] = System.currentTimeMillis();
                            }
                        }

                    }

                    @Override
                    public void onCompleted(@org.jetbrains.annotations.Nullable File file) {
                        downloadModel = null;
                        new Handler(Looper.getMainLooper()).post(() -> Realm.getDefaultInstance().executeTransaction(realm -> {
                            realm.where(DownloadModel.class).equalTo("id", downloadId).findFirst().setFile_path(file.getAbsolutePath());
                            realm.where(DownloadModel.class).equalTo("id", downloadId).findFirst().setStatus(Constants.DOWNLOAD_STATUS.COMPLETE);
                        }));

                        new Handler(Looper.getMainLooper()).post(() -> Realm.getDefaultInstance().executeTransaction(realm -> {
                            if (realm.where(extra.class).findFirst().isDownloadCompleteRingtone()) {
                                int resID = getResources().getIdentifier("download_ringtone.mp3", "raw", getPackageName());
                                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);
                                mediaPlayer.start();
                            }
                            if (realm.where(extra.class).findFirst().isDownloadCompleteVibration()) {
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                } else {
                                    v.vibrate(500);
                                }
                            }

                        }));


                        downloadFinish(downloadId);
                        stopForeground(true);
                        stopSelf();
                    }

                    @Override
                    public void onFailure(@org.jetbrains.annotations.Nullable String s) {
                        Toast.makeText(getApplicationContext(), "File has unexcepted end !", Toast.LENGTH_SHORT).show();
                        downloadModel = null;
                        new Handler(Looper.getMainLooper()).post(() -> Realm.getDefaultInstance().executeTransaction(realm -> realm.where(DownloadModel.class).equalTo("id", downloadId).findFirst().setStatus(Constants.DOWNLOAD_STATUS.FAILED)));
                        faildedDownload(downloadId);
                        stopForeground(true);
                        stopSelf();
                    }

                    @Override
                    public void onCancel() {
                        downloadModel = null;
                        new Handler(Looper.getMainLooper()).post(() -> Realm.getDefaultInstance().executeTransaction(realm -> {
                            realm.where(DownloadModel.class).equalTo("id", downloadId).findFirst().deleteFromRealm();
                        }));
                        stopForeground(true);
                        stopSelf();
                    }
                });

        Downloader downloadObject = downloader.build();
        if (isResume) {
            downloadObject.resumeDownload();
            isResume = false;
        } else {
            downloadObject.download();
        }
        downloaderArrayList.add(downloadObject);
        idAndIndex.put(downloadId, downloaderArrayList.indexOf(downloadObject));

    }


    private boolean checkThePermissionAndTesting() {
        ((MainActivity) GlobalVariables.getmActivity()).askPermission("Storage permission is needed to Write Downloading data !");

        try {
            File file = new File(downloadModel.getFile_path() + "/test.temp");

            if (file.createNewFile())
                if (file.delete()) return true;
                else return false;
            else return false;

        } catch (IOException e) {
            return false;
        }
    }


    private void upadteNotification(NotificationCompat.Builder builder, RemoteViews longnt, NotificationManagerCompat managerCompat, long id) {
        builder.setCustomBigContentView(longnt);
        managerCompat.notify((int) id, builder.build());
    }


    private void updateSpeed(String speed, long id) {
        if (GlobalVariables.isDownloadFragState())
            new Handler(Looper.getMainLooper()).post(() -> ((downloading_frag) GlobalVariables.getDownloadingFrag()).updateSpeed(id, speed));
    }

    private void updateProgress(Integer updateProgress, int s, long id) {
        if (GlobalVariables.isDownloadFragState()) {
            new Handler(Looper.getMainLooper()).post(() -> ((downloading_frag) GlobalVariables.getDownloadingFrag()).updateView(id, Long.parseLong(s + ""), Long.valueOf(updateProgress), true));
        }
    }

    private void downloadFinish(long id) {
        if (GlobalVariables.isDownloadFragState())
            new Handler(Looper.getMainLooper()).post(() -> ((downloading_frag) GlobalVariables.getDownloadingFrag()).finishDownload(id));
    }

    private void faildedDownload(long downloadId) {
        if (GlobalVariables.isDownloadFragState())
            new Handler(Looper.getMainLooper()).post(() -> ((downloading_frag) GlobalVariables.getDownloadingFrag()).failedDownload(downloadId));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
