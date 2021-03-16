package com.example.browser.functions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.browser.R;
import com.example.browser.activities.MainActivity;
import com.example.browser.model.HistoryModel;
import com.example.browser.utils.GlobalVariables;

import io.realm.Realm;
import io.realm.RealmResults;

public class ChromeClient extends WebChromeClient {


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getId() == view.getId()) {
            ((MainActivity) GlobalVariables.getmActivity()).progressBar.setVisibility(View.VISIBLE);
            ((MainActivity) GlobalVariables.getmActivity()).progressBar.setProgress(newProgress);
            if (newProgress == 100)
                ((MainActivity) GlobalVariables.getmActivity()).progressBar.setVisibility(View.GONE);
        }

        super.onProgressChanged(view, newProgress);

    }


    @Nullable
    @Override
    public View getVideoLoadingProgressView() {
        Log.d("TAGD", "getVideoLoadingProgressView: ");
        return super.getVideoLoadingProgressView();
    }


    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }


    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
        if (!GlobalVariables.isIncognitoMode()) {


            if (GlobalVariables.getWebPageDatas(view.getId())!=null)
                GlobalVariables.getWebPageDatas(view.getId()).setFrvicon(extraFunctions.convert(icon));
            else view.destroy();

            if (GlobalVariables.getActiveWebView()==view.getId())
                Glide.with(GlobalVariables.getmActivity()).load(icon).circleCrop().into((((MainActivity)GlobalVariables.getmActivity()).logoOfSiteInToolbar));


            Realm.getDefaultInstance().executeTransaction(realm -> {
                RealmResults<HistoryModel> results = realm.where(HistoryModel.class).equalTo("url", view.getUrl()).equalTo("logo", "").findAll();
                for (int i = 0; i < results.size(); i++) {
                    results.get(i).setLogo(extraFunctions.convert(icon));
                }
            });
        }
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d("TAG", "onConsoleMessage: " + consoleMessage.message());
        return true;
    }


    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        Log.d("TAGD", "onCreateWindow: "+isUserGesture+isDialog);
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        Log.d("TAGD", "onShowCustomView: ");

        super.onShowCustomView(view, callback);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        Log.d("TAGD", "onReceivedTouchIconUrl: "+url);

        super.onReceivedTouchIconUrl(view, url, precomposed);
    }



    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;

    @Override
    public Bitmap getDefaultVideoPoster()
    {
        if (mCustomView == null) {
            return null;
        }
        return BitmapFactory.decodeResource(GlobalVariables.getmActivity().getResources(), R.drawable.ic_video_icon);
    }


    public void onHideCustomView()
    {
        ((FrameLayout)GlobalVariables.getmActivity().getWindow().getDecorView()).removeView(this.mCustomView);
        this.mCustomView = null;
        GlobalVariables.getmActivity().getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
        GlobalVariables.getmActivity().setRequestedOrientation(this.mOriginalOrientation);
        this.mCustomViewCallback.onCustomViewHidden();
        this.mCustomViewCallback = null;
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        if (this.mCustomView != null)
        {
            onHideCustomView();
            return;
        }
        this.mCustomView = view;
        this.mOriginalSystemUiVisibility = GlobalVariables.getmActivity().getWindow().getDecorView().getSystemUiVisibility();
        this.mOriginalOrientation = GlobalVariables.getmActivity().getRequestedOrientation();
        this.mCustomViewCallback = callback;
        ((FrameLayout)GlobalVariables.getmActivity().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
        GlobalVariables.getmActivity().getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        super.onShowCustomView(view, requestedOrientation, callback);
    }


}

