package com.br.pagpeg.fragment.user;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.ClusterMarkerLocation;
import com.br.pagpeg.model.Store;
import com.br.pagpeg.utils.ClusterRenderer;
import com.br.pagpeg.utils.EnumIconBar;
import com.br.pagpeg.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.br.pagpeg.R.id.map;


/**
 * Created by brunolemgruber on 30/01/15.
 */
@RuntimePermissions
public class MapFragment extends Fragment implements com.google.android.gms.maps.OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap gMap;
    private View view;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;
    protected LatLng mCenterLocation;
    private ImageView mIconListImageView;
    private DatabaseReference mDatabase;
    private List<Store> storeList = new ArrayList<>();
    private ClusterMarkerLocation clickedClusterItem;
    private Fragment fragment;
    private Bundle bundle;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.content_map, container, false);
        } catch (InflateException e) {
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Lojas nas proximidades");

        Utils.setIconBar(EnumIconBar.STOREMAP,toolbar);

        mIconListImageView = (ImageView) toolbar.findViewById(R.id.ic_listStore);
        mIconListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment = new StoreListFragment();
                bundle = new Bundle();
                bundle.putSerializable("stores", (Serializable) storeList);
                fragment.setArguments(bundle);

                if(fragment != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                    mIconListImageView.setVisibility(View.GONE);
                }
            }
        });

        mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(MapFragment.this);
        googleApiClient = new GoogleApiClient.Builder(MapFragment.this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        return view;

    }

    private void setUpMap(Location l) {

        if (gMap != null && l != null) {

            Utils.openDialog(MapFragment.this.getContext(),"Carregando lojas");

            mCenterLocation = new LatLng(l.getLatitude(),l.getLongitude());
            initMarkers(l);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("Pagpeg", "Conectado ao google play service");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Pagpeg", "Conexão interrompida");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("Pagpeg", "Erro ao conectar: " + connectionResult);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        MapFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @Override
    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {

        gMap = googleMap;
        MapFragmentPermissionsDispatcher.setlocationWithCheck(this,gMap);

    }

    @Override
    public void onStart() {

        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {

        stopLocationUpdates();
        googleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {

        setUpMap(location);
    }


    private void stopLocationUpdates(){

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

    }

    public void startLocationUpdates(){

        MapFragmentPermissionsDispatcher.setStartLocationUpdatesWithCheck(this);
    }

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    public void setlocation(GoogleMap gMap){

        gMap.setMyLocationEnabled(true);
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Location l = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        setUpMap(l);
    }

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    public void setStartLocationUpdates(){

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void initMarkers(Location l) {

        ClusterManager<ClusterMarkerLocation> clusterManager = new ClusterManager<ClusterMarkerLocation>( MapFragment.this.getContext(), gMap );

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterMarkerLocation>() {
            @Override
            public boolean onClusterItemClick(ClusterMarkerLocation clusterMarkerLocation) {
                clickedClusterItem = clusterMarkerLocation;
                return false;
            }
        });

        gMap.setOnMarkerClickListener(clusterManager);
        gMap.setOnCameraChangeListener(clusterManager);
        gMap.setOnInfoWindowClickListener(new MyMarkerInfoWindowClickListener());
        gMap.setInfoWindowAdapter(clusterManager.getMarkerManager());

        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new ClusterMarkerLocationAdapter());
        clusterManager.setRenderer(new ClusterRenderer(MapFragment.this.getContext(),gMap,clusterManager));
        getStores(clusterManager,l);
    }

    public class ClusterMarkerLocationAdapter implements GoogleMap.InfoWindowAdapter{

        private final View view;

        ClusterMarkerLocationAdapter() {
            view = getActivity().getLayoutInflater().inflate(R.layout.info_windows_map, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {

            TextView title = (TextView) view.findViewById(R.id.txtTitle);
            title.setText(clickedClusterItem.getTitle());

            TextView snippet = (TextView) view.findViewById(R.id.txtSnippet);
            snippet.setText(clickedClusterItem.getSnippet());

            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    private class MyMarkerInfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener {
        @Override
        public void onInfoWindowClick(Marker marker) {

        }
    }

    private void getStores(final ClusterManager<ClusterMarkerLocation> clManager, final Location l){

        mDatabase.child("network").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                storeList.clear();

                if(dataSnapshot.hasChildren()){

                    if(dataSnapshot.hasChildren()){
                        for (DataSnapshot nt: dataSnapshot.getChildren()) {
                            for (DataSnapshot st: nt.getChildren()) {

                                Store store = st.getValue(Store.class);

                                Location storeLocation=new Location("storeLocation");
                                storeLocation.setLatitude(store.getLat());
                                storeLocation.setLongitude(store.getLng());

                                Float distanceTo = l.distanceTo(storeLocation) / 1000;
                                store.setDistance(distanceTo);

                                store.setNetwork(nt.getKey());
                                store.setKeyStore(st.getKey());

                                storeList.add(store);
                                clManager.addItem(new ClusterMarkerLocation(new LatLng(store.getLat(), store.getLng()),store.getName(),store.getAddress()));
                            }
                        }

                        Utils.closeDialog(MapFragment.this.getContext());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
