package com.br.pagpeg.fragment.user;

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
import com.br.pagpeg.adapter.user.PickUpShopperSummaryAdapter;

/**
 * Created by brunolemgruber on 22/07/16.
 */

public class PickUpShopperSummaryFragment extends Fragment {

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_pickup_shopper_summary, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(PickUpShopperSummaryFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        mAdapter = new PickUpShopperSummaryAdapter(onClickListener(),PickUpShopperSummaryFragment.this.getContext(),null);
        recyclerView.setAdapter(mAdapter);

        return view;
    }


    private PickUpShopperSummaryAdapter.PickUpSummaryOnClickListener onClickListener() {
        return new PickUpShopperSummaryAdapter.PickUpSummaryOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {


            }
        };
    }
}
