package com.example.browser.functions;

import android.content.Context;

public class Singleton {

    private static Singleton mInstance = null;

    private String mString;
    Context context;


    public static Singleton getInstance() {

        if (mInstance == null) {
            mInstance = new Singleton();
        }
        return mInstance;
    }

}