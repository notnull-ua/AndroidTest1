package com.panchohaua.vladyslav.panchohaua;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladyslav on 15.09.2016.
 */
class DownloadBrand extends AsyncTask<Void, Void, ArrayList<Brand>> {

    private Exception exception;
    private Context context;
    private ProgressBar progressBar;
    private int countPages;
    private int currentPage;


    private static final String API_URL = "http://admin.panchoha-ua.com/v1/brands";

    public DownloadBrand(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        progressBar = (ProgressBar) ((AppCompatActivity) context).findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    protected ArrayList<Brand> doInBackground(Void... urls) {
        // Do some validation here

        try {
            URL url = new URL(API_URL);
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

    protected void onPostExecute(ArrayList response) {
        if (response == null) {
            Toast toast = Toast.makeText(context,
                    "Щось пішло не так!", Toast.LENGTH_SHORT);
            toast.show();
        }
        progressBar.setVisibility(View.GONE);

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

}