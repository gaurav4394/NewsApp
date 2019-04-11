package com.example.newsapp;


import android.content.Context;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;

import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        CheckInternetConnection internetConnection=new CheckInternetConnection(MainActivity.this);
        context=MainActivity.this;
        FetchNews fetch=new FetchNews(context, recyclerView);
        if(internetConnection.isOnline()){
            fetch.fetchNews();
        }else{
            Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            String cacheObject = PreferenceManager.
                    getDefaultSharedPreferences(this).getString("theJson","");
            fetch.loadView(cacheObject);

        }

    }

    }
