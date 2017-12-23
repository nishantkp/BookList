package com.example.android.booklist;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    final static int BOOK_LOADER_ID = 1;

    final static String GOOGLE_BOOKS_QUERY_PART_ONE = "https://www.googleapis.com/books/v1/volumes?q=";
    final static String GOOGLE_BOOKS_QUERY_PART_TWO = "&langRestrict=en&maxResults=20&printType=books";
    public static final String SEARCH_TYPE = "SEARCH_TYPE";
    //final static String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=20";
    private ProgressBar mProgressBar;
    private TextView mEmptyTextView;
    private BookAdapter mBookAdapter;
    private ListView mListVIew;
    private boolean mSearchByAuthor;
    private boolean mSearchByTitle;
    private String mSearchByType;

    private String mNewGoogleBooksApiUrl;

    /* EditText view for user input query */
    private EditText mUserInputQuery;

    private final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        mUserInputQuery = (EditText) findViewById(R.id.user_query);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // find the listView with id list
        mListVIew = (ListView) findViewById(R.id.list);
        // Set empty view on listView to display "check network connection" or "no result found."
        mListVIew.setEmptyView(mEmptyTextView);

        mBookAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());

        mListVIew.setAdapter(mBookAdapter);

        // Initialize the loader after making sure that there is a valid network connection
        initLoaderAfterCheckingNetwork();

        // Get the query from user after hitting enter
        mUserInputQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Get the text from user and initialize loader with new content
                getTextAndInitLoader();
                // Hide the keyboard after hitting enter
                return false;
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "on Create loader method");

        // Get the shared preference object
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the shared preference value of ID search_book_by_author
        mSearchByAuthor = sharedPreferences.getBoolean(
                getString(R.string.search_book_by_author_key), false);

        // Get the shared preference value of ID search_book_by_title
        mSearchByTitle = sharedPreferences.getBoolean(
                getString(R.string.search_book_by_title_key), true);

        // Get the shared preference value of ID search_by_type
        mSearchByType = sharedPreferences.getString(
                getString(R.string.search_by_type_key),
                getString(R.string.search_by_type_default));

        // Find the editText with ID user_query in order to change Hint text
        // depending upon selected preferences
        EditText editText = (EditText) findViewById(R.id.user_query);

        if (mSearchByAuthor && mSearchByTitle) {
            editText.setHint("Search : title by author");
        } else if (mSearchByAuthor) {
            editText.setHint("Search : author");
        } else if (mSearchByTitle) {
            editText.setHint("Search : title");
        } else {
            editText.setHint("Search");
        }


        return new BookLoader(this, mNewGoogleBooksApiUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {

        mBookAdapter.clear();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        // find the TextView with id empty_view and set text to "No result found!"
        mEmptyTextView.setText(R.string.no_result);

        if (data == null) {
            Log.i(LOG_TAG, "null data");
        }

        if (data != null && !data.isEmpty()) {
            Log.i(LOG_TAG, "there is some data");
            mBookAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookAdapter.clear();
    }


    // Initialize the loader after making sure there is a network connection
    // in order to prevent app from crashing
    private void initLoaderAfterCheckingNetwork() {
        // Get a reference to connectivity manager to check the state of network
        ConnectivityManager connectivityManager =
                (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get the details on currently active network
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();

        // if there is a network connection
        if (isConnected) {
            // set the progressbar visibility to VISIBLE and Initialize loader
            mProgressBar.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(BOOK_LOADER_ID, null, MainActivity.this);
        } else {
            // First hide the progressbar so that the error message will be visible to user
            mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
            mProgressBar.setVisibility(View.GONE);

            // set visibility of textView to VISIBLE and update empty state
            // with with no network connection error message
            mEmptyTextView.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(R.string.no_network);
        }
    }

    private String getCorrectApiUrlFromUserQuery(String userQuery) {
        // If input query is empty, means user hit the FIND button without typing anything
        // return null url string
        if (userQuery.isEmpty()) {
            return null;
        }

        // Replace all the blank spaces with '+' and concatenate string to form correct url
        String formattedQuery = userQuery.replaceAll(" ", "+");

        // If preferences for Title and Author is selected
        if (mSearchByTitle && mSearchByAuthor) {
            // If user query contains " by " then split the string
            // otherwise return default url string
            // https://www.googleapis.com/books/v1/volumes?q=intitle:flower+inauthor:max&maxResults=20&printType=books
            if (userQuery.contains(" by ")) {
                String[] parts = userQuery.split(" by ");
                String bookTitle = parts[0].replaceAll(" ", "+");
                String bookAuthor = parts[1].replaceAll(" ", "+");
                Log.i(LOG_TAG, "title : " + bookTitle);
                Log.i(LOG_TAG, "author : " + bookAuthor);

                switch (mSearchByType) {
                    case "free":
                        // If Free book option is selected
                        return GOOGLE_BOOKS_QUERY_PART_ONE
                                + "intitle:" + bookTitle + "+inauthor:" + bookAuthor
                                + GOOGLE_BOOKS_QUERY_PART_TWO;
                    case "paid":
                        // If paid book option is selected
                        return GOOGLE_BOOKS_QUERY_PART_ONE
                                + "intitle:" + bookTitle + "+inauthor:" + bookAuthor
                                + GOOGLE_BOOKS_QUERY_PART_TWO;
                    default:
                        // If none of the option is selected generate the url string with only books filter
                        return GOOGLE_BOOKS_QUERY_PART_ONE
                                + "intitle:" + bookTitle + "+inauthor:" + bookAuthor
                                + GOOGLE_BOOKS_QUERY_PART_TWO;
                }
            } else {
                // If user didn't separated title and author with " by "
                // return url string without adding author and title information
                // https://www.googleapis.com/books/v1/volumes?q=flower&maxResults=20&printType=books
                return GOOGLE_BOOKS_QUERY_PART_ONE
                        + formattedQuery + GOOGLE_BOOKS_QUERY_PART_TWO;
            }

        } else if (mSearchByTitle) {
            // If preference for only Title is selected
            // https://www.googleapis.com/books/v1/volumes?q=intitle:flower&maxResults=20&printType=books
            switch (mSearchByType) {
                case "free":
                    // if user has selected Free books option
                    return GOOGLE_BOOKS_QUERY_PART_ONE
                            + "intitle:" + formattedQuery
                            + GOOGLE_BOOKS_QUERY_PART_TWO;
                case "paid":
                    // if user has selected Paid books option
                    return GOOGLE_BOOKS_QUERY_PART_ONE
                            + "intitle:" + formattedQuery
                            + GOOGLE_BOOKS_QUERY_PART_TWO;
                default:
                    // if user has selected one of the options, generate url option with only books filter
                    return GOOGLE_BOOKS_QUERY_PART_ONE
                            + "intitle:" + formattedQuery
                            + GOOGLE_BOOKS_QUERY_PART_TWO;
            }

        } else if (mSearchByAuthor) {
            // If preference for only Author is selected
            // https://www.googleapis.com/books/v1/volumes?q=inauthor:max&maxResults=20&printType=books
            switch (mSearchByType) {
                case "free":
                    // If user has selected Free books option
                    return GOOGLE_BOOKS_QUERY_PART_ONE
                            + "inauthor:" + formattedQuery
                            + GOOGLE_BOOKS_QUERY_PART_TWO;
                case "paid":
                    // If user has selected Paid books option
                    return GOOGLE_BOOKS_QUERY_PART_ONE
                            + "inauthor:" + formattedQuery
                            + GOOGLE_BOOKS_QUERY_PART_TWO;
                default:
                    // If user has selected none of the option, generate url option with only books filter
                    return GOOGLE_BOOKS_QUERY_PART_ONE
                            + "inauthor:" + formattedQuery
                            + GOOGLE_BOOKS_QUERY_PART_TWO;
            }
        } else if (mSearchByType.equals("free")) {
            // If user has selected only Free books options
            return GOOGLE_BOOKS_QUERY_PART_ONE
                    + formattedQuery + GOOGLE_BOOKS_QUERY_PART_TWO;

        } else if (mSearchByType.equals("paid")) {
            // If user has selected only Paid books options
            return GOOGLE_BOOKS_QUERY_PART_ONE
                    + formattedQuery + GOOGLE_BOOKS_QUERY_PART_TWO;

        } else {
            // If user has only selected None option
            // https://www.googleapis.com/books/v1/volumes?q=flower&maxResults=20&printType=books
            return GOOGLE_BOOKS_QUERY_PART_ONE
                    + formattedQuery + GOOGLE_BOOKS_QUERY_PART_TWO;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu with xml resource main.xml
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actions_setting) {
            // If id is actions_setting then create a new Intent
            // to start new activity - SettingsActivity.class
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Get the query from user and initialize new loader with new content
    private void getTextAndInitLoader(){
        // Get the query requested by user from the EditText with id user_query
        String userQuery = mUserInputQuery.getText().toString().trim();

        // Concatenate user query with GOOGLE_BOOK_QUERY to generate correct request
        mNewGoogleBooksApiUrl = getCorrectApiUrlFromUserQuery(userQuery);

        Log.i(LOG_TAG, "url string : " + mNewGoogleBooksApiUrl);

        // Set empty adapter on list view in order to reset the list view
        mBookAdapter.clear();
        mListVIew.setAdapter(mBookAdapter);

        // Destroy the loader with id BOOK_LOADER_ID in order to create a new loader with
        // new content from query requested by user
        // and then hide the empty textView to not
        // overlap progressbar and empty text view
        getLoaderManager().destroyLoader(BOOK_LOADER_ID);

        // Set visibility of Empty textView to GONE and display progressbar
        mEmptyTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        // Initialize the loader after making sure that there is a valid network connection
        initLoaderAfterCheckingNetwork();
        // forceLoad the BookLoader with new search query requested by user
        new BookLoader(MainActivity.this, mNewGoogleBooksApiUrl).forceLoad();
    }

    // Called to retrieve pre-instance state from an activity before being killed so that state can
    // be restored on onRestoreInstanceState
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_TYPE, mSearchByType);
        super.onSaveInstanceState(outState);
    }

    // Restore the state of variable mSearchBtType when activity is recreated
    // i.e switching to landscape mode
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSearchByType = savedInstanceState.getString(SEARCH_TYPE);
    }
}