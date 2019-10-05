package com.github.ecorpbro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class ProductFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";

    public static ProductFragment newInstance(int productId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT_ID,productId);
        ProductFragment productFragment = new ProductFragment();
        productFragment.setArguments(args);
        return productFragment;
    }
}
