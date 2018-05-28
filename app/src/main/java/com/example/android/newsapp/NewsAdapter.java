package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News>{
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
// Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        News currentNews = getItem(position);

        // Prepare Data before publishing
        String author = currentNews.getAuthor();
        String newAuthor ="";
        if (author== "null"){

            newAuthor = "by unknown";
        }
        else{
            newAuthor = author;
        }

        String source = currentNews.getSource();
        TextView sourceView =(TextView) listItemView.findViewById(R.id.source_text);
        sourceView.setText(source);

        // Get the original Title string from the News object, and show it in the Textview
        String title = currentNews.getTitle();
        TextView titleView = (TextView) listItemView.findViewById(R.id.title_text);
        titleView.setText(title);
        // Get the original Summary string from the News object, and show it in the Textview
        String summary = currentNews.getSummary();
        TextView summaryView = (TextView) listItemView.findViewById(R.id.text_text);
        summaryView.setText(summary);
        // Get the original Author string from the News object, and show it in the Textview

        TextView authorView = (TextView) listItemView.findViewById(R.id.author_text_view);
        authorView.setText(newAuthor);
        // Get the original Date string from the News object, and show it in the Textview
        String date = currentNews.getDate();
        TextView dateView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateView.setText(date);
        return listItemView;
}}
