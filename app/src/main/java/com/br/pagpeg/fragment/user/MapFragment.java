package com.br.pagpeg.fragment.user;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.br.pagpeg.fragment.user.MapFragmentPermissionsDispatcher;
import com.br.pagpeg.model.ClusterMarkerLocation;
import com.br.pagpeg.utils.ClusterRenderer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Random;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


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

        setRetainInstance(true);

        //Toolbar MainActivity
        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Lojas nas proximidades");
        mIconListImageView = (ImageView) toolbarMainActivity.findViewById(R.id.ic_listStore);
        mIconListImageView.setVisibility(View.VISIBLE);

        mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapFragment.this);
        googleApiClient = new GoogleApiClient.Builder(MapFragment.this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        return view;

    }

    private void setUpMap(Location l) {

        if (gMap != null && l != null) {

            mCenterLocation = new LatLng(l.getLatitude(),l.getLongitude());

            initMarkers();

//            LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
//            final CameraPosition position = new CameraPosition.Builder().target(latLng)
//                    .bearing(0).tilt(0).zoom(10).build();
//            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
//            gMap.animateCamera(update, 1000, new GoogleMap.CancelableCallback() {
//                @Override
//                public void onCancel() {
//                    Toast.makeText(MapFragment.this.getActivity(), "Ação cancelada!", Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onFinish() {
//                    //Toast.makeText(MapFragment.this.getActivity(), "PagPeg te achou!", Toast.LENGTH_LONG).show();
//                }
//            });
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
        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

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

    private void initMarkers() {
        ClusterManager<ClusterMarkerLocation> clusterManager = new ClusterManager<ClusterMarkerLocation>( MapFragment.this.getContext(), gMap );
        gMap.setOnCameraChangeListener(clusterManager);

        clusterManager.setRenderer(new ClusterRenderer(MapFragment.this.getContext(),gMap,clusterManager));

        double lat;
        double lng;
        Random generator = new Random();
        for( int i = 0; i < 30; i++ ) {
            lat = generator.nextDouble();
            lng = generator.nextDouble();
            if( generator.nextBoolean() ) {
                lat = -lat;
            }
            if( generator.nextBoolean() ) {
                lng = -lng;
            }
            clusterManager.addItem(new ClusterMarkerLocation(new LatLng(mCenterLocation.latitude + lat, mCenterLocation.longitude + lng )));
        }
    }

}
