package com.example.browser.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;


import com.example.browser.activities.SplashActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private static final String CHANNEL_ID = "Id";
    public static RealmConfiguration config2;
    public static Thread background;
    @Override
    public void onCreate() {
        super.onCreate();

        

        background = new Thread() {
            public void run() {
                try {
                    Intent i=new Intent(getBaseContext(), SplashActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();

        config2 = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {

            }
        });
        Realm.setDefaultConfiguration(config);

/*
        PRDownloaderConfig.newBuilder().setDatabaseEnabled(true).build();


        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .build();

        Fetch.Impl.setDefaultInstanceConfiguration(fetchConfiguration);
*/

        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        createNotificationChannel();

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
