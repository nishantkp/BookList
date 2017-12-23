package com.example.android.booklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.book_list_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) listItemView.findViewById(R.id.book_title);
            holder.author = (TextView) listItemView.findViewById(R.id.book_author);
            holder.publishedDate = (TextView) listItemView.findViewById(R.id.book_published_date);
            holder.bookImage = (ImageView) listItemView.findViewById(R.id.book_thumbnail);
            holder.ratingBar = (RatingBar) listItemView.findViewById(R.id.book_rating);
            holder.ratingCount = (TextView) listItemView.findViewById(R.id.book_rating_count);
            holder.bookPrice = (TextView) listItemView.findViewById(R.id.book_price);
            holder.ratingView = listItemView.findViewById(R.id.book_rating_view);
            listItemView.setTag(holder);
        } else {
            // We've just avoided calling findViewByID every time
            // So when the listView is present just use the ViewHolder
            holder = (ViewHolder) listItemView.getTag();
        }

        Book currentBookDetail = getItem(position);

        assert currentBookDetail != null;
        holder.title.setText(currentBookDetail.getBookTitle());

        // Set the text if there's a author of book, otherwise set visibility of textView to GONE
        if (currentBookDetail.getBookAuthor() == null) {
            holder.author.setVisibility(View.GONE);
        } else {
            holder.author.setVisibility(View.VISIBLE);
            holder.author.setText(currentBookDetail.getBookAuthor());
        }

        // Set the text if there is a published date available, otherwise set visibility of textView
        // to GONE
        if (currentBookDetail.getPublishedDate() == null) {
            holder.publishedDate.setVisibility(View.GONE);
        } else {
            holder.publishedDate.setVisibility(View.VISIBLE);
            // Change the format of date and then set it to textView
            holder.publishedDate.setText(formatDate(currentBookDetail.getPublishedDate()));
        }

        // Set the imageView if there is a url available, otherwise set placeholder image
        if (currentBookDetail.getBookThumbnailUrlString() == null) {
            holder.bookImage.setImageResource(R.drawable.book_placeholder);
        } else {
            String url = currentBookDetail.getBookThumbnailUrlString();
            Picasso.with(getContext()).load(url).into(holder.bookImage);
        }

        // Set the textView if there is`book price available, otherwise hide the textView
        if (currentBookDetail.getBookPrice() == null) {
            holder.bookPrice.setVisibility(View.GONE);
        } else {
            holder.bookPrice.setVisibility(View.VISIBLE);
            holder.bookPrice.setText("$" + currentBookDetail.getBookPrice());
        }


        // Set the stars and update the textView if data is available
        // otherwise hide rating bar and textView
        double rating = currentBookDetail.getBookRating();
        if (rating == 0) {
            holder.ratingView.setVisibility(View.GONE);
        } else {
            holder.ratingView.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating((float) rating);
            holder.ratingCount.setText("(" + currentBookDetail.getBookRatingNumber() + ")");
        }
        return listItemView;
    }

    // Change the date format and return string
    // returns the date format
    // i.e  case 4  :   2017
    //      case 7  :   Feb, 2017
    //      case 10 :   12 Feb, 2017
    //      default :   2017*
    @SuppressLint("SimpleDateFormat")
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
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatYearMonth = new SimpleDateFormat("yyyy-MM");
                try {
                    formattedDate = formatYearMonth.parse(date);
                } catch (ParseException e) {
                    Log.e("BookAdapter.this", "Error parsing date format in yyyy-MM", e);
                }
                @SuppressLint("SimpleDateFormat") SimpleDateFormat outDate = new SimpleDateFormat("MMM, yyyy");
                return outDate.format(formattedDate);

            case 10:
                // default length = yyyy-MM-dd
                // 2017-02-23 -> 23 Feb, 2017
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatYearMonthDate = new SimpleDateFormat("yyyy-MM-dd");
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

    /**
     * ViewHolder for list item in res/layout/book_list_item.xml
     * View holder that cashes the views so we do not need to use findViewById every time
     * So scrolling of list view becomes more smooth
     */
    private static class ViewHolder{
        private TextView title;
        private TextView author;
        private TextView bookPrice;
        private TextView publishedDate;
        private ImageView bookImage;
        private RatingBar ratingBar;
        private TextView ratingCount;
        private View ratingView;
    }
}
