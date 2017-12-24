package com.example.android.booklist;

import android.util.Log;

import com.example.android.booklist.Data.Constants;
import com.example.android.booklist.Data.PublicKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nishant on 9/5/2017.
 */

public class QueryUtils {

    private static String LOG_TAG = QueryUtils.class.getName();

    // Empty constructor
    public QueryUtils() {
    }

    public static List<Book> fetchBookInformation(String requestedQuery) {
        String JSONResponse = null;
        URL url;

        // If requested query string is null, return null object
        if (requestedQuery == null) {
            Log.i(LOG_TAG, "null url passed");
            return null;
        } else {
            // Generate URL object
            url = generateUrl(requestedQuery);
        }

        Log.i(LOG_TAG, "URL object created");
        try {
            JSONResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem closing input stream : fetchBookInformation() block gets from makeHttpRequest() block try closing inputStream.close()");
        }

        // If JSON response is not equal to null then extract the features of response
        // in order to prevent app from crashing if received response is null
        if (JSONResponse != null) {
            List<Book> booksList = extractFeaturesOfJSON(JSONResponse);
            return booksList;
        } else {
            // Otherwise return null object
            return null;
        }
    }

    /**
     * Generate url object from URL string
     *
     * @param urlString url in string format
     * @return URL object
     */
    private static URL generateUrl(String urlString) {
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating url: generateUrl() block", e);
        }
        return url;
    }

    /**
     * Takes URL of requested query and returns String JSON response
     *
     * @param url URL
     * @return String JSON response
     * @throws IOException for closing input Stream
     */
    private static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String JSONResponse = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(Constants.URL_CONNECTION_REQUEST_METHOD);
            urlConnection.setReadTimeout(Constants.URL_CONNECTION_READ_TIME_OUT);
            urlConnection.setConnectTimeout(Constants.URL_CONNECTION_CONNECT_TIME_OUT);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == Constants.URL_CONNECTION_SUCCESS_RESPONSE_CODE) {
                Log.i(LOG_TAG, "Success Response Code : 200");
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            } else {
                Log.i(LOG_TAG, "Response code : " + urlConnection.getResponseCode());
                // If received any other response(i.e 400) code return null JSON response
                JSONResponse = null;
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error solving JSON response : makeHttpRequest() block");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing an inputStream can throw IOException, which why makeHttpRequest
                // method signature specifies, throws IOException
                inputStream.close();
            }
        }

        return JSONResponse;
    }

    /**
     * Convert InputStream into stream which contains whole JSON response
     *
     * @param inputStream InputStream which contains whole JSON response
     * @return String of JSON response
     */
    private static String readFromStream(InputStream inputStream) {
        StringBuilder outputString = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    outputString.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error creating JSON response: readFromStream() block");
            }
        }
        return outputString.toString();
    }

    public static List<Book> extractFeaturesOfJSON(String JSONResponse) {

        List<Book> bookList = new ArrayList<Book>();

        try {
            JSONObject baseJsonObject = new JSONObject(JSONResponse);

            // If JSON response has error object, return null List<Book> object
            if (baseJsonObject.has(PublicKeys.JSON_ERROR_KEY)) {
                //Log.e(LOG_TAG, "Json object has error");
                bookList = null;
            }
            if (baseJsonObject.has(PublicKeys.JSON_ITEMS_KEY)) {
                //Log.i(LOG_TAG, "has JSON object ; items");
                JSONArray bookArray = baseJsonObject.getJSONArray(PublicKeys.JSON_ITEMS_KEY);
                for (int i = 0; i < bookArray.length(); i++) {

                    JSONObject bookObject = bookArray.getJSONObject(i);

                    JSONObject volumeInfo = bookObject.getJSONObject(PublicKeys.JSON_VOLUME_INFO_KEY);
                    String title = volumeInfo.getString(PublicKeys.JSON_BOOK_TITLE_KEY);

                    String authors;
                    if (volumeInfo.has(PublicKeys.JSON_AUTHOR_INFO_KEY)) {
                        JSONArray authorsArray = volumeInfo.getJSONArray(PublicKeys.JSON_AUTHOR_INFO_KEY);
                        StringBuilder authorsStringBuilder = new StringBuilder();
                        authorsStringBuilder.append(authorsArray.getString(0));
                        for (int j = 1; j < authorsArray.length(); j++) {
                            authorsStringBuilder.append(", ");
                            authorsStringBuilder.append(authorsArray.getString(j));
                        }
                        authors = authorsStringBuilder.toString();
                    } else {
                        authors = null;
                    }

                    String publishedDate;
                    if (volumeInfo.has(PublicKeys.JSON_PUBLISHED_DATE_KEY)) {
                        publishedDate = volumeInfo.getString(PublicKeys.JSON_PUBLISHED_DATE_KEY);
                    } else {
                        publishedDate = null;
                    }

                    double averageRating;
                    if (volumeInfo.has(PublicKeys.JSON_AVERAGE_RATING_KEY)) {
                        averageRating = volumeInfo.getInt(PublicKeys.JSON_AVERAGE_RATING_KEY);
                    } else {
                        averageRating = 0.0;
                    }

                    String ratingCount;
                    if (volumeInfo.has(PublicKeys.JSON_RATING_COUNT_KEY)) {
                        ratingCount = volumeInfo.getString(PublicKeys.JSON_RATING_COUNT_KEY);
                    } else {
                        ratingCount = null;
                    }

                    String thumbnailUrlString = null;
                    if (volumeInfo.has(PublicKeys.JSON_IMAGE_LINK_KEY)) {
                        JSONObject imageLinks = volumeInfo.getJSONObject(PublicKeys.JSON_IMAGE_LINK_KEY);
                        thumbnailUrlString = imageLinks.getString(PublicKeys.JSON_SMALL_THUMBNAIL_KEY);
                    }

                    JSONObject salesInfo = bookObject.getJSONObject(PublicKeys.JSON_SALES_INFO_KEY);
                    String amount;
                    if (salesInfo.has(PublicKeys.JSON_RETAIL_PRICE_KEY)) {
                        JSONObject retailPrice = salesInfo.getJSONObject(PublicKeys.JSON_RETAIL_PRICE_KEY);
                        amount = retailPrice.getString(PublicKeys.JSON_AMOUNT_KEY);
                    } else {
                        amount = null;
                    }

                    assert bookList != null;
                    bookList.add(new Book(title, authors, publishedDate, amount, averageRating,
                            ratingCount, thumbnailUrlString));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem generating JSON object : extractFeaturesOfJSON() block", e);
        }
        return bookList;
    }
}
