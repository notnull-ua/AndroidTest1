package com.panchohaua.vladyslav.panchohaua;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnDataLoaded {


    private static final String TAG = MainActivity.class.getSimpleName();
    // спецификаторы доступа!
    ProgressBar progressBar;
    Button queryButton;
    ListView listView;

    BrandAdapter brandAdapter;
    Integer currentPage = 1;
    Integer countPages = 0;
    ArrayList<Brand> mDataset;


    boolean loadMore;
    int perPage = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataset = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //list view як поле класу створювати треба
        listView = (ListView) findViewById(R.id.listView);
        //метод в якости параметру не айс
        brandAdapter = new BrandAdapter(MainActivity.this, mDataset);
        listView.setAdapter(brandAdapter);
        loadMore = true;
        loadNextBrands();
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextBrands();
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    private void loadNextBrands() {
//        ArrayList<Brand> brands = new ArrayList<>();
        if (loadMore) {
            Log.d(TAG, "try: downloadBrand currentPage:" + currentPage);
            int currentPage = this.currentPage;
            new DownloadBrand(MainActivity.this,this).execute(currentPage);
            progressBar.setVisibility(View.VISIBLE);



        }
    }


    @Override
    public void onDataLoaded(ArrayList<Brand> response, int countPage) {
        progressBar.setVisibility(View.GONE);

        if (response == null) {
            Toast toast = Toast.makeText(this,
                    "Щось пішло не так!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            mDataset.addAll(response);
            brandAdapter.notifyDataSetChanged();
            if (response.size() < perPage || currentPage == countPage) {
                Log.d(TAG, "onDataLoaded: load more False");
                loadMore = false;
            }
            this.currentPage++;

            Log.d(TAG, "response: size = " + mDataset.size());
            Log.d(TAG, "current page " + currentPage + "countPage:  = " + countPage);
        }
    }
}