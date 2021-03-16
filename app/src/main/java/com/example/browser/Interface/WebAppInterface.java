package com.example.browser.Interface;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

import com.example.browser.activities.MainActivity;
import com.example.browser.utils.GlobalVariables;

public class WebAppInterface {


    private Context context;
    private static String TAG = "aryan";
    private String VideoID = "";
    Activity mActivity;
    // TextView textView ;


    public WebAppInterface(Context context, Activity mActivity) {
        this.context = context;
        this.mActivity = mActivity;

    }


    @JavascriptInterface
    public void fun1(String msg) {
        if (!msg.equals(""))
            if (!msg.equals(this.VideoID)) {
                this.VideoID = msg;
                GlobalVariables.getWebPageDatas(GlobalVariables.getActiveWebView()).getDownloadLinks().add(0, msg);
            }
    }

    @JavascriptInterface
    public void playD() {
        if (MainActivity.dragView.getSpeed() != 1) {
            GlobalVariables.getmActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.dragView.setSpeed(1);
                    MainActivity.dragView.playAnimation();
                    MainActivity.active_download_bubble.playAnimation();
                }
            });
        } else {
            MainActivity.active_download_bubble.playAnimation();
        }


    }

    @JavascriptInterface
    public void pauseD() {
        if (MainActivity.dragView.getSpeed() != -1) {
            GlobalVariables.getmActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.dragView.setSpeed(-1);
                    MainActivity.dragView.playAnimation();
                }
            });
        }
    }
}
