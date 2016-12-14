package com.br.pagpeg.fragment.shopper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.BarCodeActivity;
import com.br.pagpeg.activity.shopper.ProductDetailActivity;
import com.br.pagpeg.adapter.shopper.OrderListAdapter;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.utils.DividerItemDecoration;
import com.br.pagpeg.utils.EnumStatus;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunolemgruber on 28/07/16.
 */

public class OrderListItemFragment extends Fragment {

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<ProductCart> productCartsAll;
    private ProductCart productCartSelect;
    private DatabaseReference mDatabase;
    public String user = "";
    private int idxSelected;
    private Double totalPriceShopper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        productCartsAll = (List<ProductCart>) getArguments().getSerializable("productsCart");

        mLayoutManager = new LinearLayoutManager(OrderListItemFragment.this.getActivity());
        mAdapter = new OrderListAdapter(onClickListener(),OrderListItemFragment.this.getContext(),productCartsAll);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(OrderListItemFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return view;
    }

    private OrderListAdapter.OrderListOnClickListener onClickListener() {
        return new OrderListAdapter.OrderListOnClickListener() {
            @Override
            public void onClickItem(View view, int idx) {
                idxSelected = idx;
                productCartSelect = productCartsAll.get(idx);
                IntentIntegrator.forSupportFragment(OrderListItemFragment.this).setCaptureActivity(BarCodeActivity.class).initiateScan();
            }

            @Override
            public void onClickButton(View view, int idx) {

                productCartSelect = productCartsAll.get(idx);
                productCartSelect.setStatus(EnumStatus.Status.PRODUCT_NOT_FIND.getName());
                mDatabase.child("cart_online").child(user).child("products").child(productCartSelect.getProduct().getName()).child("status").setValue(EnumStatus.Status.PRODUCT_NOT_FIND.getName());
                productCartsAll.remove(idx);
                updateView(productCartsAll);

                Toast.makeText(getActivity(), "Produto não foi achado", Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if(requestCode == 1){

            if(resultCode == Activity.RESULT_OK){

                productCartSelect = (ProductCart) data.getSerializableExtra("productCart");
                mDatabase.child("cart_online").child(user).child("products").child(productCartSelect.getProduct().getName()).child("status").setValue(EnumStatus.Status.PRODUCT_FIND.getName());
                mDatabase.child("cart_online").child(user).child("products").child(productCartSelect.getProduct().getName()).child("shopper_quantity").setValue(productCartSelect.getShopper_quantity());
                mDatabase.child("cart_online").child(user).child("products").child(productCartSelect.getProduct().getName()).child("shopper_price_total").setValue(productCartSelect.getShopper_price_total());
                mDatabase.child("cart_online").child(user).child("products").child(productCartSelect.getProduct().getName()).child("shopper_price_unit").setValue(productCartSelect.getShopper_price_unit());
                mDatabase.child("cart_online").child(user).child("total_price_shopper").setValue(0.0);
                productCartsAll.get(idxSelected).setStatus(EnumStatus.Status.PRODUCT_FIND.getName());
                productCartsAll.get(idxSelected).setShopper_quantity(productCartSelect.getShopper_quantity());
                productCartsAll.remove(idxSelected);

                updateView(productCartsAll);

            }
            if (resultCode == Activity.RESULT_CANCELED) {}
            
        }else{

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {

                if(result.getContents() != null){

                    if(String.valueOf(productCartSelect.getProduct().getBar_code()).equalsIgnoreCase(result.getContents())){

                        Intent intent = new Intent(OrderListItemFragment.this.getActivity(),ProductDetailActivity.class);
                        intent.putExtra("productCart",productCartSelect);
                        startActivityForResult(intent,1);

                    }else{
                        Toast.makeText(getActivity(), "Produto não encontrado", Toast.LENGTH_LONG).show();
                    }

                } else {

                    //INICIO TESTE EMULADOR
                    Intent intent = new Intent(OrderListItemFragment.this.getActivity(),ProductDetailActivity.class);
                    intent.putExtra("productCart",productCartSelect);
                    startActivityForResult(intent,1);
                    //FIM TESTE EMULADOR

                    //Toast.makeText(getActivity(), "Produto não encontrado", Toast.LENGTH_LONG).show();
                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
  
    }

    public void updateView(List<ProductCart> productCarts){
        productCartsAll = new ArrayList<>();
        productCartsAll.addAll(productCarts);
        mAdapter.notifyDataSetChanged();
    }
}


