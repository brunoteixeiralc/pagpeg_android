package com.br.pagpeg.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.br.pagpeg.adapter.user.CategoryAdapter;
import com.br.pagpeg.model.Store;
import com.br.pagpeg.utils.DividerItemDecoration;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by brunolemgruber on 15/07/16.
 */

public class DetailStoreFragment extends Fragment implements OnMapReadyCallback{

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private Store store;
    private ImageView imgMap;
    private GoogleMap gMap;
    //Store location
    LatLng latLng = new LatLng(35.0116363, 135.7680294);
    private SupportMapFragment mapFragment;
    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.content_store_detail, container, false);
        } catch (InflateException e) {
        }

        //Toolbar MainActivity
        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.GONE);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(DetailStoreFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new CategoryAdapter(onClickListener(),DetailStoreFragment.this.getActivity(),null));

        recyclerView.addItemDecoration(new DividerItemDecoration(DetailStoreFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DetailStoreFragment.this);

        return view;
    }

    private CategoryAdapter.CategoryOnClickListener onClickListener() {
        return new CategoryAdapter.CategoryOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {

                fragment = new ProductListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-15.7797200,-47.929720)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(-15.7797200,-47.9297200), 5f);
        googleMap.moveCamera(cameraUpdate);
    }
}
