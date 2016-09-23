package com.br.pagpeg.fragment.user;

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
import com.br.pagpeg.adapter.user.StoreAdapter;
import com.br.pagpeg.model.Store;
import com.br.pagpeg.utils.DividerItemDecoration;

import java.util.List;

/**
 * Created by brunolemgruber on 14/07/16.
 */

public class StoreListFragment extends Fragment {

    private static View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;
    private ImageView mIconMapImageView;
    private List<Store> storeList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_store, container, false);

        storeList = (List<Store>) getArguments().getSerializable("stores");

        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Lojas nas proximidades");
        mIconMapImageView = (ImageView) toolbarMainActivity.findViewById(R.id.ic_mapStore);
        mIconMapImageView.setVisibility(View.VISIBLE);
        mIconMapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mLayoutManager = new LinearLayoutManager(StoreListFragment.this.getActivity());
        mAdapter = new StoreAdapter(onClickListener(),StoreListFragment.this.getContext(),storeList);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(StoreListFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return  view;
    }

    private StoreAdapter.StoreOnClickListener onClickListener() {
        return new StoreAdapter.StoreOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {
                fragment = new DetailStoreFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        };
    }

}
