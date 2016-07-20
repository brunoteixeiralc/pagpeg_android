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
import com.br.pagpeg.adapter.CCAdapter;
import com.br.pagpeg.utils.DividerItemDecoration;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class CCManageFragment extends Fragment {


    private static View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cc_manage_frag, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(CCManageFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        mAdapter = new CCAdapter(onClickListener(),CCManageFragment.this.getContext(),null);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(CCManageFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return  view;
    }

    private CCAdapter.CCOnClickListener onClickListener() {
        return new CCAdapter.CCOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {

//                fragment = new DetailStoreFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        };
    }
}
