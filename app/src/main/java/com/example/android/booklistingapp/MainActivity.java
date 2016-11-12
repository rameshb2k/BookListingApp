package com.example.android.booklistingapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    //Test URL for book data for google book api
    private static final String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=data&maxResults=10";
    StringBuilder urlString = new StringBuilder();
    private BookAdapter bookAdapter;
    private ArrayList<Book> savedBooks = new ArrayList<Book>();

    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        //  Create a new loader for the given URL
        return new BookLoader(MainActivity.this, urlString.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        // Clear the adapter of previous book data
        bookAdapter.clear();
        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            bookAdapter.addAll(books);
            savedBooks = (ArrayList)books;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        //  Loader reset, so we can clear out our existing data.
        getSupportLoaderManager().restartLoader(1, null, this);
    }

    //Save books data when activity is destroyed
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //save books
        savedInstanceState.putParcelableArrayList("key", savedBooks);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check for Internet Connection
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setEmptyView(findViewById(R.id.empty_view));
        final TextView emptyView = (TextView) findViewById(R.id.empty_view);

        //grab the search term when the search button is clicked
        final Button searchButton = (Button) findViewById(R.id.search_button_view);

        //Restore Contents of Adapter if Instance is saved before Activity is destroyed
        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            Log.v("savedInstanceState", " false");
            // Create a new {@link ArrayAdapter} of books
            bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        } else {
            Log.v("savedInstanceState", "true");
            savedBooks = savedInstanceState.getParcelableArrayList("key");
            bookAdapter = new BookAdapter(this, savedBooks);
        }

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(bookAdapter);

            //set a click listener on Button View
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnected) {
                        EditText searchText = (EditText) findViewById(R.id.search_text_view);
                        String ed_text = searchText.getText().toString().trim();
                        if (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null) {
                            Log.v("MainActivity", "searchButton: EMPTY");
                            bookAdapter.clear();
                            emptyView.setText("Please Enter a Search Term");
                        } else {
                            Log.v("MainActivity", "searchButton: NOT EMPTY");
                            String searchTerm = searchText.getText().toString().trim().replaceAll(" +", "+");

                            urlString.setLength(0);
                            urlString.append("https://www.googleapis.com/books/v1/volumes?q=" + searchTerm + "&maxResults=10");
                            Log.v("NOT EMPTY", urlString.toString());
                            getSupportLoaderManager().restartLoader(1, null, MainActivity.this);
                        }
                    } else {
                        emptyView.setText("No Internet Connection");
                    }
                }
            });


    }//onCreate

}//MainActivity Class