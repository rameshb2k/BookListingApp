package com.example.android.booklistingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.R.string.no;

/**
 * Created by Ramesh on 10/26/2016.
 */

public final class BookQueryUtils {


    private String mURL;

    /*
     * Create a private constructor because no one should ever create a {@link BookQueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name BookQueryUtils (and an object instance of BookQueryUtils is not needed).
     */
    private BookQueryUtils()
    {

    }

    //
    public static ArrayList<Book> fetchBookData(String stringUrl) {

        // Create URL object
        URL url = createUrl(stringUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.v("LOG_TAG", "Error with getting JSON response");

        }

        //Extract fields from JSON Response
        return extractBooks(jsonResponse);

    }



    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Book> extractBooks(String jsonResponse) {


        // Create an empty ArrayList that we can start adding books
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of Book objects with the corresponding data.
            JSONObject bookJson = new JSONObject(jsonResponse);
            JSONArray items = bookJson.getJSONArray("items");
            int length = items.length();

            for (int i = 0; i<length; i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");

                ArrayList<String> authorsList = new ArrayList<String>();
                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    int authorslength = authors.length();
                    for (int j = 0; j < authorslength; j++) {
                        authorsList.add(authors.getString(j));
                    }
                }
                books.add(new Book(title, authorsList));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("BookQueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }




    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("LOG_TAG", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.v("Response Code:", String.valueOf(responseCode));
            }

        } catch (IOException e) {
            // TODO: Handle the exception
            Log.v("IOException message: ", e.getMessage());
            Log.v("IOException cause: ", e.getCause().toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
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


} //BookQueryUtils Class
