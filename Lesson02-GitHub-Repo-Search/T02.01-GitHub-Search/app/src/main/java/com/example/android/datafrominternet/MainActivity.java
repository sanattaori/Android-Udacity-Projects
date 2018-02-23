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
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // TODO DONE(26) Create an EditText variable called mSearchBoxEditText
    private EditText mSearchBoxet;
    private TextView mUrlDisplayTv;
    private TextView mSearchResultTv;
    private TextView erroMsgTv;

    private static final String queryKey = "query";
    private static final String resultKey = "results";


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
            String qResults = savedInstanceState.getString(resultKey);

            mSearchBoxet.setText(qUrl);
            mSearchResultTv.setText(qResults);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String queryUrl = mSearchBoxet.getText().toString();
        outState.putString(queryKey,queryUrl);
        String resultsJson = mSearchResultTv.getText().toString();
        outState.putString(resultKey,resultsJson);
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
    public class GithubAsyncTaskQuery extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                mSearchResultTv.setText(githubSearchResults);
                showJsonDataView();
            }

            else {
                showErrorMessage();
            }
        }
    }

    private void makeGithubSearchQuery() {
        //get text from search box
        String githubQuery = mSearchBoxet.getText().toString();
        //pass to NetworkUtils BuildUrl
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        //set result to display TextView
        mSearchResultTv.setText(String.format("Wait Searching/Requesting url : %s", githubSearchUrl.toString()));
        try {
            new GithubAsyncTaskQuery().execute(githubSearchUrl);
            mSearchResultTv.setText(githubSearchUrl.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
