package com.github.ecorpbro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ecorpbro.database.ProductBaseManager;
import com.github.ecorpbro.database.SettingsBaseManager;
import com.github.ecorpbro.products.ProductItem;
import com.github.ecorpbro.products.Products;
import com.github.ecorpbro.settings.SettingsActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class ProductsListFragment extends Fragment {
    public static final String TAG = "ProductsListFragment";

    private RecyclerView mRecyclerView;
    private ProductsAdapter mProductsAdapter;
    private Toast mToast;
    private boolean isToastNoUrl = false;

    private Button mBtnDownload;
    private Button mBtnOrder;
    private Button mBtnSave;
    private Button mBtnLoad;
    private Button mBtnClear;
    private ProgressBar mProgressBar;

    private Products mProducts;

    public static ProductsListFragment newInstance() {
        return new ProductsListFragment();
    }

    private class JSONDownloaderTask extends AsyncTask<Void, Void, Products> {
        @Override
        protected Products doInBackground(Void... params) {

            try {
                String url = SettingsBaseManager.get(getActivity()).getDefUrl();
                if (url == SettingsBaseManager.NOSORUCE) {
                    isToastNoUrl = true;
                    return null;
                }
                String jsonString = new JSONDownloader().getUrlString(url);
                mProducts = JSONDownloader.jsonStringToProducts(jsonString, getContext());
                mProducts.addProducts(getContext());
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
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_page, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.products_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mBtnDownload = (Button) view.findViewById(R.id.button_download);

        mBtnOrder = (Button) view.findViewById(R.id.button_order);

        mBtnSave = (Button) view.findViewById(R.id.button_save);

        mBtnLoad = (Button) view.findViewById(R.id.button_load);

        mBtnClear = (Button) view.findViewById(R.id.button_clear);

        mBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                new JSONDownloaderTask().execute();

            }
        });

        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ProductItem product : mProducts.getProductItemList()) {
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

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProducts != null) {
                    mProducts.addProducts(getContext());
                }
            }
        });

        mBtnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProducts = new ProductBaseManager(getActivity()).getProductsFromBase();
                if (mProducts != null) {
                    setupAdapter();
                }
            }
        });

        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProducts.deleteProducts();
                mProducts = new ProductBaseManager(getActivity()).getProductsFromBase();
                setupAdapter();
            }
        });

        setupAdapter();

        return view;
    }

    //Сохранение данных прм повороте экрана
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("products", mProducts);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mProducts = (Products) savedInstanceState.getParcelable("products");
        }
        if (mProducts != null) {
            setupAdapter();
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_product_list,menu);

        MenuItem settingItem = menu.findItem(R.id.txtDefName);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.txtDefName:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //*******************************ProductsHolder********************************//
    private class ProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private TextView mQuantityTextView;
        private TextView mPriceTextView;
        private EditText mTextOrder;

        private ProductItem mProductItem;

        public ProductsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_product, parent, false));
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.product_name);
            mQuantityTextView = (TextView) itemView.findViewById(R.id.product_quantity);
            mPriceTextView = (TextView) itemView.findViewById(R.id.product_price);
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
            mTextOrder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    mProducts.updateProductItem(mProductItem);
                }
            });
        }

        @Override
        public void onClick(View v) {
            mProducts.addProducts(getContext());
            Intent intent = ProductPagerActivity.newIntent(getActivity(), mProductItem.getId());
            startActivity(intent);
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
            return new ProductsHolder(layoutInflater, parent);
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
            if (isToastNoUrl) {
                mToast = Toast.makeText(getActivity(),"Установите в настройках адрес источника данных!", Toast.LENGTH_LONG);
                mToast.setGravity(1,1,1);
                mToast.show();
            }
            if (mProducts == null) {
                mProducts = Products.get(getActivity());
                mProducts.setProductItemListToBase();
            }
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            List<ProductItem> productItemList = mProducts.getProductItemList();
            mProductsAdapter = new ProductsAdapter(productItemList);
            mRecyclerView.setAdapter(mProductsAdapter);
        }
    }

    private String orderToString() {
        String stringOrder = "ORDER\n\n";
        for (ProductItem item : mProducts.getProductItemList()) {
            stringOrder += String.format("%-20s %5s%n", item.getName(), item.getOrder());
        }
        return stringOrder;
    }

    public Context getContext() {
        return getActivity();
    }

}



