package com.panchohaua.vladyslav.panchohaua;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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
    DownloadBrand downloadBrand;
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.i(TAG, "-----------------------------------------------");
//                Log.d(TAG, "onScroll: firstVisibleItem =" + firstVisibleItem);
//                Log.d(TAG, "onScroll: visibleItemCount =" + visibleItemCount);
//                Log.d(TAG, "onScroll: totalItemCount =" + totalItemCount);

                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                //
                if (loadMore) {
                    Log.d(TAG, "onScroll: LoadMore = true");

                    loadNextBrands();
                }
            }
        });
    }

    private void loadNextBrands() {
//        ArrayList<Brand> brands = new ArrayList<>();
        if (loadMore) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "try: downloadBrand ");
            downloadBrand = new DownloadBrand(MainActivity.this);
            downloadBrand.setmListener(this);
            downloadBrand.execute(this.currentPage);


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
            this.currentPage++;
            mDataset.addAll(response);
            brandAdapter.notifyDataSetChanged();
            if (response.size() < perPage || currentPage == countPage) loadMore = false;
            Log.d(TAG, "response: size = " + mDataset.size());
            Log.d(TAG, "current page "+currentPage+"countPage:  = " +countPage);
        }
    }
}