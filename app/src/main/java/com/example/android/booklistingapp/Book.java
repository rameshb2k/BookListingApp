package com.example.android.booklistingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Ramesh on 10/26/2016.
 */

public class Book implements Parcelable {

    private String mTitle;
    private ArrayList<String> mAuthors;

    public Book (String title, ArrayList<String> authors) {
        this.mTitle = title;
        this.mAuthors = authors;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public String getTitle() {
        return mTitle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeStringList(mAuthors);
    }
    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private Book(Parcel in) {
        mTitle = in.readString();
        mAuthors = (ArrayList<String>)in.readSerializable();
    }


}
