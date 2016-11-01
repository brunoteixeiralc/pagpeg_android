package com.br.pagpeg.fragment.shopper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.Store;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by brunolemgruber on 28/07/16.
 */

public class OrderFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private Button btnSeeOrder;
    private Fragment fragment;
    private SupportMapFragment mapFragment;
    private DatabaseReference mDatabase;
    private Cart order;
    private Store orderStore;
    private TextView storeName,storeAddress,storeKm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_order, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        storeAddress = (TextView) view.findViewById(R.id.store_address);
        storeName = (TextView) view.findViewById(R.id.store_name);
        storeKm = (TextView) view.findViewById(R.id.store_km);

        btnSeeOrder = (Button) view.findViewById(R.id.see_order);
        btnSeeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new OrderTabFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        getOrder();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(orderStore.getLat(), orderStore.getLng())));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(orderStore.getLat(),orderStore.getLng()), 5f);
        googleMap.moveCamera(cameraUpdate);
    }

    private void getOrder() {

        mDatabase.child("cart_online").orderByChild("shopper").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {
                        order = st.getValue(Cart.class);
                        getStore(order.getNetwork(),order.getStore());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getStore(String network, String store){

        mDatabase.child("network").child(network).child(store).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    orderStore = ((DataSnapshot)dataSnapshot.getChildren()).getValue(Store.class);

                    storeName.setText(orderStore.getName());
                    storeAddress.setText(orderStore.getAddress());
                    storeAddress.setText(String.valueOf(orderStore.getDistance()) + " km");

                    mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.store_map);
                    mapFragment.getMapAsync(OrderFragment.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
