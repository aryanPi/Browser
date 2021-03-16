package com.example.browser.activities;

public class constants {
    public static final String FB_JS = "javascript:function updateVideo(u) {" +
            "var ve = document.getElementsByTagName('video')[0];" +
            "if(ve){" +
            "if(!(JSON.parse(ve.parentElement.dataset.store)['src'] == u))" +
            "{" +
            "ve.onplay=function(){Android.playD();Android.fun1(JSON.parse(ve.parentElement.dataset.store)['src']);};" +
            "ve.onpause=function(){Android.pauseD();};" +
            "" +
            "Android.fun1(JSON.parse(ve.parentElement.dataset.store)['src']);}" +
            "}}";


    public static final String INSTA_JS = "javascript:function updateVideo(){" +
            "var ve = document.getElementsByTagName('video');" +
            "for(var i=0;i< ve.length;i++){" +
            "if(ve[i]!=undefined){" +
            "ve[i].onpause = function (){Android.pauseD();};" +
            "ve[i].onplay = function (){" +
            "var tempVideos = document.getElementsByTagName('video');" +
            "for(var j=0;j< tempVideos.length;j++){" +
            "if(tempVideos[j]!=undefined){" +
            "if(!tempVideos[j].paused){if(tempVideos[j].src!=''){Android.fun1(tempVideos[j].src);Android.playD();}" +
            "else if(tempVideos[j].firstElementChild.src!='')" +
            "{Android.fun1(tempVideos[j].firstElementChild.src);" +
            "Android.playD();}break;}}" +
            "}" +
            "}" +
            "}}" +
            "}";



    public static final String PDF_IMAGE = "draw_pdf";
    public static final String IMG_IMAGE = "draw_img";
    public static final String ZIP_IMAGE = "draw_zip";
    public static final String MUSICAL_IMAGE = "draw_music";
    public static final String FILE_IMAGE = "draw_file";



    /*
    *
    * "var ve = document.getElementsByTagName('video');" +
            "for(var i=0;i< ve.length;i++){" +
            "if(ve[i]!=undefined){" +
            "ve[i].onpause = function (){console.log('pause');console.log(ve[i].src);}" +
            "ve[i].onplay = function (){console.log('play');console.log(ve[i].src);}" +
            "}}" +
    * */

    public static final String SAVE_FOLDER_NAME = "/.BROWSER Downloaded/";
    public static final String WA_FOLDER_NAME = "/WhatsApp/";

    public static final String googleQuery = "https://www.google.com/search?q=";
    public static final String yahooQuery = "https://in.search.yahoo.com/search?p=";
    public static final String aloQuery = "";
    public static final String askQuery = "https://www.ask.com/web?o=0&l=dir&qo=homepageSearchBox&q=";
    public static final String NaverQuery = "https://m.search.naver.com/search.naver?where=m&sm=top_hty&fbm=0&ie=utf8&query=";
    public static final String yandexQuery = "https://yandex.com/search/?text=";
    public static final String duckduckgoQuery = "https://duckduckgo.com/?q=";
    public static final String bingQuery = "https://www.bing.com/search?q=";


    public static final String NEWS_API_KEY = "aa2e8da8152645169bd0f306495aa715";
    public static final String NEWS_SOURCE = "techcrunch";

}
