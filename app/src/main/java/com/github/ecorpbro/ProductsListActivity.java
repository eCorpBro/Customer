package com.github.ecorpbro;

import android.util.Log;

import androidx.fragment.app.Fragment;

public class ProductsListActivity extends SingleFragmentActivity {

    public static final String TAG = "ProductListActivity";

    @Override
    protected Fragment createFragment() {
        return ProductsListFragment.newInstance();
    }
}
