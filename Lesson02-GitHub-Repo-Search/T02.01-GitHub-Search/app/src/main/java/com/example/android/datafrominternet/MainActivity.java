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

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // TODO DONE(26) Create an EditText variable called mSearchBoxEditText
    EditText mSearchBoxet;
    TextView mUrlDisplayTv;
    TextView mSearchResultTv;
    // TODO DONE(27) Create a TextView variable called mUrlDisplayTextView
    // TODO DONE(28) Create a TextView variable called mSearchResultsTextView



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO DONE(29) Use findViewById to get a reference to mSearchBoxEditText
        mSearchBoxet =  findViewById(R.id.edit_text_search_github);
        mUrlDisplayTv = findViewById(R.id.tv_url_display);
        mSearchResultTv = findViewById(R.id.tv_search_results);
        // TODO DONE(30) Use findViewById to get a reference to mUrlDisplayTextView
        // TODO DONE(31) Use findViewById to get a reference to mSearchResultsTextView
    }
    //
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

    private void makeGithubSearchQuery() {
        //get text from search box
        String githubQuery = mSearchBoxet.getText().toString();
        //pass to NetworkUtils BuildUrl
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        //set result to display TextView
        mSearchResultTv.setText(String.format("Requesting url : %s", githubSearchUrl.toString()));
        try {
            String GitHubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubSearchUrl);
            mSearchResultTv.setText(GitHubSearchResults);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
