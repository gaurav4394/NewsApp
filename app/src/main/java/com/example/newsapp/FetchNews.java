package com.example.newsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FetchNews {
    private String jsonURL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=3e47c4c204e14e42921a6d8efae3f9e0";
    private final int jsoncode = 1;
    private ArrayList<NewsDataModel> newsDataModelArrayList;
    private NewsAdapter newsAdapter;
    private static ProgressDialog mProgressDialog;
    private Context context;
    private RecyclerView recyclerView;
    public FetchNews(Context ctx,RecyclerView recycler) {
        context=ctx;
        recyclerView=recycler;
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchNews() {

        showSimpleProgressDialog( context, "Loading...","Fetching News Please Wait...",false);

        StringRequest stringRequest = new StringRequest(jsonURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("neewwss",response);
                onTaskCompleted(response,jsoncode);


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        removeSimpleProgressDialog();
                        Toast.makeText(context,"something went wrong while sending request",Toast.LENGTH_LONG).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void onTaskCompleted(String response, int serviceCode) {
        Log.d("responsejson", response.toString());
        switch (serviceCode) {
            case jsoncode:

                if (isSuccess(response)) {
                    removeSimpleProgressDialog();  //will remove progress dialog
                    loadView(response);
                }else {
                    Toast.makeText(context, getErrorCode(response), Toast.LENGTH_SHORT).show();
                    removeSimpleProgressDialog();
                }
        }
    }

    public void loadView(String response){
        newsDataModelArrayList = getInfo(response);
        newsAdapter = new NewsAdapter(context,newsDataModelArrayList);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    }

    public ArrayList<NewsDataModel> getInfo(String response) {
        ArrayList<NewsDataModel> newsModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("ok")) {

                JSONArray dataArray = jsonObject.getJSONArray("articles");

                for (int i = 0; i < dataArray.length(); i++) {

                    NewsDataModel newsModel = new NewsDataModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    JSONObject sourceobj=dataobj.getJSONObject("source");
                    newsModel.setSource(sourceobj.getString("name"));
                    newsModel.setTitle(dataobj.getString("title"));
                    newsModel.setDescription(dataobj.getString("description"));
                    newsModel.setUrlToImage(dataobj.getString("urlToImage"));
                    newsModel.setUrl(dataobj.getString("url"));
                    newsModelArrayList.add(newsModel);

                }
                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putString("theJson",jsonObject.toString()).apply();


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsModelArrayList;
    }

    public boolean isSuccess(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("status").equals("ok")) {
                return true;
            } else {

                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getErrorCode(String response) {
        return "No data";
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
