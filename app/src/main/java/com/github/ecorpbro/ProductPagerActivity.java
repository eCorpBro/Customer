package com.github.ecorpbro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class ProductPagerActivity extends AppCompatActivity {

    private static final String EXTRA_PRODUCT_ID = "com.github.ecorpbro.product_id";

    private ViewPager mViewPager;
    private List<ProductItem> mProductItems;

    public static Intent newIntent(Context packageContext, int productId) {
        Intent intent = new Intent(packageContext, ProductPagerActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_pager);

        int productId = (int)getIntent().getSerializableExtra(EXTRA_PRODUCT_ID);

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
            if (mProductItems.get(i).getId() == productId) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
