package com.br.pagpeg.fragment;

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
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.br.pagpeg.adapter.ProductAdapter;
import com.br.pagpeg.utils.DividerItemDecoration;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductListFragment extends Fragment {

    private static View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;
    private ImageView mIconListImageView;
    private ImageView mIconMapImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_product, container, false);

        //Toolbar MainActivity
        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Bebidas");

        mIconMapImageView = (ImageView) toolbarMainActivity.findViewById(R.id.ic_mapStore);
        mIconListImageView = (ImageView) toolbarMainActivity.findViewById(R.id.ic_listStore);
        mIconListImageView.setVisibility(View.GONE);
        mIconMapImageView.setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(ProductListFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        mAdapter = new ProductAdapter(onClickListener(),ProductListFragment.this.getContext(),null);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(ProductListFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return  view;
    }

    private ProductAdapter.ProductOnClickListener onClickListener() {
        return new ProductAdapter.ProductOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {

                fragment = new ProductDetailFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        };
    }
}
