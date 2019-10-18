package com.github.ecorpbro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.ecorpbro.products.ProductItem;
import com.github.ecorpbro.products.Products;

import java.util.UUID;

public class ProductFragment extends Fragment {
    private static final String ARG_PRODUCT_ID = "com.github.ecorpbro.product_id";

    private ProductItem mProductItem;

    private TextView mName;
    private TextView mQuantity;
    private TextView mPrice;
    private TextView mOrder;


    public static ProductFragment newInstance(UUID productId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT_ID,productId);
        ProductFragment productFragment = new ProductFragment();
        productFragment.setArguments(args);
        return productFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID   productId = (UUID) getArguments().getSerializable(ARG_PRODUCT_ID);
        mProductItem = Products.get(getActivity()).getProductItem(productId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        mName = (TextView) v.findViewById(R.id.productItem_name);
        mName.setText(mProductItem.getName());

        mQuantity = (TextView) v.findViewById(R.id.productItem_quantity);
        mQuantity.setText(mProductItem.getQuantity());

        mPrice = (TextView) v.findViewById(R.id.productItem_price);
        mPrice.setText(mProductItem.getPrice());

        mOrder = (TextView) v.findViewById(R.id.productItem_order);
        if (mProductItem.getOrder().equals("")) {
            mOrder.setText("0");
        } else mOrder.setText(mProductItem.getOrder());

        return v;
    }


}
