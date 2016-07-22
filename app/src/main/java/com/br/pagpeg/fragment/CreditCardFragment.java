package com.br.pagpeg.fragment;

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
import com.br.pagpeg.adapter.CreditCardAdapter;
import com.br.pagpeg.utils.DividerItemDecoration;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class CreditCardFragment extends Fragment {


    private static View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_credit_card, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(CreditCardFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        mAdapter = new CreditCardAdapter(onClickListener(),CreditCardFragment.this.getContext(),null);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(CreditCardFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return  view;
    }

    private CreditCardAdapter.CCOnClickListener onClickListener() {
        return new CreditCardAdapter.CCOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {

//                fragment = new DetailStoreFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        };
    }
}
