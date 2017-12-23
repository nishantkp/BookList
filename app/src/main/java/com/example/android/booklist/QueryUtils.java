package com.example.android.booklist;

import android.graphics.drawable.Drawable;
import android.util.Log;

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
            Log.i(LOG_TAG,"null url passed");
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
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
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
            if (baseJsonObject.has("error")) {
                //Log.e(LOG_TAG, "Json object has error");
                bookList = null;
            }
            if (baseJsonObject.has("items")) {
                //Log.i(LOG_TAG, "has JSON object ; items");
                JSONArray bookArray = baseJsonObject.getJSONArray("items");
                for (int i = 0; i < bookArray.length(); i++) {

                    JSONObject bookObject = bookArray.getJSONObject(i);

                    JSONObject volumeInfo = bookObject.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");

                    String authors;
                    if (volumeInfo.has("authors")) {
                        JSONArray authorsArray = volumeInfo.getJSONArray("authors");
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
                    if (volumeInfo.has("publishedDate")) {
                        publishedDate = volumeInfo.getString("publishedDate");
                    } else {
                        publishedDate = null;
                    }

                    double averageRating;
                    if (volumeInfo.has("averageRating")) {
                        averageRating = volumeInfo.getInt("averageRating");
                    } else {
                        averageRating = 0.0;
                    }

                    String ratingCount;
                    if (volumeInfo.has("ratingsCount")) {
                        ratingCount = volumeInfo.getString("ratingsCount");
                    } else {
                        ratingCount = null;
                    }


                    Drawable drawableThumbnail;
                    String thumbnailUrlString = null;
                    if (volumeInfo.has("imageLinks")) {
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                       thumbnailUrlString = imageLinks.getString("smallThumbnail");
                       //drawableThumbnail = getDrawableObject(thumbnailUrlString);
                    }

                    JSONObject salesInfo = bookObject.getJSONObject("saleInfo");
                    String amount;
                    String currencyCode;
                    if (salesInfo.has("retailPrice")) {
                        JSONObject retailPrice = salesInfo.getJSONObject("retailPrice");
                        amount = retailPrice.getString("amount");
                        currencyCode = retailPrice.getString("currencyCode");
                    } else {
                        amount = null;
                        currencyCode = null;
                    }

                    //Log.i(LOG_TAG, title);
                    //Log.i(LOG_TAG, "\n" + authors);
                    //Log.i(LOG_TAG, "\n" + publishedDate);
                    //Log.i(LOG_TAG, "\n" + amount);
                    //Log.i(LOG_TAG, "\n" + currencyCode);
                    //Log.i(LOG_TAG, "\n" + averageRating);
                    //Log.i(LOG_TAG, "\n" + ratingCount);
                    //Log.i(LOG_TAG, "\n" + drawableThumbnail);
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

    // Gets the drawable object from specific URL
    private static Drawable getDrawableObject(String urlString) {
        URL url = generateUrl(urlString);
        Drawable drawable = null;
        try {
            InputStream inputStream = (InputStream) url.getContent();
            drawable = Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving content from URL: getDrawableObject() block");
        }
        return drawable;
    }
}
