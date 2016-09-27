package com.panchohaua.vladyslav.panchohaua;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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

    ProgressBar progressBar;
    Button queryButton;


    BrandAdapter brandAdapter;
    DownloadBrand downloadBrand;
    Integer currentPage = 1;
    Integer countPages = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final ListView listView = (ListView) findViewById(R.id.listView);

        brandAdapter = new BrandAdapter(MainActivity.this, loadNextBrands());
        listView.setAdapter(brandAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

                if (loadMore && downloadBrand.getStatus() == AsyncTask.Status.FINISHED && loadNextBrands() != null) {
                    brandAdapter.addData(loadNextBrands());
                    brandAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    protected ArrayList<Brand> loadNextBrands() {
        ArrayList<Brand> brands = new ArrayList<>();
        downloadBrand = (DownloadBrand) new DownloadBrand(MainActivity.this).execute(this.currentPage);

        try {
            this.countPages = downloadBrand.getCountPages();
            if (this.currentPage < this.countPages) {
                brands = downloadBrand.get();
                this.currentPage++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brands;
    }


}