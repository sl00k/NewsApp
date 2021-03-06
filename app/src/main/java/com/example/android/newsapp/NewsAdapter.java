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
import java.util.StringTokenizer;

public class NewsAdapter extends ArrayAdapter<News>{
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
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

        String summary = currentNews.getSummary();
        String newSummary = "";
        if (summary == "null") {

            newSummary = "no text available";
        } else {
            newSummary = summary;
        }

        String source = currentNews.getSource();
        TextView sourceView = listItemView.findViewById(R.id.source_text);
        sourceView.setText(source);

        // Get the original Title string from the News object, and show it in the Textview
        String title = currentNews.getTitle();
        TextView titleView = listItemView.findViewById(R.id.title_text);
        titleView.setText(title);
        // Get the original Summary string from the News object, and show it in the Textview

        TextView summaryView = listItemView.findViewById(R.id.text_text);
        summaryView.setText(newSummary);
        // Get the original Author string from the News object, and show it in the Textview

        TextView authorView = listItemView.findViewById(R.id.author_text_view);
        authorView.setText(newAuthor);
        // Get the original Date string from the News object, and show it in the Textview
        String date = currentNews.getDate();

        // Split String to get rid of letters
        StringTokenizer st = new StringTokenizer(date, "T");
        String day = st.nextToken();
        String time = st.nextToken();
        time = time.replace("Z", "");
        date = day + " " + time;

        TextView dateView = listItemView.findViewById(R.id.date_text_view);
        dateView.setText(date);
        return listItemView;
}}
