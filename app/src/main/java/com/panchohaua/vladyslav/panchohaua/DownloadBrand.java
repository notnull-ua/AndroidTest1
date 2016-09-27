package com.panchohaua.vladyslav.panchohaua;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Vladyslav on 15.09.2016.
 */
public class DownloadBrand extends AsyncTask<Integer, Void, ArrayList<Brand>> {

    private Exception exception;
    private Context context;
    private int countPages;
    private int currentPage = 1;
    private OnDataLoaded mListener;

    private static String API_URL = "http://admin.panchoha-ua.com/v1/brands";


    public DownloadBrand(Context context) {
        this.context = context;
    }

    public DownloadBrand(Context context, OnDataLoaded mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    protected void onPreExecute() {
    }

    protected ArrayList<Brand> doInBackground(Integer... page) {
        // Do some validation here
        if (page != null) {
            currentPage = page[0];
        }
        try {
            String apiUrl = API_URL;
            if (currentPage > 0 && currentPage != 1) {
                apiUrl += "?page=" + currentPage;
            }
            Log.e(TAG, "doInBackground: apiUrl" + apiUrl);
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            countPages = Integer.parseInt(urlConnection.getHeaderField("X-Pagination-Page-Count"));
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return this.parseJson(stringBuilder.toString());
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(ArrayList<Brand> response) {
        Log.d("Tag", "post Execute()");
        if (mListener != null) {
            mListener.onDataLoaded(response, countPages);
        }

    }

    /*парсінг  json  та повертає список об'єктів бренду*/
    protected ArrayList<Brand> parseJson(String jsonString) {
        JSONArray objects;
        ArrayList<Brand> brands = new ArrayList<>();
        try {
            objects = (JSONArray) new JSONTokener(jsonString).nextValue();
            for (int i = 0; i < objects.length(); i++) {
                JSONObject obj = objects.getJSONObject(i);
                brands.add(new Brand(obj.getString("name"), obj.getString("img_src")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return brands;
    }


    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCountPages() {
        return this.countPages;
    }

    public void setmListener(OnDataLoaded mListener) {
        this.mListener = mListener;
    }
}