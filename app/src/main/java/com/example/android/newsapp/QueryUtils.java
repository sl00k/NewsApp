package com.example.android.newsapp;

import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import static com.example.android.newsapp.MainActivity.LOG_TAG;

public final class QueryUtils  {
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding News to
        List<News> news = new ArrayList<>();
        String tag = "TAGS";
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONArray associated with the key called "response",
            // which represents a list of articles.
            JSONObject newsArray2 = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = newsArray2.getJSONArray("results");


            // For each results in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the value for the key called "sectionName"
                String section = currentNews.getString("sectionName");
                // Extract the value for the key called "title"
                String title = currentNews.getString("webTitle");

                // Extract the value for the key called "fields" to get summary of the news
                JSONObject trailText = currentNews.getJSONObject("fields");

                String description = trailText.getString("trailText");

                // Extract the value for the key called "publishedAt"
                String date = currentNews.getString("webPublicationDate");

                // Extract the value for the key called "url"
                String url = currentNews.getString("webUrl");

                JSONArray tags = currentNews.getJSONArray("tags");
                String authorName = "";
                if (!tags.isNull(0)) {
                    JSONObject currentTag = tags.getJSONObject(0);
                    //Author name
                    authorName = !currentTag.isNull("webTitle") ? currentTag.getString("webTitle") : "";
                }
                String author = authorName;
                // Create a new {@link News} object with the title, summary, date, author, url from the JSON response.
                News newsSum = new News(section, title, description, date, author, url);

                news.add(newsSum);
            }}

         catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        }

        // Return the list of news
        return news;
    }
    /**
     * Query the USGS dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) throws InterruptedException {
        // Create URL object
        Thread.sleep(5000);
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@ling News}s
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return news;
    }
}
/*          SAVING THIS PART FOR NEWS STAGE 2
// Extract the JSONArray associated with the key called "features",
            // which represents a list of articles.
            JSONArray newsArray = baseJsonResponse.getJSONArray("articles");


            // For each articel in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);
                JSONObject sourceNews = currentNews.getJSONObject("source");

                String source = sourceNews.getString("name");

                // Extract the value for the key called "title"
                String title = currentNews.getString("title");

                // Extract the value for the key called "description"
                String description = currentNews.getString("description");

                // Extract the value for the key called "publishedAt"
                String date = currentNews.getString("publishedAt");

                // Extract the value for the key called "url"
                String url = currentNews.getString("url");

                // Extract the value for the key called "author"
                String author = currentNews.getString("author");

                // Create a new {@link News} object with the title, summary, date, author, url from the JSON response.
                News newsSum = new News(source, title, description, date, author, url);
               // News newsSum = new News("eins", "eins", "eins", "eins", "eins");
                // Add the new {@link News} to the list of news.
                news.add(newsSum);
 */