package com.br.pagpeg.fragment.shopper;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.shopper.MainShopperActivity;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.Product;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.model.Store;
import com.br.pagpeg.model.User;
import com.br.pagpeg.notification.SendNotification;
import com.br.pagpeg.utils.EnumStatus;
import com.br.pagpeg.utils.EnumToolBar;
import com.br.pagpeg.utils.Utils;
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

import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by brunolemgruber on 28/07/16.
 */

@RuntimePermissions
public class OrderFragment extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private View view;
    private Button btnSeeOrder,btnCall,btnNotSeeOrder;
    private Fragment fragment;
    private SupportMapFragment mapFragment;
    private DatabaseReference mDatabase;
    private Cart order;
    private Store orderStore;
    private TextView storeName,storeAddress,storeKm,userName,userNumber,shopperOrder;
    private ImageView userImage;
    private GoogleApiClient googleApiClient;
    private Location storeLocation;
    private ProgressBar progressBar;
    private LinearLayout llMain,llNoOrder;
    private List<ProductCart> productCarts;
    int count;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.content_order, container, false);
        } catch (InflateException e) {

        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        Utils.setIconBar(EnumToolBar.SHOPPERORDER,toolbar);

        googleApiClient = new GoogleApiClient.Builder(OrderFragment.this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        llMain = (LinearLayout) view.findViewById(R.id.ll_main);
        llNoOrder = (LinearLayout) view.findViewById(R.id.ll_empty_order);
        storeAddress = (TextView) view.findViewById(R.id.store_address);
        storeName = (TextView) view.findViewById(R.id.store_name);
        storeKm = (TextView) view.findViewById(R.id.store_km);
        userImage = (ImageView) view.findViewById(R.id.user_image);
        userName = (TextView) view.findViewById(R.id.user_name);
        userNumber = (TextView) view.findViewById(R.id.user_number);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        shopperOrder = (TextView) view.findViewById(R.id.shopper_order);

        llNoOrder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);

        btnSeeOrder = (Button) view.findViewById(R.id.see_order);
        btnSeeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           getProducts();

            }
        });

        btnNotSeeOrder = (Button) view.findViewById(R.id.not_see_order);
        btnNotSeeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("users").child(order.getUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        SendNotification.sendNotificationUser(user.getName(),user.getOne_signal_key(),dataSnapshot.getKey(), EnumStatus.Status.SHOPPER_DECLINED.getName()," o shopper não aceitou seu pedido.\nClique no carrinho novamente ou no botão abaixo.");

                        order.setStatus(EnumStatus.Status.SHOPPER_DECLINED.getName().toString());
                        mDatabase.child("history").push().child(order.getShopper()).setValue(order);

                        order.setShopper(null);
                        order.setStatus(null);
                        mDatabase.child("cart_online").child(order.getUser().toString()).child("status").removeValue();
                        mDatabase.child("cart_online").child(order.getUser().toString()).child("shopper").removeValue();
                        mDatabase.child("shoppers").child(getArguments().get("shopper_uid") == null ? FirebaseAuth.getInstance().getCurrentUser().getUid().toString() : (String)getArguments().get("shopper_uid")).child("is_free").setValue(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });

        btnCall = (Button) view.findViewById(R.id.btn_call);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderFragmentPermissionsDispatcher.makeCallWithCheck(OrderFragment.this);
            }
        });

        mDatabase.child("cart_online").orderByChild("shopper").equalTo(getArguments().get("shopper_uid") == null ? FirebaseAuth.getInstance().getCurrentUser().getUid().toString() : (String)getArguments().get("shopper_uid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(OrderFragment.this.getContext() != null){

                    Utils.openDialog(OrderFragment.this.getContext(),"Carregando pedido");

                    if (dataSnapshot.hasChildren()) {

                        for (DataSnapshot st : dataSnapshot.getChildren()) {
                            order = st.getValue(Cart.class);
                            order.setUser(st.getKey());

                            if(order.getStatus().equalsIgnoreCase(EnumStatus.Status.SHOPPER_PAYING.getName())){
                                getProducts();

                            }else{
                                int countUnity = 0;
                                for(Map.Entry<String, ProductCart> entry : order.getProducts().entrySet()) {
                                    ProductCart value = entry.getValue();
                                    countUnity = countUnity + value.getQuantity();
                                }
                                shopperOrder.setText(order.getProducts().size() + " itens ( " + String.valueOf(countUnity) + " unidades )");

                                llNoOrder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);

                                getStore(order.getNetwork(),order.getStore());
                                getUser(st.getKey());

                                ((MainShopperActivity)getActivity()).bottomBarBadge.setCount(1);
                            }
                        }

                    }else{

                        llNoOrder.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);

                        ((MainShopperActivity)getActivity()).bottomBarBadge.setCount(0);

                    }

                    Utils.closeDialog(OrderFragment.this.getContext());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(orderStore.getLat(), orderStore.getLng())));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(orderStore.getLat(),orderStore.getLng()), 5f);
        googleMap.moveCamera(cameraUpdate);
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
                    mapFragment.getMapAsync(OrderFragment.this);
                 }
             }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUser(String uuid){

        mDatabase.child("users").child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChildren()){

                    User user = dataSnapshot.getValue(User.class);

                    userName.setText(user.getName());
                    userNumber.setText(user.getNumber());
                    if(user.getUser_img() != null){
                        Glide.with(OrderFragment.this).load(user.getUser_img()).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(userImage);
                    }else{
                        progressBar.setVisibility(View.GONE);
                    }

                 Utils.closeDialog(OrderFragment.this.getContext());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OrderFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
        Log.i("Pagpeg", "Erro ao conectar: " + connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location != null && storeLocation != null){
            Float distanceTo = location.distanceTo(storeLocation) / 1000;
            orderStore.setDistance(distanceTo);
            storeKm.setText(String.valueOf(orderStore.getDistance()) + " km");
        }

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

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    public void getShopperLocation(){

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @NeedsPermission(android.Manifest.permission.CALL_PHONE)
    public void makeCall(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + userNumber.getText().toString()));
        startActivity(intent);
    }

    private void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void startLocationUpdates(){
        OrderFragmentPermissionsDispatcher.getShopperLocationWithCheck(this);
    }

    private void getProducts(){

        count = 0;

        for(final Map.Entry<String, ProductCart> entry : order.getProducts().entrySet()) {

            final ProductCart value = entry.getValue();

            mDatabase.child("product").child(entry.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    count++;

                    if(dataSnapshot.hasChildren()){
                        Product p = dataSnapshot.getValue(Product.class);
                        p.setName(dataSnapshot.getKey());
                        value.setProduct(p);
                    }

                    if(count >= order.getProducts().size()){

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order",order);
                        fragment = new OrderTabFragment();
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

}
