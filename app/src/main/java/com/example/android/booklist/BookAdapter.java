package com.example.android.booklist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Nishant on 9/5/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> bookList) {
        super(context, 0, bookList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.book_list_item, parent, false);
        }

        Book currentBookDetail = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.book_title);
        title.setText(currentBookDetail.getBookTitle());

        // Find the textView with ID book_author
        // Set the text if there's a author of book, otherwise set visibility of textView to GONE
        TextView author = (TextView) listItemView.findViewById(R.id.book_author);
        if (currentBookDetail.getBookAuthor() == null) {
            author.setVisibility(View.GONE);
        } else {
            author.setVisibility(View.VISIBLE);
            author.setText(currentBookDetail.getBookAuthor());
        }

        // Find the textView with ID book_published_date
        // Set the text if there is a published date available, otherwise set visibility of textView
        // to GONE
        TextView publishedDate = (TextView) listItemView.findViewById(R.id.book_published_date);
        if (currentBookDetail.getPublishedDate() == null) {
            publishedDate.setVisibility(View.GONE);
        } else {
            publishedDate.setVisibility(View.VISIBLE);
            // Change the format of date and then set it to textView
            publishedDate.setText(formatDate(currentBookDetail.getPublishedDate()));
        }

        // FInd the imageVIew with ID book_thumbnail
        // Set the imageView if there is a drawable available, otherwise set placeholder image
        ImageView bookImage = (ImageView) listItemView.findViewById(R.id.book_thumbnail);
        if (currentBookDetail.getBookThumbnail() == null) {
            bookImage.setImageResource(R.drawable.book_placeholder);
        } else {
            bookImage.setImageDrawable(currentBookDetail.getBookThumbnail());
        }

        // Find the text view with ID book_price
        // Set the textView if there is`book price available, otherwise hide the textView
        TextView bookPrice = (TextView) listItemView.findViewById(R.id.book_price);
        if (currentBookDetail.getBookPrice() == null) {
            bookPrice.setVisibility(View.GONE);
        } else {
            bookPrice.setVisibility(View.VISIBLE);
            bookPrice.setText('$' + currentBookDetail.getBookPrice());
        }


        // Find the ratingBar with ID book_rating and book_rating_count
        // Set the stars and update the textView if data is available
        // otherwise hide rating bar and textView
        RatingBar ratingBar = (RatingBar) listItemView.findViewById(R.id.book_rating);
        TextView ratingCount = (TextView) listItemView.findViewById(R.id.book_rating_count);
        // View is a linear layout with rating bar and text view as child view
        View ratingView = (View) listItemView.findViewById(R.id.book_rating_view);
        double rating = currentBookDetail.getBookRating();
        if (rating == 0) {
            ratingView.setVisibility(View.GONE);
        } else {
            ratingView.setVisibility(View.VISIBLE);
            ratingBar.setRating((float) rating);
            ratingCount.setText('(' + currentBookDetail.getBookRatingNumber() + ')');
        }
        return listItemView;
    }

    // Change the date format and return string
    // returns the date format
    // i.e  case 4  :   2017
    //      case 7  :   Feb, 2017
    //      case 10 :   12 Feb, 2017
    //      default :   2017*
    private String formatDate(String date) {
        Date formattedDate = null;
        switch (date.length()) {

            case 4:
                // date length = yyyy format
                // return the same string because it's only year
                return date;
            case 7:
                //date length = yyyy-MM format
                // 2017-09 -> Sept, 2017
                SimpleDateFormat formatYearMonth = new SimpleDateFormat("yyyy-MM");
                try {
                    formattedDate = formatYearMonth.parse(date);
                } catch (ParseException e) {
                    Log.e("BookAdapter.this", "Error parsing date format in yyyy-MM", e);
                }
                SimpleDateFormat outDate = new SimpleDateFormat("MMM, yyyy");
                return outDate.format(formattedDate);

            case 10:
                // default length = yyyy-MM-dd
                // 2017-02-23 -> 23 Feb, 2017
                SimpleDateFormat formatYearMonthDate = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    formattedDate = formatYearMonthDate.parse(date);
                } catch (ParseException e) {
                    Log.e("BookAdapter.this", "Error parsing date format in yyyy-MM-dd", e);
                }
                outDate = new SimpleDateFormat("dd MMM, yyyy");
                return outDate.format(formattedDate);

            default:
                // Any other format
                // return the same string because it's only year
                return date;
        }
    }
}
