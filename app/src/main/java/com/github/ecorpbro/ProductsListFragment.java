package com.github.ecorpbro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ecorpbro.database.ProductBaseHelper;
import com.github.ecorpbro.database.ProductCursorWrapper;
import com.github.ecorpbro.database.ProductDbSchema;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductsListFragment extends Fragment {
    public static final String TAG = "ProductsListFragment";

    private RecyclerView mRecyclerView;
    private Button mButtonDownload;
    private Button mButtonOrder;
    private Button mButtonSave;
    private Button mButtonLoad;
    private Button mButtonRead;
    private String mJsonString;

    private Products mProducts;
    private Context mContext;

    public static ProductsListFragment newInstance() {
        return new ProductsListFragment();
    }

    private class JSONDownloaderTask extends AsyncTask<Void, Void, Products> {
        @Override
        protected Products doInBackground(Void... params) {

                try {
                    mJsonString = new JSONDownloader().getUrlString("https://api.myjson.com/bins/nilz1");
                    mContext = getActivity();
                    mProducts = JSONDownloader.jsonStringToProducts(mJsonString,mContext);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return mProducts;
        }

        @Override
        protected void onPostExecute(Products products) {
            setupAdapter();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_products_page, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.products_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mButtonDownload = (Button) view.findViewById(R.id.button_download);

        mButtonOrder = (Button) view.findViewById(R.id.button_order);
        mButtonOrder.setEnabled(false);

        mButtonSave = (Button) view.findViewById(R.id.button_save);
        mButtonSave.setEnabled(false);

        mButtonRead = (Button)view.findViewById(R.id.button_read);

        mButtonLoad = (Button) view.findViewById(R.id.button_load);

        mButtonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSave.setEnabled(true);
                mButtonOrder.setEnabled(true);
                new JSONDownloaderTask().execute();
            }
        });

        mButtonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ProductItem product: mProducts.getProductItemList()) {
                    if (product.getOrder() == null) {
                        product.setOrder("0");
                    }
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, orderToString());
                intent.putExtra(Intent.EXTRA_SUBJECT, "Order");
                startActivity(intent);
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProducts != null) {
                    mProducts.addProducts(getContext());
                }
            }
        });

        mButtonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readProducts();
            }
        });

        mButtonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProducts != null) {
                    mProducts = getProductsFromBase();
                    setupAdapter();
                }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("products",mProducts);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mProducts = (Products) savedInstanceState.getSerializable("products");
        }
        if (mProducts != null) {
            setupAdapter();
        }
    }

    //*******************************ProductsHolder********************************//
    private class ProductsHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mQuantityTextView;
        private TextView mPriceTextView;
        private EditText mTextOrder;

        private ProductItem mProductItem;

        public ProductsHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_product,parent,false));
            mNameTextView = (TextView)itemView.findViewById(R.id.product_name);
            mQuantityTextView = (TextView)itemView.findViewById(R.id.product_quantity);
            mPriceTextView = (TextView)itemView.findViewById(R.id.product_price);
            mTextOrder = (EditText) itemView.findViewById(R.id.product_order);
        }

        public void bind(ProductItem productItem) {
            mProductItem = productItem;
            mNameTextView.setText(mProductItem.getName());
            mQuantityTextView.setText(mProductItem.getQuantity());
            mPriceTextView.setText(mProductItem.getPrice());
            mTextOrder.setText(mProductItem.getOrder());
            mTextOrder.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mProductItem.setOrder(mTextOrder.getText().toString());
                }
            });
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

//*******************************UserMetods********************************//
    private void setupAdapter() {
        if (isAdded()) {
            List<ProductItem> productItemList = mProducts.getProductItemList();
            ProductsAdapter productsAdapter = new ProductsAdapter(productItemList);
            mRecyclerView.setAdapter(productsAdapter);
        }
    }

    private String orderToString() {
        String stringOrder = "ORDER\n\n";
        for (ProductItem item: mProducts.getProductItemList()) {
            stringOrder += String.format("%-20s %5s%n", item.getName(), item.getOrder());
        }
        return stringOrder;
    }

    public Context getContext() {
        return getActivity();
    }

    public void readProducts() {
        ProductCursorWrapper cursorWrapper = getCursorWrapper();
        if (cursorWrapper.moveToFirst()) {
            do {
                ProductItem productItem = cursorWrapper.getProduct();
                Log.d("readDb", "_id = " + cursorWrapper.getInt(1) +
                        ", ID = " + productItem.getId() +
                        ", name = " + productItem.getName() +
                        ", quantity = " + productItem.getQuantity() +
                        ", price = " + productItem.getPrice() +
                        ", order = " + productItem.getOrder());
            } while (cursorWrapper.moveToNext());
        } else
            Log.d("readDb", "0 rows");
        cursorWrapper.close();
    }

    public Products getProductsFromBase() {
        ProductCursorWrapper cursorWrapper = getCursorWrapper();
        List<ProductItem> mProductItemList = new ArrayList<>();
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                mProductItemList.add(cursorWrapper.getProduct());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        Products products = new Products();
        products.setProductItemList(mProductItemList);
        return products;
    }

    private ProductCursorWrapper getCursorWrapper() {
        SQLiteDatabase db = new ProductBaseHelper(getActivity()).getWritableDatabase();
        Cursor cursor = db.query(
                ProductDbSchema.ProductTable.DB_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);
        return cursorWrapper;
    }
}


