package com.example.android.booklist.Data;

/**
 * Custom class containing all constants through out the app
 * Created by Nishant on 12/23/2017.
 */

public class Constants {

    public static final String URL_CONNECTION_REQUEST_METHOD = "GET";
    public static final int URL_CONNECTION_READ_TIME_OUT = 10000;
    public static final int URL_CONNECTION_CONNECT_TIME_OUT = 15000;
    public static final int URL_CONNECTION_SUCCESS_RESPONSE_CODE = 200;
    public static final String GOOGLE_BOOKS_QUERY_PART_ONE = "https://www.googleapis.com/books/v1/volumes?q=";
    public static final String GOOGLE_BOOKS_QUERY_PART_TWO = "&langRestrict=en&maxResults=20&printType=books";
    public static final String SEPARATOR = " by ";
    public static final String SEARCH_TYPE_FREE = "free";
    public static final String SEARCH_TYPE_PAID = "paid";

    private Constants() {
        // Empty constructor so no one can initialize it
    }
}
