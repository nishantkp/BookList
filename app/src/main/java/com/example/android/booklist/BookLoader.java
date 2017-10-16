package com.example.android.booklist;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;;

import java.util.List;

/**
 * Created by Nishant on 9/6/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;
    private String LOG_TAG = BookLoader.class.getName();


    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"force load");
        forceLoad();
    }


    @Override
    public List<Book> loadInBackground() {

        Log.i(LOG_TAG,"load in back ground");
        if (mUrl == null) {
            Log.i(LOG_TAG,"url null");
            return null;
        }

        List<Book> currentBookData = QueryUtils.fetchBookInformation(mUrl);
        //Log.i(LOG_TAG,"\n load in background"+mCurrentBookData.toString());
        return currentBookData;
    }

}
