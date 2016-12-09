package com.br.pagpeg.fragment.user;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.baoyachi.stepview.HorizontalStepView;
import com.br.pagpeg.R;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.Shopper;
import com.br.pagpeg.model.Store;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by brunolemgruber on 21/07/16.
 */

@RuntimePermissions
public class PickUpReadyFragment extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {

    private View view;
    private SupportMapFragment mapFragment, miniMapFragment;
    private String userUid;
    private HorizontalStepView stepView;
    private DatabaseReference mDatabase;
    private GoogleApiClient googleApiClient;
    private Location storeLocation;
    private Store orderStore;
    private TextView storeName,storeAddress,storeKm,shopperName;
    private ImageView shopperImg;

    public PickUpReadyFragment(){}

    public PickUpReadyFragment(HorizontalStepView stepView, String userUid) {
        this.stepView = stepView;
        this.userUid = userUid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.content_pickup_ready, container, false);
        } catch (InflateException e) {
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        storeAddress = (TextView) view.findViewById(R.id.store_address);
        storeName = (TextView) view.findViewById(R.id.store_name);
        storeKm = (TextView) view.findViewById(R.id.store_km);

        googleApiClient = new GoogleApiClient.Builder(PickUpReadyFragment.this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        getOrder();

        mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(PickUpReadyFragment.this);

        //miniMapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.store_map);
        //miniMapFragment.getMapAsync(PickUpReadyFragment.this);

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

        mDatabase.child("cart_online").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    Cart order = dataSnapshot.getValue(Cart.class);
                    order.setUser(dataSnapshot.getKey());

                    getStore(order.getNetwork(),order.getStore());
                    getShopper(order.getShopper());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getStore(String network, String store){

        mDatabase.child("network").child(network).child(store).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    orderStore = dataSnapshot.getValue(Store.class);

                    storeName.setText(orderStore.getName());
                    storeAddress.setText(orderStore.getAddress());

                    storeLocation=new Location("storeLocation");
                    storeLocation.setLatitude(orderStore.getLat());
                    storeLocation.setLongitude(orderStore.getLng());

                    Location l = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    if(l != null){
                        Float distanceTo = l.distanceTo(storeLocation) / 1000;
                        orderStore.setDistance(distanceTo);
                        storeKm.setText(String.valueOf(orderStore.getDistance()) + " km");
                    }

                    mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.store_map);
                    mapFragment.getMapAsync(PickUpReadyFragment.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getShopper(String uid) {

        mDatabase.child("shoppers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    Shopper shopper = dataSnapshot.getValue(Shopper.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
        googleApiClient.disconnect();
    }

    private void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void startLocationUpdates(){
        PickUpReadyFragmentPermissionsDispatcher.getUserLocationWithCheck(this);
    }

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    public void getUserLocation(){

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("Pagpeg", "Conectado ao google play service");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null && storeLocation != null){
            Float distanceTo = location.distanceTo(storeLocation) / 1000;
            orderStore.setDistance(distanceTo);
            storeKm.setText(String.valueOf(orderStore.getDistance()) + " km");
        }

    }
}
