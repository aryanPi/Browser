package com.example;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.browser.R;

public class Constants {
    public interface ACTION {

        String PREV_ACTION = "5m";
        String PLAY_ACTION = "4mani";
        String NEXT_ACTION = "3m";
        String STARTFOREGROUND_ACTION = "1m";
        String STOPFOREGROUND_ACTION = "0a";
        String SEEK_ACTION = "2m";
        String GET_INFO = "10m";


        String OPEN_DOWNLOADS = "do";
    }


    public interface  DOWNLOAD_STATUS{
        public static final int COMPLETE = 0;
        public static final int PAUSE = 2;
        public static final int RUNNING = 1;
        public static final int FAILED = 3;
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 992242;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_musical, options);
        } catch (Error | Exception ignored) {
        }

        return bm;
    }

}
