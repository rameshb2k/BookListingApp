package com.example.android.booklistingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Ramesh on 10/26/2016.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        //set the title information for the Book
        String titleString = currentBook.getTitle();
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(titleString);

        //Set the Authors information for the Book
        StringBuilder authorsString = new StringBuilder();
        ArrayList  authorList = currentBook.getAuthors();
        for (int i=0; i<authorList.size(); i++ ) {
            authorsString.append(authorList.get(i) + " ");
        }
        TextView authorsTextView = (TextView) listItemView.findViewById(R.id.authors_text_view);
        authorsTextView.setText(authorsString);


        return listItemView;
    }
}
