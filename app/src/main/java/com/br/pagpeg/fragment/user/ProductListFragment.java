package com.br.pagpeg.fragment.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.BarCodeActivity;
import com.br.pagpeg.activity.user.MainUserActivity;
import com.br.pagpeg.adapter.user.ProductAdapter;
import com.br.pagpeg.model.Product;
import com.br.pagpeg.model.StoreCategory;
import com.br.pagpeg.utils.DividerItemDecoration;
import com.br.pagpeg.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductListFragment extends Fragment {

    private static View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;
    private ImageView mIconListImageView;
    private ImageView mIconMapImageView;
    private ImageView mIconBarCode;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private AlertDialog builder = null;
    private StoreCategory category;
    private DatabaseReference mDatabase;
    private List<Product> products = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        category = (StoreCategory) getArguments().getSerializable("category");

        Utils.openDialog(ProductListFragment.this.getContext(),"Carregando produtos");
        getProducts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_product, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle(category.getName());

        mIconMapImageView = (ImageView) toolbarMainActivity.findViewById(R.id.ic_mapStore);
        mIconListImageView = (ImageView) toolbarMainActivity.findViewById(R.id.ic_listStore);
        mIconBarCode = (ImageView) toolbarMainActivity.findViewById(R.id.ic_bar_code);
        mIconListImageView.setVisibility(View.GONE);
        mIconMapImageView.setVisibility(View.GONE);
        mIconBarCode.setVisibility(View.VISIBLE);

        mIconBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(ProductListFragment.this).setCaptureActivity(BarCodeActivity.class).initiateScan();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(ProductListFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(ProductListFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        snackbar = Snackbar
                .make(coordinatorLayout, "Produto adicionado.", Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbarUndo = Snackbar.make(coordinatorLayout, "Produto removido.", Snackbar.LENGTH_SHORT);
                        snackbarUndo.show();
                    }
                });

        builder = new AlertDialog.Builder(getActivity(), R.style.Dialog_Quantity)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancelar", null)
                .setTitle("PagPeg")
                .setMessage("Quantidade do produto")
                .setIcon(R.mipmap.ic_launcher)
                .setView(new EditText(ProductListFragment.this.getContext()))
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.show();
                        ((MainUserActivity)getActivity()).mBottomBar.makeBadgeForTabAt(2, getResources().getColor(R.color.colorPrimary), 3);
                        builder.dismiss();
                    }
                });
                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });

        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                Log.d("MainActivity", "Cancelled scan");
            } else {
                Log.d("MainActivity", "Scanned");
                builder.show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private ProductAdapter.ProductOnClickListener onClickListener() {
        return new ProductAdapter.ProductOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {

                fragment = new ProductDetailFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        };
    }

    private void getProducts(){

        mDatabase.child("product").orderByChild("category").equalTo(category.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                products.clear();

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                        Product product = st.getValue(Product.class);
                        product.setName(st.getKey());
                        products.add(product);
                    }
                }

                if(products.size() != 0)
                    recyclerView.setAdapter(new ProductAdapter(onClickListener(),ProductListFragment.this.getContext(),products));
                Utils.closeDialog(ProductListFragment.this.getContext());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
