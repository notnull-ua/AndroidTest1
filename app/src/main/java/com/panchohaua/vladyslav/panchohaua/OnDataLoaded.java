package com.panchohaua.vladyslav.panchohaua;

import java.util.ArrayList;

/**
 * Created by Yevhenii on 27.09.16.
 */

public interface OnDataLoaded {
    void onDataLoaded(ArrayList<Brand> response, int pagesCount);
}
