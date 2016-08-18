package com.br.pagpeg.fragment.shopper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by brunolemgruber on 28/07/16.
 */

public class OrderFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private Button btnSeeOrder;
    private Fragment fragment;
    private SupportMapFragment mapFragment;
    private ImageView mIconOrderCompleted;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_order, container, false);

        mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(OrderFragment.this);

        btnSeeOrder = (Button) view.findViewById(R.id.see_order);
        btnSeeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new OrderTabFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-15.7797200,-47.929720)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(-15.7797200,-47.9297200), 5f);
        googleMap.moveCamera(cameraUpdate);
    }
}
