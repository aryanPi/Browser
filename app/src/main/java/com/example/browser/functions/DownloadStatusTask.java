package com.example.browser.functions;

import android.app.Activity;
import android.app.DownloadManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.example.browser.activities.MainActivity;
import com.example.browser.fragment.downloading_frag;
import com.example.browser.model.DownloadModel;
import com.example.browser.utils.GlobalVariables;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadStatusTask extends AsyncTask<String, String, String> {


    DownloadModel downloadModelTask;
    Activity mActivity;
    int timeLimit = 500;


    public DownloadStatusTask(DownloadModel downloadModel) {
        this.downloadModelTask = downloadModel;
        this.mActivity = GlobalVariables.getmActivity();
    }


    @Override
    protected String doInBackground(String... strings) {
        downloadFileProcess(strings[0]);
        return null;
    }

    private void downloadFileProcess(String downloadId) {

        DownloadManager downloadManager = (DownloadManager) mActivity.getSystemService(DOWNLOAD_SERVICE);
        boolean downloading = true;

        int bytes_downloaded = 0;

        long startingTime = System.currentTimeMillis();
        String status = "";

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(Long.parseLong(downloadId));


        while (downloading) {
            Cursor cursor = downloadManager.query(query);
            cursor.moveToFirst();

            if (cursor.getCount() == 0) {
                break;
            }

            bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false;
            }


            if (GlobalVariables.isDownloadFragState()) {
                if ((System.currentTimeMillis() - startingTime) >= timeLimit) {
                    status = extraFunctions.getStatusMessage(cursor);
                    publishProgress(String.valueOf(bytes_downloaded), status, cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) + "");
                    startingTime = System.currentTimeMillis();
                }
            }

            cursor.close();
        }
        onPostExecute("" + bytes_downloaded);
    }

    @Override
    protected void onProgressUpdate(final String... values) {
        super.onProgressUpdate(values);
        ((downloading_frag) GlobalVariables.getDownloadingFrag()).updateView(downloadModelTask.getId(), Long.parseLong(values[0]), Long.parseLong(values[2]), false);
    }

    @Override
    protected void onPostExecute(String s) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (s != null) {
                if (downloadModelTask.getTotal_file_Size().equals(Long.parseLong(s))) {
                    ((MainActivity) mActivity).updateDownloadFromAsyncTaskDownloadManager(downloadModelTask.getId());
                    if (GlobalVariables.isDownloadFragState()) {
                        ((downloading_frag) GlobalVariables.getDownloadingFrag()).finishDownload(downloadModelTask.getId());
                    }
                }
            }else{
                Log.d("TAG", "onPostExecute: "+s+" : "+downloadModelTask.getStatus());
            }
        });
        super.onPostExecute(s);
    }
}

