package com.github.ecorpbro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.ecorpbro.products.ProductItem;
import com.github.ecorpbro.products.Products;

import java.util.List;
import java.util.UUID;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class ProductPagerActivity extends AppCompatActivity {
    public static final String TAG = "ProductPagerAdapter";

    private static final String EXTRA_PRODUCT_ID = "com.github.ecorpbro.product_id";

    private ViewPager mViewPager;
    private List<ProductItem> mProductItems;

    public static Intent newIntent(Context packageContext, UUID productId) {
        Intent intent = new Intent(packageContext, ProductPagerActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_pager);

        UUID productId = (UUID) getIntent().getSerializableExtra(EXTRA_PRODUCT_ID);

        Log.d(TAG, "onCreate");
        Log.d(TAG, "productId = " + productId);


        mViewPager = (ViewPager) findViewById(R.id.product_view_pager);

        mProductItems = Products.get(this).getProductItemList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                ProductItem productItem = mProductItems.get(position);
                return ProductFragment.newInstance(productItem.getId());
            }

            @Override
            public int getCount() {
                return mProductItems.size();
            }
        });

        for (int i = 0; i < mProductItems.size(); i++) {
            if (mProductItems.get(i).getId().equals(productId)) {
                mViewPager.setCurrentItem(i);
                Log.d(TAG,"mViewPager.setCurrentItem(i) i = " + i);
                break;
            }
        }
    }
}
