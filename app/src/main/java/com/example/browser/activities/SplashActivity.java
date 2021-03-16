package com.example.browser.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.browser.R;
import com.example.browser.model.extra;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Browser);
        setContentView(R.layout.activity_splash);
        final boolean[] firstTime = {false};
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                if (realm.where(extra.class).findAll().size()==0) {
                    firstTime[0] = true;
                }
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (firstTime[0]){
                startActivity(new Intent(SplashActivity.this, FirstTimeActivity.class));
            }else{
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        }, 3000);

    }
}