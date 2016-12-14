package com.br.pagpeg.fragment.user;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.br.pagpeg.R;
import com.br.pagpeg.adapter.user.PickUpSummaryAdapter;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.Product;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.model.Store;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by brunolemgruber on 22/07/16.
 */

public class PickUpSummaryFragment extends Fragment{

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private HorizontalStepView stepView;
    private TextView storeName,storeAddress;
    private ImageView storeImg;
    private Store store;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private Cart order;
    private DatabaseReference mDatabase;
    private List<ProductCart> productCarts;
    int count;

    public PickUpSummaryFragment(){}

    public PickUpSummaryFragment(HorizontalStepView stepView) {
        this.stepView = stepView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_pickup_summary, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Sumário da sua compra");

        store = (Store) getArguments().getSerializable("store");
        order = (Cart) getArguments().getSerializable("order");

        storeAddress = (TextView) view.findViewById(R.id.store_address);
        storeName = (TextView) view.findViewById(R.id.store_name);
        storeImg = (ImageView) view.findViewById(R.id.store_img);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        storeAddress.setText(store.getAddress());
        storeName.setText(store.getName());
        Glide.with(PickUpSummaryFragment.this.getContext()).load(store.getImg()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(storeImg);

        if (productCarts == null)
            productCarts = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(PickUpSummaryFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        for (Map.Entry<String, ProductCart> entry : order.getProducts().entrySet()) {

            final ProductCart value = entry.getValue();

            mDatabase.child("product").child(entry.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    count++;

                    if (dataSnapshot.hasChildren()) {
                        Product p = dataSnapshot.getValue(Product.class);
                        p.setName(dataSnapshot.getKey());
                        value.setProduct(p);

                        productCarts.add(value);
                    }

                    if (count >= order.getProducts().size()) {

                        mAdapter = new PickUpSummaryAdapter(PickUpSummaryFragment.this.getContext(),productCarts);
                        recyclerView.setAdapter(mAdapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return view;
    }
}
