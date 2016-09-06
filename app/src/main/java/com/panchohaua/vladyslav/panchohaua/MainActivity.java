package com.panchohaua.vladyslav.panchohaua;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    TextView responseView;
    ProgressBar progressBar;
    Integer page = 1;
    Integer countPage = null;

    static final String API_URL = "http://admin.panchoha-ua.com/v1/brands";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseView = (TextView) findViewById(R.id.responseView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask();
                retrieveFeedTask.execute();


                try {
                    String jsonResponse;
                    jsonResponse = retrieveFeedTask.get();


                } catch (Exception e) {
                    Log.i("log", e.getMessage());
                }


            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                URL url = new URL(API_URL + "?page=" + page);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                countPage = Integer.parseInt(urlConnection.getHeaderField("X-Pagination-Page-Count"));
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            JSONArray objects = null;

            try {
                List<String> names = new ArrayList<>();
                objects = (JSONArray) new JSONTokener(response).nextValue();
                ListView listView = (ListView) findViewById(R.id.listView);
                for (int i = 0; i < objects.length(); i++) {
                    JSONObject obj = objects.getJSONObject(i);
                    names.add(obj.getString("name"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, names);
                listView.setAdapter(adapter);
                if (page < countPage) page++;

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // responseView.setText(response);
        }
    }

}