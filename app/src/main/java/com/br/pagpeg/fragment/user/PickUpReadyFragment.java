package com.br.pagpeg.fragment.user;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.br.pagpeg.R;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.Shopper;
import com.br.pagpeg.model.Store;
import com.br.pagpeg.notification.SendNotification;
import com.br.pagpeg.utils.EnumStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.br.pagpeg.R.id.img;

/**
 * Created by brunolemgruber on 21/07/16.
 */

@RuntimePermissions
public class PickUpReadyFragment extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {

    private View view;
    private SupportMapFragment mapFragment;
    private String userUid;
    private HorizontalStepView stepView;
    private DatabaseReference mDatabase;
    private GoogleApiClient googleApiClient;
    private Location storeLocation;
    private Store orderStore;
    private TextView storeName,storeAddress,storeKm,shopperName, storeCloseTime,storeTimeDayToGet,instructions;
    private ImageView shopperImg,storeImg;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private Button btnPickedProduct;
    private Fragment fragment;
    private Cart order;
    private Shopper shopper;
    private AlertDialog builder = null;

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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Hora de pegar sua compra");

        stepView.setStepsViewIndicatorComplectingPosition(3);

        instructions = (TextView) view.findViewById(R.id.instructions);
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getActivity(),R.style.Dialog_Quantity)
                        .setPositiveButton("OK", null)
                        .setTitle("PagPeg")
                        .setMessage("Hora de pegar sua compra com o shopper do PagPeg.\nPode se dirigir ao estacionamento do " + orderStore.getName() + " e pegar a sua compra com " + shopper.getName())
                        .setIcon(R.mipmap.ic_launcher)
                        .create();
                builder.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                        btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();
                            }
                        });
                    }
                });
                builder.show();
            }
        });
        storeAddress = (TextView) view.findViewById(R.id.store_address);
        storeName = (TextView) view.findViewById(R.id.store_name);
        storeKm = (TextView) view.findViewById(R.id.store_km);
        storeImg = (ImageView) view.findViewById(R.id.store_img);
        storeCloseTime = (TextView) view.findViewById(R.id.store_time);
        storeTimeDayToGet = (TextView) view.findViewById(R.id.txt_day_time_get);
        shopperImg = (ImageView) view.findViewById(img);
        shopperName = (TextView) view.findViewById(R.id.txt_img);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        btnPickedProduct = (Button) view.findViewById(R.id.picked_up);
        btnPickedProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putSerializable("store",orderStore);
                b.putSerializable("order",order);

                fragment = new PickUpSummaryFragment();
                fragment.setArguments(b);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(EnumStatus.Status.USER_RECEIVED.getName());

                SendNotification.sendNotificationShopper(shopper.getName(),shopper.getOne_signal_key(),shopper.getKey(),"Ótimo trabalho, seu produto foi entregue com sucesso.",false);
            }
        });

        googleApiClient = new GoogleApiClient.Builder(PickUpReadyFragment.this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

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

        mDatabase.child("cart_online").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    order = dataSnapshot.getValue(Cart.class);
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

    private void getStore(String network, final String store){

        mDatabase.child("network").child(network).child(store).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    orderStore = dataSnapshot.getValue(Store.class);

                    storeName.setText(orderStore.getName());
                    storeAddress.setText(orderStore.getAddress());
                    storeCloseTime.setText("Aberta até as " + orderStore.getClose());
                    storeLocation=new Location("storeLocation");
                    storeLocation.setLatitude(orderStore.getLat());
                    storeLocation.setLongitude(orderStore.getLng());
                    storeTimeDayToGet.setText("Pegue seus produtos antes de " + orderStore.getClose() + " do dia " + com.br.pagpeg.utils.Date.formatDate(new Date(), DateFormat.LONG));
                    Glide.with(PickUpReadyFragment.this).load(orderStore.getImg()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).diskCacheStrategy(DiskCacheStrategy.ALL).into(storeImg);

                    Location l = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    if(l != null){
                        Float distanceTo = l.distanceTo(storeLocation) / 1000;
                        orderStore.setDistance(distanceTo);
                        storeKm.setText(String.valueOf(orderStore.getDistance()) + " km");
                    }

                    mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
                    shopper = dataSnapshot.getValue(Shopper.class);

                    shopperName.setText(shopper.getName() + " está lhe aguardando para entregar a sua compra");
                    Glide.with(PickUpReadyFragment.this.getContext()).load(shopper.getUser_img()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).diskCacheStrategy(DiskCacheStrategy.ALL).into(shopperImg);
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
