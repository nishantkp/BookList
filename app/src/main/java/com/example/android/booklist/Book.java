package com.example.android.booklist;

/**
 * Created by Nishant on 9/5/2017.
 */

public class Book {

    private String mBookTitle;
    private String mBookAuthor;
    private String mBookPrice;
    private double mBookRating;
    private String mBookThumbnailUrlString;
    private String mBookRatingNumber;
    private String mPublishedDate;

    /**
     * Book Object
     *
     * @param bookTitle              title of the book
     * @param bookAuthor             author of the book
     * @param publishedDate          published date of book
     * @param bookPrice              price of the book
     * @param bookRating             rating for the book
     * @param bookRatingNumber       number of people rated the book
     * @param bookThumbnailUrlString url of book image(front cover)
     */
    public Book(String bookTitle, String bookAuthor, String publishedDate, String bookPrice, double bookRating,
                String bookRatingNumber, String bookThumbnailUrlString) {
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mPublishedDate = publishedDate;
        mBookPrice = bookPrice;
        mBookRating = bookRating;
        mBookRatingNumber = bookRatingNumber;
        mBookThumbnailUrlString = bookThumbnailUrlString;
    }


    /**
     * get the title of the book
     */
    public String getBookTitle() {
        return mBookTitle;
    }

    /**
     * Get the author of the book
     */
    public String getBookAuthor() {
        return mBookAuthor;
    }

    /**
     * Get the published date of book
     */
    public String getPublishedDate() {
        return mPublishedDate;
    }

    /**
     * Get the price of the book
     */
    public String getBookPrice() {
        return mBookPrice;
    }

    /**
     * Get the rating of the book
     */
    public double getBookRating() {
        return mBookRating;
    }

    /**
     * Get number of, how many people provided rating
     */
    public String getBookRatingNumber() {
        return mBookRatingNumber;
    }

    /**
     * Get the url for thumbnail of the book
     */
    public String getBookThumbnailUrlString() {
        return mBookThumbnailUrlString;
    }
}

