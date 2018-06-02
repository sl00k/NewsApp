package com.example.android.newsapp;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String BASE_URL = "https://content.guardianapis.com/search?show-fields=trailText&show-tags=contributor&api-key=";
    String apiKey = BuildConfig.THE_GUARDIAN_API_KEY;
    String REQUEST_URL = BASE_URL + apiKey;
    //public static final String REQUEST_URL = "https://newsapi.org/v2/top-headlines?country=de&category=business&apiKey=5f99e4501411422a877e52d2cb8aa22e";
    private static final int News_Loader_ID = 1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyStateTextView;

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {


        mEmptyStateTextView = findViewById(R.id.empty_view);
        // falls keine daten vorhanden sind
        ListView newsListView = findViewById(R.id.list);
        newsListView.setEmptyView(mEmptyStateTextView);
        return new NewsLoader(this, REQUEST_URL);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mAdapter.clear();
        SwipeRefreshLayout swipeContainer = findViewById(R.id.refresh);

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);

            swipeContainer.setRefreshing(false);
            ProgressBar progressBar;
            progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
        } else {
            // Get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            // Get details on the currently active default data network
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {


                mEmptyStateTextView.setText(R.string.no_json_results);
            } else {
                // Otherwise, display error

                mEmptyStateTextView = findViewById(R.id.empty_view);
                // Update empty state with no connection error message
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            }


        }
        // hide loading indicator so error message will be visible
        View loadingIndicator = findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(View.GONE);
        swipeContainer.setRefreshing(false);
        ProgressBar progressBar;
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    private void testConnection() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(News_Loader_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView = findViewById(R.id.empty_view);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView newsListView = findViewById(R.id.list);


        testConnection();

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
        mSwipeRefreshLayout = findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                testConnection();
                getLoaderManager().restartLoader(News_Loader_ID, null, MainActivity.this);
            }
        });

    }
}
