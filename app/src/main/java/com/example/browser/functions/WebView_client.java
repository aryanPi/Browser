package com.example.browser.functions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.example.browser.R;
import com.example.browser.activities.MainActivity;
import com.example.browser.activities.constants;
import com.example.browser.model.HistoryModel;
import com.example.browser.model.TabModel;
import com.example.browser.model.extra;
import com.example.browser.utils.GlobalVariables;
import com.monstertechno.adblocker.AdBlockerWebView;
import com.monstertechno.adblocker.util.AdBlocker;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoExtractor;
import vimeoextractor.VimeoVideo;

public class WebView_client extends WebViewClient {


    public WebView_client() {
    }


    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (GlobalVariables.getActiveWebView() == view.getId()) {
            if (GlobalVariables.getFlag() == 0) {
                String requestUrl = request.getUrl().toString();


                if (requestUrl.startsWith("whatsapp://")) {
                    shouldOverrideUrlLoading(view, requestUrl);
                }

                String extension = MimeTypeMap.getFileExtensionFromUrl(requestUrl);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                if (mimeType != null) {
                    if (mimeType.startsWith("video/")) {
                        if (!GlobalVariables.getWebPageDatas(view.getId()).getDownloadLinks().contains(requestUrl)) {
                            GlobalVariables.getWebPageDatas(view.getId()).getDownloadLinks().add(0, requestUrl);
                            GlobalVariables.setUrl(requestUrl);
                            GlobalVariables.setVideoState(true);
                            GlobalVariables.videoState = true;
                            GlobalVariables.getmActivity().runOnUiThread(() -> {
                                if (MainActivity.dragView.getSpeed() != 1) {
                                    MainActivity.dragView.setSpeed(1);
                                    MainActivity.dragView.playAnimation();
                                    MainActivity.active_download_bubble.playAnimation();
                                } else {
                                    MainActivity.active_download_bubble.playAnimation();
                                }
                            });
                        }
                    }
                }
            }
        }
//        Log.d("TAGD", "shouldInterceptRequest: "+request);

        return super.shouldInterceptRequest(view, request);
    }


    @SuppressWarnings("deprecation")
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        //Log.d("TAGD", "shouldInterceptRequest: "+url);
        return AdBlockerWebView.blockAds(view, url) ? AdBlocker.createEmptyResource() :
                super.shouldInterceptRequest(view, url);

    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);

        GlobalVariables.getWebPageDatas(view.getId()).setUrl(url);
        GlobalVariables.getWebPageDatas(view.getId()).setTitle(view.getTitle());


        if (!(GlobalVariables.isStateOfRestoreTabs() <= 0)) {
            GlobalVariables.setStateOfRestoreTabs(GlobalVariables.isStateOfRestoreTabs() - 1);
        } else {
            if (!(view.getUrl().equals("about:blank"))) {


                ArrayList<Integer> allOpenIds = new ArrayList<>();


                for (TabModel t : GlobalVariables.getTabModels()) {
                    allOpenIds.add(t.getId());
                }
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();


                RealmResults<HistoryModel> results = realm.where(HistoryModel.class).equalTo("stateOfPage", true).findAll();

                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i) != null && !allOpenIds.contains(results.get(i).getWebviewID())) {
                        results.get(i).setStateOfPage(false);
                    }
                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                System.out.println(formatter.format(date));

                realm.copyToRealm(new HistoryModel(GlobalVariables.getHistoryStack(), view.getUrl(), view.getTitle(), "", true, view.getId(), date));
                realm.where(extra.class).findFirst().setHistoryStack(GlobalVariables.getHistoryStack() + 1);

                realm.commitTransaction();

                GlobalVariables.setHistoryStack(GlobalVariables.getHistoryStack() + 1);

            }

        }
        ((MainActivity) GlobalVariables.getmActivity()).findViewById(R.id.layout_sear_toolbar).setVisibility(View.VISIBLE);
        ((MainActivity) GlobalVariables.getmActivity()).searchView_toolBar.setText(view.getTitle());
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("TAGD", "shouldOverrideUrlLoading: before " + url);
        if (url != null)
            if (url.startsWith("tel:")) {
                view.getContext().startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
            } else if ( url.startsWith("whatsapp://")) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else if ( url.startsWith("geo:")) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        extraFunctions.resetDownloadBtn();

        if (GlobalVariables.getWebPageDatas(view.getId()) != null)
            GlobalVariables.getWebPageDatas(view.getId()).setDownloadLinks(new ArrayList<>());

        if (url.equals("about:blank")) {
            TabModel tabModel = new TabModel(view.getId(), "HOME PAGE", "");
            GlobalVariables.setTabModels(view.getId(),tabModel);
        } else {
            TabModel tabModel = new TabModel(view.getId(), view.getTitle(), view.getUrl());
            GlobalVariables.setTabModels(view.getId(),tabModel);
        }
        if (url.contains("fb.com") || url.contains("facebook.com")) {
            view.loadUrl(constants.FB_JS);
            GlobalVariables.getWebPageDatas(view.getId()).customScriptSite(true);
            GlobalVariables.setFlag(1);
        } else if (url.contains("insta")) {
            GlobalVariables.setFlag(2);
            view.loadUrl(constants.INSTA_JS);
            GlobalVariables.getWebPageDatas(view.getId()).customScriptSite(true);
        } else if (url.contains("vimeo")) {
            GlobalVariables.setFlag(3);
            GlobalVariables.getWebPageDatas(view.getId()).customScriptSite(true);
            if (!url.contains("vimeo.com/watch")){
                GlobalVariables.setVideoState(true);
                GlobalVariables.videoState = true;
                GlobalVariables.getmActivity().runOnUiThread(() -> {
                    if (MainActivity.dragView.getSpeed() != 1) {
                        MainActivity.dragView.setSpeed(1);
                        MainActivity.dragView.playAnimation();
                        MainActivity.active_download_bubble.playAnimation();
                    } else {
                        MainActivity.active_download_bubble.playAnimation();
                    }
                });
            }
        } else {
            GlobalVariables.setFlag(0);
            GlobalVariables.getWebPageDatas(view.getId()).customScriptSite(false);
        }

    }


    @Override
    public void onPageFinished(WebView view, String url) {


        if (url.equals("about:blank")) {
            TabModel tabModel = new TabModel(view.getId(), "HOME PAGE", "");
            GlobalVariables.setTabModels(view.getId(),tabModel);
        } else {
            TabModel tabModel = new TabModel(view.getId(), view.getTitle(), view.getUrl());
            GlobalVariables.setTabModels(view.getId(),tabModel);
        }

        super.onPageFinished(view, url);


    }

    @Override
    public void onLoadResource(WebView view, String url) {
        if (GlobalVariables.getFlag() == 1) {
            if (!GlobalVariables.isVideoState())
                view.loadUrl("javascript:updateVideo('" + GlobalVariables.url + "');");
        } else if (GlobalVariables.getFlag() == 2) {
            view.loadUrl("javascript:updateVideo();");
        }


        super.onLoadResource(view, url);
    }


    public String checkUrl(String str) {
        if (str == null) {
            return str;
        }
        StringBuilder stringBuilder;
        if (Build.VERSION.SDK_INT < 28) {
            if (!str.startsWith("http")) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("http://");
                stringBuilder.append(str);
                str = stringBuilder.toString();
            }
            return str;

        } else if (str.startsWith("https")) {
            return str;
        } else {
            if (str.startsWith("http")) {
                return str.replaceFirst("http", "https");
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("https://");
            stringBuilder.append(str);
            return stringBuilder.toString();
        }
    }
}

