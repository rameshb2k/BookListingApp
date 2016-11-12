package com.example.android.booklistingapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Ramesh on 10/26/2016.
 */


public class BookLoader extends android.support.v4.content.AsyncTaskLoader<List<Book>> {
    private String mUrl;
    public BookLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }
        return BookQueryUtils.fetchBookData(mUrl);
    }
}