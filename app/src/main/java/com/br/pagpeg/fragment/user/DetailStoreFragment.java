package com.br.pagpeg.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.user.OpenCatalog;
import com.br.pagpeg.adapter.user.CategoryAdapter;
import com.br.pagpeg.model.Promotion;
import com.br.pagpeg.model.Store;
import com.br.pagpeg.model.StoreCategory;
import com.br.pagpeg.utils.DividerItemDecoration;
import com.br.pagpeg.utils.EnumToolBar;
import com.br.pagpeg.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by brunolemgruber on 15/07/16.
 */

public class DetailStoreFragment extends Fragment implements OnMapReadyCallback,SearchView.OnQueryTextListener{

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private Store store;
    private SupportMapFragment mapFragment;
    private Fragment fragment;
    private TextView name,address,distance,txtCatalog;
    private ProgressBar progressBar;
    public ImageView img;
    private DatabaseReference mDatabase;
    private List<StoreCategory> storeCategories  = new ArrayList<>();
    private Toolbar toolbar;
    private ImageView catalog;
    private Promotion promotion;
    private List<Promotion> promotions;
    private StoreCategory category;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Utils.openDialog(DetailStoreFragment.this.getContext(),"Carregando categorias");
        getCategories();
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
            view = inflater.inflate(R.layout.content_store_detail, container, false);
        } catch (InflateException e) {
        }

        store = (Store) getArguments().getSerializable("store");

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(store.getNetwork());

        Utils.setIconBar(EnumToolBar.STOREDETAIL,toolbar);

        catalog = (ImageView) view.findViewById(R.id.catalog);
        txtCatalog = (TextView) view.findViewById(R.id.txt_catalog);
        validatePromotionNetwork();

        name = (TextView) view.findViewById(R.id.name);
        address = (TextView) view.findViewById(R.id.address);
        distance = (TextView) view.findViewById(R.id.km);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        img = (ImageView) view.findViewById(R.id.img);
        Glide.with(DetailStoreFragment.this.getContext()).load(store.getImg()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);

        name.setText(store.getName());
        address.setText(store.getAddress());

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        distance.setText(String.valueOf(df.format(store.getDistance())) + " km");

        if(recyclerView == null){

            recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
            mLayoutManager = new LinearLayoutManager(DetailStoreFragment.this.getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(DetailStoreFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        }

        mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DetailStoreFragment.this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });
    }

    private CategoryAdapter.CategoryOnClickListener onClickListener() {
        return new CategoryAdapter.CategoryOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {

                StoreCategory category = storeCategories.get(idx);

                Bundle bundle = new Bundle();
                bundle.putSerializable("store",store);
                bundle.putSerializable("category",category);

                fragment = new ProductListFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(store.getLat(),store.getLng())));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(store.getLat(),store.getLng()), 5f);
        googleMap.moveCamera(cameraUpdate);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void getCategories(){

        mDatabase.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                storeCategories.clear();
                category = new StoreCategory();
                category.setName("Todos produtos em oferta");
                storeCategories.add(category);

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                        category = new StoreCategory();
                        category.setName(st.getKey());
                        storeCategories.add(category);
                    }
                }

                recyclerView.setAdapter(new CategoryAdapter(onClickListener(),DetailStoreFragment.this.getActivity(),storeCategories));
                Utils.closeDialog(DetailStoreFragment.this.getContext());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void validatePromotionNetwork(){

        mDatabase.child("promotions").child(store.getNetwork()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                promotions = new ArrayList<Promotion>();
                java.util.Date date = new java.util.Date();

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                        promotion = st.getValue(Promotion.class);
                        promotion.setKey(st.getKey());
                        boolean isValidate = validateDates((date.getTime()/1000),promotion.getStart(),promotion.getEnd());
                        if(isValidate)
                            promotions.add(promotion);
                    }

                    if(promotions.size() != 0) {

                        catalog.setVisibility(View.VISIBLE);
                        txtCatalog.setVisibility(View.VISIBLE);

                        catalog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openPdf(promotions.get(0).getCatalog());
                            }
                        });

                        txtCatalog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openPdf(promotions.get(0).getCatalog());
                            }
                        });

                    }else{

                        catalog.setVisibility(View.GONE);
                        txtCatalog.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean validateDates(Long dateNow, Long date1, Long date2){

        Date dNow = new Date(dateNow * 1000);
        Date d1 = new Date(date1 * 1000);
        Date d2 = new Date(date2 * 1000);

        Calendar calNow = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        calNow.setTime(dNow);
        cal1.setTime(d1);
        cal2.setTime(d2);

        if(calNow.after(cal1) || calNow.equals(cal1)){
            if(calNow.before(cal2) || calNow.equals(cal2)){
                return true;
            }
        }
        return false;
    }

    private void openPdf(String urlPdf){

        Intent intent = new Intent(DetailStoreFragment.this.getContext(),OpenCatalog.class);
        intent.putExtra("url", urlPdf);
        startActivity(intent);

    }
}
