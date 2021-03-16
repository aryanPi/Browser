package com.example.browser.functions;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Base64;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.browser.R;
import com.example.browser.activities.MainActivity;
import com.example.browser.fragment.downloading_frag;
import com.example.browser.model.BookmarksModel;
import com.example.browser.model.DownloadModel;
import com.example.browser.utils.GlobalVariables;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import io.realm.Realm;

import static android.content.Context.DOWNLOAD_SERVICE;


public class extraFunctions {

    public static String TAG = "tomar";

    public static boolean addBookmarkBtn() {
        if (MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl() != null) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealm(new BookmarksModel(
                    MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl(),
                    MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getTitle(),
                    convert(MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getFavicon())));
            realm.commitTransaction();
            return true;
        }
        return false;
    }


    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap) {
        if (bitmap == null) return "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static void removeBookmark() {
        if (MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl() != null) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.where(BookmarksModel.class).equalTo("url", MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl()).findFirst().deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public static void resetDownloadBtn() {
        GlobalVariables.setUrl("");
        GlobalVariables.setVideoState(false);
        GlobalVariables.videoState = false;
        //   MainActivity.dragView.setImageBitmap(getRoundedShape(BitmapFactory.decodeResource(GlobalVariables.getResources(), R.drawable.download_inactive_btn)));

        if (MainActivity.dragView.getSpeed() != -1)
            GlobalVariables.getmActivity().runOnUiThread(() -> {
                MainActivity.dragView.setSpeed(-1);
                MainActivity.dragView.playAnimation();

            });

    }

    public static void desktopViewChange(boolean isChecked) {
        WebView webView = MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView());
        String userAgent = webView.getSettings().getUserAgentString();
        if (webView.getUrl() == null) return;
        if (isChecked) {

            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);

            webView.getSettings().setUserAgentString("Desktop");
            webView.reload();

            if (webView.getUrl().startsWith("https://m.")) {
                webView.loadUrl("https://www" + webView.getUrl().substring(webView.getUrl().indexOf(".")));
            }

        } else {
            userAgent = null;

            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);

            webView.getSettings().setUserAgentString(userAgent);
            webView.reload();
        }
    }


    public static void shareFile(String path) {
        if (path != null) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);

            if (!path.startsWith("file")) {
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, path);
            } else {
                Uri screenshotUri = Uri.parse(path);
                sharingIntent.setType("file/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            }


            (GlobalVariables.getmActivity()).startActivity(Intent.createChooser(sharingIntent, "Share"));
        }
    }

    public static String getStatusMessage(Cursor cursor) {
        String msg;
        switch (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Failed";
                break;
            case DownloadManager.STATUS_PAUSED:
                msg = "Paused";
                break;
            case DownloadManager.STATUS_RUNNING:
                msg = "Running";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Complete";
                break;
            case DownloadManager.STATUS_PENDING:
                msg = "Pending";
                break;
            default:
                msg = "Unknown";
                break;
        }
        return msg;
    }

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {

        int targetWidth = 150;
        int targetHeight = 150;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        canvas.drawBitmap(scaleBitmapImage,
                new Rect(0, 0, scaleBitmapImage.getWidth(),
                        scaleBitmapImage.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }

    public static String ValidateUrl(String url) {
        String prefix = GlobalVariables.getExtraData().getDefaultSearchEngine();
        String re = "";

        if (url.startsWith("whatsapp://")) {
            return url;
        }
        if (url.contains(" ")) return prefix + url;
        if (!url.startsWith("http://") && !url.startsWith("https://") &&
                !url.endsWith(".com")) {
            re = prefix + url;
        }
        if (url.endsWith(".com") || url.endsWith(".as") || url.endsWith(".uk") || url.endsWith(".biz") || url.endsWith(".org")) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                re = "https://" + url;
            }
        }
        if (re.equals(""))
            return url;
        return re;
    }

    public static void change(TextView fromThis, TextView toThis, Context context) {
        toThis.setTextColor(Color.parseColor("#FFFFFFFF"));
        toThis.setBackground(AppCompatResources.getDrawable(context, R.drawable.sub_frag_files_selector_subfrag));
        fromThis.setTextColor(Color.parseColor("#77FFFFFF"));
        fromThis.setBackgroundColor(Color.parseColor("#00000000"));

    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {

            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(GlobalVariables.getmActivity(), "Website Not Allowed ! ", Toast.LENGTH_SHORT).show();
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static void downloadWithDownloadManager(DownloadModel downloadModel) {


        DownloadManager.Request request;
        request = new DownloadManager.Request(Uri.parse(downloadModel.getUrl()))
                .setTitle(downloadModel.getTitle())
                .setDescription("Running")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(new File(downloadModel.getFile_path())))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        if (GlobalVariables.getExtraData().isDownloadOnlyWtithWifi()) {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        }

        DownloadManager downloadManager = (DownloadManager) GlobalVariables.getmActivity().getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        Realm.getDefaultInstance().executeTransaction(realm -> {
            DownloadModel downloadModel1 = realm.where(DownloadModel.class).equalTo("id", downloadModel.getId()).findFirst();
            downloadModel1.setDownloadingWith(false);
            downloadModel1.setDownloadManagerId(downloadId);
        });

        downloadModel.setDownloadManagerId(downloadId);

        if (GlobalVariables.isDownloadFragState())
            ((downloading_frag) downloading_frag.mFragment).addNewDownloadItem(downloadModel);

        DownloadStatusTask downloadStatusTask = new DownloadStatusTask(downloadModel);
        try {
            downloadStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(downloadId));
        } catch (Exception e) {
            Toast.makeText(GlobalVariables.getmActivity(), "Error first", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String getDuration(int duration) {


        String duration_formatted;
        int sec = (duration / 1000) % 60;
        int min = (duration / (1000 * 60)) % 60;
        int hrs = duration / (1000 * 60 * 60);
        if (hrs == 0) {
            duration_formatted = String.valueOf(min).concat(":".concat(String.format(Locale.UK, "%02d", sec)));
        } else {
            duration_formatted = String.valueOf(hrs).concat(":".concat(String.format(Locale.UK, "%02d", min).concat(":".concat(String.format(Locale.UK, "%02d", sec)))));
        }

        return duration_formatted;
    }

    public static String getMonth(Integer month) {
        String monthString = "";
        switch (month) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
            default:
                monthString = "Invalid month";
                break;
        }
        return monthString;
    }

}

