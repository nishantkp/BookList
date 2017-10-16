package com.example.android.booklist;

import android.graphics.drawable.Drawable;

/**
 * Created by Nishant on 9/5/2017.
 */

public class Book {

    private String mBookTitle;
    private String mBookAuthor;
    private String mBookPrice;
    private double mBookRating;
    private Drawable mBookThumbnail;
    private String mBookPriceCurrencyCode;
    private String mBookRatingNumber;
    private String mPublishedDate;


    /**
     * Book object
     * @param bookTitle             title of the book
     * @param bookAuthor            author of the book
     * @param bookPrice             price of the book
     * @param bookPriceCurrencyCode currency code for the price
     * @param bookRating            rating for the book
     * @param bookThumbnail         image of book(front cover)
     */
    public Book(String bookTitle, String bookAuthor, String bookPrice, String bookPriceCurrencyCode
            , double bookRating, Drawable bookThumbnail){
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mBookPrice = bookPrice;
        mBookPriceCurrencyCode = bookPriceCurrencyCode;
        mBookRating = bookRating;
        mBookThumbnail = bookThumbnail;
    }

    /**
     * Book Object
     * @param bookTitle         title of the book
     * @param bookAuthor        author of the book
     * @param publishedDate     published date of book
     * @param bookPrice         price of the book
     * @param bookRating        rating for the book
     * @param bookRatingNumber  number of people rated the book
     * @param bookThumbnail     image of book(front cover)
     */
    public Book(String bookTitle, String bookAuthor, String publishedDate , String bookPrice, double bookRating,
            String bookRatingNumber , Drawable bookThumbnail){
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mPublishedDate = publishedDate;
        mBookPrice = bookPrice;
        mBookRating = bookRating;
        mBookRatingNumber = bookRatingNumber;
        mBookThumbnail = bookThumbnail;
    }


    /**
     * get the title of the book
     */

    public String getBookTitle(){
        return mBookTitle;
    }

    /**
     * Get the author of the book
     */
    public String getBookAuthor(){
        return mBookAuthor;
    }

    /**
     * Get the published date of book
     */
    public String getPublishedDate(){
        return mPublishedDate;
    }

    /**
     * Get the price of the book
     */
    public String getBookPrice(){
        return mBookPrice;
    }

    /**
     * Get the currency code for price
     */
    public String getBookPriceCurrencyCode(){
        return mBookPriceCurrencyCode;
    }
    /**
     * Get the rating of the book
     */
    public double getBookRating(){
        return mBookRating;
    }

    /**
     * Get number of, how many people provided rating
     */
    public String getBookRatingNumber(){
        return mBookRatingNumber;
    }

    /**
     * Get the thumbnail of the book
     */
    public Drawable getBookThumbnail(){
        return mBookThumbnail;
    }

}

