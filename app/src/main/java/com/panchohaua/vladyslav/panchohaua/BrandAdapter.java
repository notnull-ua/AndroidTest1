package com.panchohaua.vladyslav.panchohaua;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Vladyslav on 15.09.2016.
 */
public class BrandAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Brand> object;

    BrandAdapter(Context context, ArrayList<Brand> brands) {
        ctx = context;
        object = brands;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // кількість елементів
    public int getCount() {
        return object.size();
    }

    //  елемент за позицією
    public Object getItem(int position) {
        return object.get(position);
    }

    // id  по позиції
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Brand b = getBrand(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewBrand);

        ((TextView) view.findViewById(R.id.nameBrand)).setText(b.getName());
        Picasso.with(ctx).load(b.getUrlImage()).into(imageView);

        return view;
    }

    // повертає бренд по позиції
    Brand getBrand(int position) {
        return ((Brand) getItem(position));
    }

    public void addData(ArrayList<Brand> data) {
        object.addAll(data);
    }
}
