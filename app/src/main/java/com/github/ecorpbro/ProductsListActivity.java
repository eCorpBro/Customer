package com.github.ecorpbro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class ProductsListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ProductsListFragment.newInstance();
    }
}
