package com.github.ecorpbro;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class ProductsListFragment extends Fragment {
    public static final String TAG = "ProductsListFragment";

    private RecyclerView mProductsRecyclerView;
    private ProductsAdapter mProductsAdapter;
    private Products mProducts;

    public static ProductsListFragment newInstance() {
        return new ProductsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_products_page, container, false);

        mProductsRecyclerView = (RecyclerView) view.findViewById(R.id.products_recycler_view);
        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        try {
            mProducts = JSONReader.readProductsJSONFile(this.getActivity());
            List<ProductItem> productItems = mProducts.getProductItemList();
            mProductsAdapter = new ProductsAdapter(productItems);
            mProductsRecyclerView.setAdapter(mProductsAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

//*******************************ProductsHolder********************************//
    private class ProductsHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mQuantityTextView;
        private TextView mPriceTextView;

        private ProductItem mProductItem;

        public ProductsHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_product,parent,false));
            mNameTextView = (TextView)itemView.findViewById(R.id.product_name);
            mQuantityTextView = (TextView)itemView.findViewById(R.id.product_quantity);
            mPriceTextView = (TextView)itemView.findViewById(R.id.product_price);
        }

        public void bind(ProductItem productItem) {
            mProductItem = productItem;
            mNameTextView.setText(mProductItem.getName());
            mQuantityTextView.setText(mProductItem.getQuantity());
            mPriceTextView.setText(mProductItem.getPrice());//!!!!!!!!!!(int to double)
        }
    }

//*******************************ProductsAdapter********************************//
    private class ProductsAdapter extends RecyclerView.Adapter<ProductsHolder> {
        private List<ProductItem> mProductItems;

        public ProductsAdapter(List<ProductItem> productItems) {
            this.mProductItems = productItems;
        }

        @NonNull
        @Override
        public ProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ProductsHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsHolder holder, int position) {
            ProductItem productItem = mProductItems.get(position);
            holder.bind(productItem);
        }

        @Override
        public int getItemCount() {
            return mProductItems.size();
        }
    }
}
