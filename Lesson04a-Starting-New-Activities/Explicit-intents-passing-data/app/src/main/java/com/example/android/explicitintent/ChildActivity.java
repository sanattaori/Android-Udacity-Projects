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
package com.example.android.explicitintent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChildActivity extends AppCompatActivity {

    /* Field to store our TextView */
    private TextView mDisplayText;

    private Button site;
    private Button map;
    private Button call;
    private EditText loc;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_child);

        /* Typical usage of findViewById... */
        mDisplayText = (TextView) findViewById(R.id.tv_display);

        // TODO (3) Use the getIntent method to store the Intent that started this Activity in a variable
        Intent startActivity = getIntent();
        // TODO (4) Create an if statement to check if this Intent has the extra we passed from MainActivity
        //if has data
        if (startActivity.hasExtra(Intent.EXTRA_TEXT)){
            String text = startActivity.getStringExtra(Intent.EXTRA_TEXT);
            mDisplayText.setText(text);
        }

        site = findViewById(R.id.site);
        map = findViewById(R.id.map);

        call = findViewById(R.id.call);
        loc = findViewById(R.id.loc);

        site.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });


        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String page = "http://sanattaori.me";
                Log.d("page", "onClick: "+page);
                openWebPage(page);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loc.setText("");
                String address = loc.getText().toString();

                openmap(address);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loc.setText("");
                String phoneNumber = loc.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }
    private void openmap(String location){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .query(location);

        Uri addressUri = builder.build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(addressUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    
     private void openWebPage(String page){
        Uri web_page = Uri.parse(page);
        Intent intent = new Intent(Intent.ACTION_VIEW, web_page);
         if (intent.resolveActivity(getPackageManager()) != null) {
             startActivity(intent);
         }
    }
}