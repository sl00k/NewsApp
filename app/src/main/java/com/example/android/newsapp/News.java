package com.example.android.newsapp;

public class News {

    private String mSource;
    private String mTitle;
    private String mSummary;
    private String mDate;
    private String mAuthor;
    private String mUrl;


    public News(String source,String title, String summary, String date, String author, String url) {
        mSource = source;
        mTitle = title;
        mSummary = summary;
        mDate = date;
        mAuthor = author;
        mUrl = url;
    }

    public String getSource(){
        return mSource;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getSummary(){
        return mSummary;
    }
    public String getDate(){
        return mDate;
    }
    public String getAuthor(){return mAuthor;}
    public String getUrl(){
        return mUrl;
    }
}