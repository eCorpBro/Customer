package com.github.ecorpbro;

import androidx.fragment.app.Fragment;

public class ProductActivity extends SingleFragmentActivity {
    private static final String EXTRA_PRODUCT_ID ="product_id";

    @Override
    protected Fragment createFragment() {
        int productId = (int) getIntent().getSerializableExtra(EXTRA_PRODUCT_ID);
        return ProductFragment.newInstance(productId);
    }
}
