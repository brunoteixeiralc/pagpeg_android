package com.br.pagpeg.fragment.shopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.BarCodeActivity;
import com.br.pagpeg.activity.shopper.ProductDetailActivity;
import com.br.pagpeg.adapter.shopper.OrderListAdapter;
import com.br.pagpeg.utils.DividerItemDecoration;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by brunolemgruber on 28/07/16.
 */

public class OrderListItemFragment extends Fragment {

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_order_items, container, false);

        mLayoutManager = new LinearLayoutManager(OrderListItemFragment.this.getActivity());
        mAdapter = new OrderListAdapter(onClickListener(),OrderListItemFragment.this.getContext(),null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(OrderListItemFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return view;
    }

    private OrderListAdapter.OrderListOnClickListener onClickListener() {
        return new OrderListAdapter.OrderListOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {
                IntentIntegrator.forSupportFragment(OrderListItemFragment.this).setCaptureActivity(BarCodeActivity.class).initiateScan();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                startActivity(new Intent(OrderListItemFragment.this.getActivity(),ProductDetailActivity.class));
            } else {
                Log.d("MainActivity", "Scanned");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
