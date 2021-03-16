package com.example.browser.utils;

import com.example.browser.model.DownloadModel;

public interface ItemClickListener {
    void onCLickItem(String file_path);
    void onShareClick(DownloadModel downloadModel);
}
