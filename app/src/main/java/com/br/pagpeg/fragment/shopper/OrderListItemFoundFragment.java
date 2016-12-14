package com.br.pagpeg.fragment.shopper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.pagpeg.R;
import com.br.pagpeg.adapter.shopper.OrderListAdapter;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunolemgruber on 28/07/16.
 */

public class OrderListItemFoundFragment extends Fragment {

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;
    private List<ProductCart> productCartsAll;
    private ProductCart productCartSelect;
    public String user = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list, container, false);

        productCartsAll = (List<ProductCart>) getArguments().getSerializable("productsCart");

        mLayoutManager = new LinearLayoutManager(OrderListItemFoundFragment.this.getActivity());
        mAdapter = new OrderListAdapter(onClickListener(),OrderListItemFoundFragment.this.getContext(),productCartsAll);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(OrderListItemFoundFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return view;
    }

    private OrderListAdapter.OrderListOnClickListener onClickListener() {
        return new OrderListAdapter.OrderListOnClickListener() {
            @Override
            public void onClickItem(View view, int idx) {
                productCartSelect = productCartsAll.get(idx);
            }

            @Override
            public void onClickButton(View view, int idx) {
                productCartSelect = productCartsAll.get(idx);
            }
        };
    }

    public void updateView(List<ProductCart> productCarts){
        productCartsAll = new ArrayList<>();
        productCartsAll.addAll(productCarts);
        mAdapter.notifyDataSetChanged();
    }
}
