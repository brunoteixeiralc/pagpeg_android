package com.br.pagpeg.fragment.shopper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.pagpeg.R;
import com.br.pagpeg.adapter.shopper.OrderHistoryAdapter;
import com.br.pagpeg.utils.DividerItemDecoration;

/**
 * Created by brunolemgruber on 27/07/16.
 */

public class OrderHistoryFragment extends Fragment {

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list, container, false);

        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Histórico de pedidos");

        mLayoutManager = new LinearLayoutManager(OrderHistoryFragment.this.getActivity());
        mAdapter = new OrderHistoryAdapter(onClickListener(),OrderHistoryFragment.this.getContext(),null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(OrderHistoryFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return view;
    }

    private OrderHistoryAdapter.OrderOnClickListener onClickListener() {
        return new OrderHistoryAdapter.OrderOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {
                fragment = new OrderHistoryProductFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        };
    }
}
