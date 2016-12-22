package com.br.pagpeg.fragment.shopper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.pagpeg.R;
import com.br.pagpeg.adapter.shopper.OrderHistoryAdapter;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.Store;
import com.br.pagpeg.utils.DividerItemDecoration;
import com.br.pagpeg.utils.EnumToolBar;
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
 * Created by brunolemgruber on 27/07/16.
 */

public class OrderHistoryFragment extends Fragment {

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private List<Cart> orders;
    private Store orderStore;
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Histórico de pedidos");
        Utils.setIconBar(EnumToolBar.SHOPPERHISTORY,toolbar);

        getHistory();

        mLayoutManager = new LinearLayoutManager(OrderHistoryFragment.this.getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(OrderHistoryFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return view;
    }

    private OrderHistoryAdapter.OrderOnClickListener onClickListener() {
        return new OrderHistoryAdapter.OrderOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {
                fragment = new OrderHistoryProductFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        };
    }

    private void getHistory(){

        Utils.openDialog(OrderHistoryFragment.this.getContext(),"Carregando");

        if(orders == null)
            orders = new ArrayList<>();

        mDatabase.child("history").orderByChild("shopper").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                        count++;

                        Cart order = st.getValue(Cart.class);
                        getStore(order,dataSnapshot);
                        orders.add(order);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getStore(final Cart order, final DataSnapshot dtSnap){

        mDatabase.child("network").child(order.getNetwork()).child(order.getStore()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    orderStore = dataSnapshot.getValue(Store.class);
                    order.setStoreDetail(orderStore);
                }

                if (count >= dtSnap.getChildrenCount()) {

                    mAdapter = new OrderHistoryAdapter(onClickListener(),OrderHistoryFragment.this.getContext(),orders);
                    recyclerView.setAdapter(mAdapter);

                    Utils.closeDialog(OrderHistoryFragment.this.getContext());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
