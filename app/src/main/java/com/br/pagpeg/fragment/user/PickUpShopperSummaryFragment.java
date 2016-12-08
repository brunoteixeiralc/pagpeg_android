package com.br.pagpeg.fragment.user;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baoyachi.stepview.HorizontalStepView;
import com.br.pagpeg.R;
import com.br.pagpeg.adapter.user.PickUpShopperSummaryAdapter;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.Product;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.model.Shopper;
import com.br.pagpeg.notification.SendNotification;
import com.br.pagpeg.utils.EnumStatus;
import com.br.pagpeg.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by brunolemgruber on 22/07/16.
 */

public class PickUpShopperSummaryFragment extends Fragment {

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private HorizontalStepView stepView;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private Cart order;
    int count;
    private String userUid;
    private List<ProductCart> productCarts;
    private Button btnBuyOrder;

    public PickUpShopperSummaryFragment(HorizontalStepView stepView) {
        this.stepView = stepView;
    }

    public PickUpShopperSummaryFragment(HorizontalStepView stepView, String userUid) {
        this.stepView = stepView;
        this.userUid = userUid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_pickup_shopper_summary, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (productCarts == null)
            productCarts = new ArrayList<>();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Lista de produtos encontrados");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(PickUpShopperSummaryFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        getOrder();
        stepView.setStepsViewIndicatorComplectingPosition(2);

        btnBuyOrder = (Button) view.findViewById(R.id.buy_order);
        btnBuyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShopper(order.getShopper());
            }
        });

        return view;
    }

    private PickUpShopperSummaryAdapter.PickUpSummaryOnClickListener onClickListener() {
        return new PickUpShopperSummaryAdapter.PickUpSummaryOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {


            }
        };
    }

    private void getOrder() {

        Utils.openDialog(PickUpShopperSummaryFragment.this.getContext(), "Carregando pedido");

        mDatabase.child("cart_online").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    order = dataSnapshot.getValue(Cart.class);
                    order.setUser(dataSnapshot.getKey());

                    for (Map.Entry<String, ProductCart> entry : order.getProducts().entrySet()) {

                        final ProductCart value = entry.getValue();

                        mDatabase.child("product").child(entry.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                count++;

                                if (dataSnapshot.hasChildren()) {
                                    Product p = dataSnapshot.getValue(Product.class);
                                    p.setName(dataSnapshot.getKey());
                                    value.setProduct(p);

                                    productCarts.add(value);
                                }

                                if (count >= order.getProducts().size()) {

                                    mAdapter = new PickUpShopperSummaryAdapter(onClickListener(), PickUpShopperSummaryFragment.this.getContext(), productCarts);
                                    recyclerView.setAdapter(mAdapter);

                                    Utils.closeDialog(PickUpShopperSummaryFragment.this.getContext());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
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
                        SendNotification.sendNotificationShopper(shopper.getName(), shopper.getOne_signal_key(), dataSnapshot.getKey(), "Usuário aprovou sua compra ",false);
                        mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(EnumStatus.Status.SHOPPER_PAYING.getName());

                        btnBuyOrder.setEnabled(false);
                        btnBuyOrder.setText("Aguarde um momento, shopper pagando!");
                        btnBuyOrder.setBackgroundColor(R.color.green_button);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
