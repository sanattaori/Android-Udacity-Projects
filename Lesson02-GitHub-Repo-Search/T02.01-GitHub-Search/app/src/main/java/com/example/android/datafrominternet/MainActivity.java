/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.datafrominternet;

import android.annotation.SuppressLint;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>{

    // TODO DONE(26) Create an EditText variable called mSearchBoxEditText
    private EditText mSearchBoxet;
    private TextView mUrlDisplayTv;
    private TextView mSearchResultTv;
    private TextView erroMsgTv;

    private static final String queryKey = "query";
    private static final String resultKey = "results";
    private static final int GITHUB_LOADER_SEARCH = 222;


    // TODO DONE(27) Create a TextView variable called mUrlDisplayTextView
    // TODO DONE(28) Create a TextView variable called mSearchResultsTextView

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO DONE(29) Use findViewById to get a reference to mSearchBoxEditText
        mSearchBoxet =  findViewById(R.id.edit_text_search_github);
        mUrlDisplayTv = findViewById(R.id.tv_url_display);
        mSearchResultTv = findViewById(R.id.tv_search_results);
        erroMsgTv = findViewById(R.id.tv_error_message_display);
        // TODO DONE(30) Use findViewById to get a reference to mUrlDisplayTextView
        // TODO DONE(31) Use findViewById to get a reference to mSearchResultsTextView
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        //store data when destroyed
        if (savedInstanceState != null) {
            String qUrl = savedInstanceState.getString(queryKey);


            mSearchBoxet.setText(qUrl);

        }
        getSupportLoaderManager().initLoader(GITHUB_LOADER_SEARCH,null,this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String queryUrl = mSearchBoxet.getText().toString();
        outState.putString(queryKey,queryUrl);
//        String resultsJson = mSearchResultTv.getText().toString();
//        outState.putString(resultKey,resultsJson);
    }

    //show error
    private void showErrorMessage() {
        mSearchResultTv.setVisibility(View.INVISIBLE);
        erroMsgTv.setVisibility(View.VISIBLE);
    }

    private void showJsonDataView() {
        erroMsgTv.setVisibility(View.INVISIBLE);
        mSearchResultTv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickId = item.getItemId();
        if (clickId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String mGithubJson;
            @Override
            public String loadInBackground() {
                String search_query = args.getString(queryKey);
                if (search_query == null || TextUtils.isEmpty(search_query)) {
                    return null;
                }
                try {
                    URL github_url = new URL(search_query);
                    return NetworkUtils.getResponseFromHttpUrl(github_url);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {

                super.onStartLoading();
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);

                /*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 */
                if (mGithubJson != null) {
                    deliverResult(mGithubJson);
                } else {

                    forceLoad();
                }
            }
            //Override deliverResult and store the data in mGithubJson
            @Override
            public void deliverResult(String githubJson) {
                mGithubJson = githubJson;
                super.deliverResult(githubJson);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")) {
            mSearchResultTv.setText(data);
            showJsonDataView();
        }

        else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

//    @SuppressLint("StaticFieldLeak")
//    public class GithubAsyncTaskQuery extends AsyncTask<URL, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(URL... params) {
//            URL searchUrl = params[0];
//            String githubSearchResults = null;
//            try {
//                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return githubSearchResults;
//        }
//
//        @Override
//        protected void onPostExecute(String githubSearchResults) {
//
//        }
//    }

    @SuppressLint("SetTextI18n")
    private void makeGithubSearchQuery() {
        //get text from search box
        String githubQuery = mSearchBoxet.getText().toString();

        if (TextUtils.isEmpty(githubQuery)) {
            mUrlDisplayTv.setText("No query entered, nothing to search for.");
            return;
        }

        //pass to NetworkUtils BuildUrl
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);

        mSearchResultTv.setText(String.format("Wait Searching/Requesting url : %s", githubSearchUrl.toString()));
        try {
            //new GithubAsyncTaskQuery().execute(githubSearchUrl);
            mSearchResultTv.setText(githubSearchUrl.toString());

            Bundle queryBundle = new Bundle();
            queryBundle.putString(queryKey, githubSearchUrl.toString());

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> githubSearchLoader = loaderManager.getLoader(GITHUB_LOADER_SEARCH);

            if (githubSearchLoader  == null) {
                loaderManager.initLoader(GITHUB_LOADER_SEARCH,queryBundle,this);
            } else {
                loaderManager.restartLoader(GITHUB_LOADER_SEARCH,queryBundle,this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
