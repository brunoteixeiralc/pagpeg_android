package com.br.pagpeg.fragment.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.adapter.user.CartAdapter;
import com.br.pagpeg.model.Product;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.utils.DividerItemDecoration;
import com.br.pagpeg.utils.EnumIconBar;
import com.br.pagpeg.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunolemgruber on 18/07/16.
 */

public class CartFragment  extends Fragment{

    private static View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;
    private Button btnDiscount,btnBuy,btnCCManage;
    private EditText discount;
    private DatabaseReference mDatabase;
    private List<ProductCart> productCarts;
    private Toolbar toolbar;
    private ProductCart productCart;
    private TextView txtDiscount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_cart, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Utils.openDialog(CartFragment.this.getContext(),"Carregando produtos");

        if(productCarts == null){
            productCarts = new ArrayList<>();
        }

        txtDiscount = (TextView) view.findViewById(R.id.discount);

        if(recyclerView == null){

            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            mLayoutManager = new LinearLayoutManager(CartFragment.this.getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);

            recyclerView.addItemDecoration(new DividerItemDecoration(CartFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        }

        getProductsCart();

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        Utils.setIconBar(EnumIconBar.CART,toolbar);

        View dialoglayout = CartFragment.this.getActivity().getLayoutInflater().inflate(R.layout.content_alert_discount, null);
        discount = (EditText) dialoglayout.findViewById(R.id.discount);
        final AlertDialog builder = new AlertDialog.Builder(getActivity(), R.style.Dialog_Quantity)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancelar", null)
                .setTitle("PagPeg")
                .setMessage("Código do desconto")
                .setIcon(R.mipmap.ic_launcher)
                .setView(dialoglayout)
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        validadeDiscount(discount.getText().toString());
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

        btnDiscount = (Button) view.findViewById(R.id.btn_discount);
        btnDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.show();
            }
        });

        btnBuy = (Button) view.findViewById(R.id.buy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment = new StepToPickUpFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        btnCCManage = (Button) view.findViewById(R.id.ccManage);
        btnCCManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment = new CreditCardFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        return  view;
    }

    private CartAdapter.CartOnClickListener onClickListener() {
        return new CartAdapter.CartOnClickListener() {
            @Override
            public void onClick(View view, Product product) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("product",product);

                fragment = new ProductDetailFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        };
    }

    private CartAdapter.RemoveCartOnClickListener cartOnClickListener() {
        return new CartAdapter.RemoveCartOnClickListener() {
            @Override
            public void onClick(View view, Product product) {
                deleteProductCart(product.getName());
            }
        };
    }

    private void getProductsCart(){

        mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                productCarts.clear();

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                        productCart = st.getValue(ProductCart.class);
                        productCart.setProduct(new Product());
                        productCart.setName(st.getKey());
                        productCarts.add(productCart);
                    }

                    if(productCarts.size() != 0){

                        toolbar.setTitle("Resumo do pedido ( " + String.format("%02d", productCarts.size()) + " itens )");

                        mAdapter = new CartAdapter(onClickListener(),cartOnClickListener(),CartFragment.this.getContext(),productCarts,mDatabase);
                        recyclerView.setAdapter(mAdapter);
                    }

                    Utils.closeDialog(CartFragment.this.getContext());

                }else{

                    Utils.closeDialog(CartFragment.this.getContext());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void validadeDiscount(String code){

        mDatabase.child("discount").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    txtDiscount.setText("Desconto : R$ " + dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void deleteProductCart(final String name){

        mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("products").child(name).removeValue();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
