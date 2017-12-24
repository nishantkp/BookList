package com.example.android.booklist.Data;

/**
 * Custom class to store all public keys used in app
 * Created by Nishant on 12/23/2017.
 */

public class PublicKeys {

    public static final String JSON_ERROR_KEY = "error";
    public static final String JSON_ITEMS_KEY = "items";
    public static final String JSON_VOLUME_INFO_KEY = "volumeInfo";
    public static final String JSON_BOOK_TITLE_KEY = "title";
    public static final String JSON_AUTHOR_INFO_KEY = "authors";
    public static final String JSON_PUBLISHED_DATE_KEY = "publishedDate";
    public static final String JSON_AVERAGE_RATING_KEY = "averageRating";
    public static final String JSON_RATING_COUNT_KEY = "ratingsCount";
    public static final String JSON_IMAGE_LINK_KEY = "imageLinks";
    public static final String JSON_SMALL_THUMBNAIL_KEY = "smallThumbnail";
    public static final String JSON_SALES_INFO_KEY = "saleInfo";
    public static final String JSON_RETAIL_PRICE_KEY = "retailPrice";
    public static final String JSON_AMOUNT_KEY = "amount";
    public static final String SEARCH_TYPE_KEY = "search_type";
    public static final String SEARCH_API_INTITLE_KEY = "intitle";
    public static final String SEARCH_API_INAUTOR_KEY = "inauthor";

    private PublicKeys() {
        // Empty constructor, so no one can initialize it
    }
}
